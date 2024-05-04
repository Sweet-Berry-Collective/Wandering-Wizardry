package dev.sweetberry.wwizardry.content.entity;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Snail extends Animal implements VariantHolder<Snail.Variant> {
	public static final TagKey<Item> FOOD = TagKey.create(Registries.ITEM, WanderingWizardry.id("snail_food"));
	public static final TagKey<Block> DAMAGES = TagKey.create(Registries.BLOCK, WanderingWizardry.id("damages_snail"));
	public static final String VARIANT_KEY = "variant";

	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);

	public AvoidDamagingBlocksGoal avoidBlocks;

	protected Snail(EntityType<Snail> type, Level level) {
		super(type, level);

		setVariant(Variant.randomNonSlug(level().random));
	}

	@Override
	public boolean onClimbable() {
		return true;
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(4, new TemptGoal(this, 1, (s) -> s.is(FOOD), false));
		goalSelector.addGoal(3, new BreedGoal(this, 1));
		goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
		goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		goalSelector.addGoal(8, new AvoidEntityGoal<>(this, Armadillo.class, 8, 1.6, 1.4));
		avoidBlocks = new AvoidDamagingBlocksGoal(this, 1.6, 8);
		goalSelector.addGoal(16, avoidBlocks);
	}

	public static AttributeSupplier createAttributes() {
		return Mob
			.createMobAttributes()
			.add(Attributes.MOVEMENT_SPEED, 0.1)
			.add(Attributes.MAX_HEALTH, 5.0)
			.add(Attributes.FOLLOW_RANGE, 8.0)
			.add(Attributes.SAFE_FALL_DISTANCE, 16.0)
			.build();
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(FOOD);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		var entity = EntityInitializer.SNAIL.get().create(serverLevel);
		entity.setVariant(Variant.SLUG);
		entity.setAge(-5000);
		return entity;
	}

	@Override
	public void tick() {
		boolean wasBaby = isBaby();
		super.tick();

		if (wasBaby && !isBaby())
			setVariant(Variant.randomNonSlug(level().random));

		if (getBlockStateOn().is(DAMAGES))
			hurt(level().damageSources().dryOut(), 0.125f);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.isShiftKeyDown() || isBaby())
			return super.mobInteract(player, hand);
		var level = level();
		var variant = getVariant();
		var stack = player.getItemInHand(hand);
		if (variant != Variant.SLUG && stack.is(Items.SHEARS)) {
			setVariant(Variant.SLUG);
			level().playSound(player, BlockPos.containing(getPosition(0)), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS);
			if (!player.isCreative()) {
				stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
				if (level instanceof ServerLevel serverLevel) {
					var item = new ItemEntity(serverLevel, getX(), getY(), getZ(), ItemInitializer.SNAIL_SHELL.get().getDefaultInstance());
					serverLevel.addFreshEntity(item);
				}
			}
			return InteractionResult.SUCCESS;
		} else if (variant == Variant.SLUG && stack.is(ItemInitializer.SNAIL_SHELL.get())) {
			setVariant(Variant.randomNonSlug(level().random));
			level().playSound(player, BlockPos.containing(getPosition(0)), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS);
			if (!player.isCreative())
				stack.consume(1, player);
			return InteractionResult.SUCCESS;
		}

		return super.mobInteract(player, hand);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putInt(VARIANT_KEY, getRawVariant());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		setRawVariant(nbt.getInt(VARIANT_KEY));
	}

	@Override
	public void setVariant(Variant variant) {
		setRawVariant(variant.ordinal());
	}

	private void setRawVariant(int variant) {
		if (!(level() instanceof ServerLevel))
			return;
		entityData.set(VARIANT, variant);
	}

	@Override
	@NotNull
	public Variant getVariant() {
		return Variant.values()[getRawVariant()];
	}

	private int getRawVariant() {
		return entityData.get(VARIANT);
	}

	public enum Variant implements StringRepresentable {
		NORMAL,
		SLUG,
		MOSS,
		SCULK;

		public static final Variant[] NON_SLUG = {
			NORMAL,
			MOSS,
			SCULK
		};

		@Override
		public String getSerializedName() {
			return toString().toLowerCase();
		}

		public String getTextureName() {
			return "textures/entity/snail/" + getSerializedName() + ".png";
		}

		public static Variant random(RandomSource random) {
			return values()[random.nextInt(values().length)];
		}

		public static Variant randomNonSlug(RandomSource random) {
			return NON_SLUG[random.nextInt(NON_SLUG.length)];
		}
	}

	public static class AvoidDamagingBlocksGoal extends MoveToBlockGoal {
		public AvoidDamagingBlocksGoal(PathfinderMob self, double speed, int searchRange) {
			super(self, speed, searchRange);
		}

		@Override
		protected boolean isValidTarget(LevelReader levelReader, BlockPos pos) {
			return !levelReader.getBlockState(pos).is(DAMAGES);
		}
	}
}
