package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import io.github.sweetberrycollective.wwizardry.recipe.AltarCatalyzationRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.ArrayList;

public class AltarCatalyzerBlockEntity extends AltarBlockEntity {

	public ItemStack result = ItemStack.EMPTY;

	public boolean keepCatalyst = false;
	private final SculkBehavior behavior = SculkBehavior.createBehavior();
	public int bloom = 0;

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();
	public boolean shouldUpdateClient = false;

	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	@Override
	public void startCrafting(AltarCatalyzationRecipe recipe) {
		result = recipe.result().copy();
		keepCatalyst = recipe.keepCatalyst();
		bloom = recipe.bloom();
		for (var n : getNeighbors()) {
			n.startCrafting();
		}
		super.startCrafting(recipe);
	}

	@Override
	public void tryCraft(BlockState state) {
		var optional = world.getRecipeManager().getFirstMatch(AltarCatalyzationRecipe.TYPE, this, world);
		optional.ifPresent(this::startCrafting);
	}

	public void cancelCraft() {
		result = ItemStack.EMPTY;
		bloom = 0;
		for (var n : getNeighbors()) {
			n.cancelCraft();
		}
		super.cancelCraft();
	}

	@Override
	public void finishCrafting(BlockState state, boolean removeHeldItem) {
		world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, 0, 0, 0);
		var stackEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, result.copy());
		result = ItemStack.EMPTY;
		world.spawnEntity(stackEntity);
		world.playSound(pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 2, 1, true);
		if (!world.isClient && bloom > 0) {
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 2, 1, true);
			if (state.get(WanderingBlocks.NATURALLY_GENERATED)) {
				behavior.addCharge(pos, bloom);
			}
		}
		world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));
		bloom = 0;
		super.finishCrafting(state, removeHeldItem);
	}

	@Override
	public Block getBlock() {
		return AltarCatalyzerBlock.INSTANCE;
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state) {
		behavior.updateCharges(world, pos, world.random, true);
		markDirty();
		if (crafting) {
			if (++craftingTick >= 100) {
				finishCrafting(state, !keepCatalyst);
			}
			world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.25 * ((craftingTick + 30) / 100f), 0);
		} else {
			craftingTick = 0;
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putBoolean("keepCatalyst", keepCatalyst);
		behavior.write(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		keepCatalyst = nbt.getBoolean("keepCatalyst");
		behavior.read(nbt);
	}

	public ArrayList<AltarPedestalBlockEntity> getNeighbors() {
		var out = new ArrayList<AltarPedestalBlockEntity>();
		// TODO accesswidener
		Direction[] directions = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };
		for (Direction direction : directions) {
			BlockPos pedestalPos = pos.offset(direction, 2);
			if (world.getBlockEntity(pedestalPos) instanceof AltarPedestalBlockEntity pedestal
					&& world.getBlockState(pedestalPos).get(HorizontalFacingBlock.FACING) == direction) {
				out.add(pedestal);
			}
		}
		return out;
	}
}
