package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.content.block.Sculkable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class UseBlockHandler {
	public static InteractionResult onBlockUse(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction) {
		if (!player.mayBuild()) return InteractionResult.PASS;
		var state = world.getBlockState(pos);
		var stack = player.getItemInHand(hand);
		var shouldSneak = false;
		if (state.getBlock() instanceof Sculkable sculkable) {
			shouldSneak = sculkable.hasPrimaryAction();
		}
		var isBasicallySneaking = player.isShiftKeyDown() || !shouldSneak;
		if (isBasicallySneaking) {
			var test = testPos(pos, state, player, world, hand, stack);
			if (test != InteractionResult.PASS) return test;
		}
		if (state.hasProperty(Sculkable.SCULK_INFESTED) && !isBasicallySneaking) return InteractionResult.PASS;
		var hitPos = pos.relative(direction);
		var hitState = world.getBlockState(hitPos);
		return testPos(hitPos, hitState, player, world, hand, stack);
	}

	public static InteractionResult testPos(BlockPos pos, BlockState state, Player player, Level world, InteractionHand hand, ItemStack stack) {
		if (!state.hasProperty(Sculkable.SCULK_INFESTED)) return InteractionResult.PASS;

		boolean isInfested = state.getValue(Sculkable.SCULK_INFESTED);

		if (stack.getItem() instanceof ShearsItem && isInfested) {
			world.setBlockAndUpdate(pos, state.setValue(Sculkable.SCULK_INFESTED, false));
			if (player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
			}
			world.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 0.5F, 1.0F);
			world.playSound(player, pos, SoundEvents.SCULK_BLOCK_BREAK, SoundSource.BLOCKS, 1.5F, 1.0F);
			stack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));
			if (!player.isCreative()) {
				var item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Items.SCULK_VEIN.getDefaultInstance());
				world.addFreshEntity(item);
			}
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
			return InteractionResult.SUCCESS;
		} else if (stack.getItem() == Items.SCULK_VEIN && !isInfested) {
			world.setBlockAndUpdate(pos, state.setValue(Sculkable.SCULK_INFESTED, true));
			if (player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
			}
			world.playSound(player, pos, SoundEvents.SCULK_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!player.isCreative()) {
				stack.shrink(1);
			}
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
