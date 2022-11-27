package io.github.sweetberrycollective.wwizardry;

import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import io.github.sweetberrycollective.wwizardry.item.WanderingItems;
import io.github.sweetberrycollective.wwizardry.recipe.WanderingRecipes;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderingMod implements ModInitializer {
	public static final String MODID = "wwizardry";
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

	@Override
	public void onInitialize(ModContainer mod) {
		WanderingBlocks.init();
		WanderingItems.init();
		WanderingRecipes.init();
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			var pos = hitResult.getBlockPos();
			var state = world.getBlockState(pos);
			var stack = player.getStackInHand(hand);
			if (!player.isSneaking()) return ActionResult.PASS;
			if (stack.getItem() instanceof ShearsItem && state.contains(WanderingBlocks.SCULK_INFESTED) && state.get(WanderingBlocks.SCULK_INFESTED)) {
				world.setBlockState(pos, state.with(WanderingBlocks.SCULK_INFESTED, false));
				if (player instanceof ServerPlayerEntity serverPlayer) {
					Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
				}
				world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.5F, 1.0F);
				world.playSound(player, pos, SoundEvents.BLOCK_SCULK_BREAK, SoundCategory.BLOCKS, 1.5F, 1.0F);
				stack.damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
				return ActionResult.SUCCESS;
			} else if (stack.getItem() == Items.SCULK_VEIN && state.contains(WanderingBlocks.SCULK_INFESTED) && !state.get(WanderingBlocks.SCULK_INFESTED)) {
				world.setBlockState(pos, state.with(WanderingBlocks.SCULK_INFESTED, true));
				if (player instanceof ServerPlayerEntity serverPlayer) {
					Criteria.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
				}
				world.playSound(player, pos, SoundEvents.BLOCK_SCULK_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				stack.damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
