package dev.sweetberry.wwizardry;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.block.Sculkable;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.component.VoidBagComponent;
import dev.sweetberry.wwizardry.datagen.WallHolderBlockType;
import dev.sweetberry.wwizardry.datagen.WanderingDatagen;
import dev.sweetberry.wwizardry.gamerule.WanderingGameRules;
import dev.sweetberry.wwizardry.item.VoidBagItem;
import dev.sweetberry.wwizardry.item.WanderingItems;
import dev.sweetberry.wwizardry.recipe.WanderingRecipes;
import dev.sweetberry.wwizardry.sounds.WanderingSounds;
import dev.sweetberry.wwizardry.world.WanderingWorldgen;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.registry.api.event.RegistryMonitor;
import org.quiltmc.qsl.villager.api.TradeOfferHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static dev.sweetberry.wwizardry.datagen.WallHolderBlockType.ParentType;

public class WanderingMod implements ModInitializer {
	public static final String MODID = "wwizardry";
	public static final Identifier VOID_BAG_PACKET = id("void_bag");
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

	public static final RegistryKey<PaintingVariant> ALTAR_PAINTING = RegistryKey.of(RegistryKeys.PAINTING_VARIANT, id("altar"));

	@Override
	public void onInitialize(ModContainer mod) {
		WanderingBlocks.init();
		WanderingItems.init();
		WanderingRecipes.init();
		WanderingDatagen.init();
		WanderingWorldgen.init();
		WanderingSounds.init();
		WanderingGameRules.init();

		TradeOfferHelper.registerWanderingTraderOffers(
			1,
			offers -> {
				offers.addAll(List.of(
					new TradeOffers.SellItemFactory(
						WanderingDatagen.DENIA_WOOD.SAPLING_ITEM,
						5, 1, 8, 1
					),
					new TradeOffers.SellItemFactory(
						WanderingDatagen.MYCHA_WOOD.SAPLING_ITEM,
						5, 1, 8, 1
					),
					new TradeOffers.SellItemFactory(
						WanderingItems.INDIGO_CAERULEUM,
						1, 1, 12, 1
					),
					new TradeOffers.SellItemFactory(
						WanderingItems.SCULKFLOWER,
						1, 1, 12, 1
					)
				));
			}
		);

		TradeOfferHelper.registerWanderingTraderOffers(
			2,
			offers -> {
				offers.add(
					new TradeOffers.SellItemFactory(
						WanderingItems.CRYSTALLINE_SCULK_SHARD,
						3, 1, 12, 5
					)
				);
			}
		);

		Registry.register(Registries.PAINTING_VARIANT, ALTAR_PAINTING, new PaintingVariant(32, 32));
		UseBlockCallback.EVENT.register(WanderingMod::onBlockUse);
		RegistryMonitor.create(Registries.BLOCK).forAll((ctx) -> onBlockAdded(ctx.value(), ctx.id()));
		ServerPlayNetworking.registerGlobalReceiver(VOID_BAG_PACKET, ((server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
				var bag = VoidBagComponent.getForPlayer(player);
				bag.openScreen();
			});
		}));

		AltarCraftable.EVENT.register((view, world) -> {
			if (view.getItemInPedestal(AltarRecipeView.AltarDirection.CENTER).getItem() instanceof AltarCraftable craftable)
				return craftable.tryCraft(view, world);
			return false;
		});
	}

	public static ActionResult onBlockUse(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if (!player.canModifyBlocks()) return ActionResult.PASS;
		var pos = hitResult.getBlockPos();
		var state = world.getBlockState(pos);
		var stack = player.getStackInHand(hand);
		var shouldSneak = false;
		if (state.getBlock() instanceof Sculkable sculkable) {
			shouldSneak = sculkable.hasPrimaryAction();
		}
		var isBasicallySneaking = player.isSneaking() || !shouldSneak;
		if (isBasicallySneaking) {
			var test = testPos(pos, state, player, world, hand, stack);
			if (test != ActionResult.PASS) return test;
		}
		if (state.contains(WanderingBlocks.SCULK_INFESTED) && !isBasicallySneaking) return ActionResult.PASS;
		var hitPos = pos.offset(hitResult.getSide());
		var hitState = world.getBlockState(hitPos);
		return testPos(hitPos, hitState, player, world, hand, stack);
	}

	public static ActionResult testPos(BlockPos pos, BlockState state, PlayerEntity player, World world, Hand hand, ItemStack stack) {
		if (!state.contains(WanderingBlocks.SCULK_INFESTED)) return ActionResult.PASS;

		boolean isInfested = state.get(WanderingBlocks.SCULK_INFESTED);

		if (stack.getItem() instanceof ShearsItem && isInfested) {
			world.setBlockState(pos, state.with(WanderingBlocks.SCULK_INFESTED, false));
			if (player instanceof ServerPlayerEntity serverPlayer) {
				Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
			}
			world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.5F, 1.0F);
			world.playSound(player, pos, SoundEvents.BLOCK_SCULK_BREAK, SoundCategory.BLOCKS, 1.5F, 1.0F);
			stack.damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
			if (!player.isCreative()) {
				var item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Items.SCULK_VEIN.getDefaultStack());
				world.spawnEntity(item);
			}
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));
			return ActionResult.SUCCESS;
		} else if (stack.getItem() == Items.SCULK_VEIN && !isInfested) {
			world.setBlockState(pos, state.with(WanderingBlocks.SCULK_INFESTED, true));
			if (player instanceof ServerPlayerEntity serverPlayer) {
				Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
			}
			world.playSound(player, pos, SoundEvents.BLOCK_SCULK_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!player.isCreative()) {
				stack.decrement(1);
			}
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	private static final String[] modid_exclusions = new String[] {
		"jello"
	};

	public static void onBlockAdded(Block block, Identifier id) {
		// Ignore known broken mods. TODO: Make this configurable
		if (
			Arrays.stream(modid_exclusions)
				.anyMatch(
					it -> it.equals(id.getNamespace())
				)
		)
			return;
		if (block instanceof CandleBlock)
			registerHolderBlock(block, id, ParentType.CANDLE);
		// Blocked out for now, until I can implement them
//		if (block instanceof TorchBlock)
//			registerHolderBlock(block, id, ParentType.TORCH, ParentType.TORCH_TOGGLEABLE);
//		if (block instanceof LanternBlock)
//			registerHolderBlock(block, id, ParentType.LANTERN, ParentType.LANTERN_TOGGLEABLE);
	}

	public static void registerHolderBlock(Block block, Identifier id, ParentType monostate, ParentType bistate) {
		registerHolderBlock(
			block,
			id,
			block.getDefaultState().contains(Properties.POWERED) ?
				bistate :
				monostate
		);
	}

	public static void registerHolderBlock(Block block, Identifier id, ParentType parentType) {
		WanderingDatagen.registerDataGenerator(WallHolderBlockType.transformId(id), new WallHolderBlockType(id, block, parentType));
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
