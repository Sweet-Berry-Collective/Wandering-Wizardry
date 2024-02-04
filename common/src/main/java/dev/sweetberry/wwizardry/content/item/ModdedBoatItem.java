package dev.sweetberry.wwizardry.content.item;

import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;

import java.util.function.Predicate;

public class ModdedBoatItem extends Item {
	private static final Predicate<Entity> RIDERS = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

	private final ResourceLocation type;
	private final boolean chest;

	public ModdedBoatItem(ResourceLocation type, boolean chest, Item.Properties settings) {
		super(settings);

		this.type = type;
		this.chest = chest;
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var stack = user.getItemInHand(hand);

		var hitResult = Item.getPlayerPOVHitResult(world, user, ClipContext.Fluid.ANY);
		if (hitResult.getType() == HitResult.Type.MISS)
			return InteractionResultHolder.pass(stack);

		var rotationVec = user.getViewVector(1f);
		var riders = world.getEntities(user, user.getBoundingBox().expandTowards(rotationVec.scale(5d)).inflate(1d), RIDERS);

		// Prevent collision with user
		if (!riders.isEmpty()) {
			var eyePos = user.getEyePosition();
			for (var entity : riders) {
				var box = entity.getBoundingBox().inflate(entity.getPickRadius());
				if (box.contains(eyePos))
					return InteractionResultHolder.pass(stack);
			}
		}

		if (hitResult.getType() != HitResult.Type.BLOCK)
			return InteractionResultHolder.pass(stack);

		// Spawn boat entity

		var x = hitResult.getLocation().x;
		var y = hitResult.getLocation().y;
		var z = hitResult.getLocation().z;

		var boat = chest
			? new ChestBoat(world, x, y, z)
			: new Boat(world, x, y, z);

		ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, boat).type = type;

		boat.setYRot(user.getYRot());

		if (!world.noCollision(boat, boat.getBoundingBox().inflate(-0.1d)))
			return InteractionResultHolder.fail(stack);

		if (!world.isClientSide()) {
			world.addFreshEntity(boat);
			world.gameEvent(user, GameEvent.ENTITY_PLACE, BlockPos.containing(hitResult.getLocation()));

			if (!user.getAbilities().instabuild)
				stack.shrink(1);
		}

		user.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}
}
