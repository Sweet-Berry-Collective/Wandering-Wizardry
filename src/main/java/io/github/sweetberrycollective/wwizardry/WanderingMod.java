package io.github.sweetberrycollective.wwizardry;

import io.github.sweetberrycollective.wwizardry.block.Sculkable;
import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import io.github.sweetberrycollective.wwizardry.datagen.WallCandleBlockType;
import io.github.sweetberrycollective.wwizardry.datagen.WanderingDatagen;
import io.github.sweetberrycollective.wwizardry.item.WanderingItems;
import io.github.sweetberrycollective.wwizardry.recipe.WanderingRecipes;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.registry.api.event.RegistryEvents;
import org.quiltmc.qsl.registry.api.event.RegistryMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderingMod implements ModInitializer {
	public static final String MODID = "wwizardry";
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

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

	public static void onBlockAdded(Block block, Identifier id) {
		if (block instanceof CandleBlock candleBlock)
			onCandleBlockAdded(candleBlock, id);

	}

	public static void onCandleBlockAdded(CandleBlock candleBlock, Identifier id) {
		WanderingDatagen.registerDataGenerator(WallCandleBlockType.transformId(id), new WallCandleBlockType(id, candleBlock));
	}

	@Override
	public void onInitialize(ModContainer mod) {
		WanderingBlocks.init();
		WanderingItems.init();
		WanderingRecipes.init();
		WanderingDatagen.init();
		UseBlockCallback.EVENT.register(WanderingMod::onBlockUse);
		RegistryMonitor.create(Registries.BLOCK).forAll((ctx) -> onBlockAdded(ctx.value(), ctx.id()));
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
