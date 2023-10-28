package dev.sweetberry.wwizardry.block.entity;

import dev.sweetberry.wwizardry.api.AltarCraftable;
import dev.sweetberry.wwizardry.api.AltarRecipeView;
import dev.sweetberry.wwizardry.block.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.item.WanderingItems;
import dev.sweetberry.wwizardry.recipe.AltarCatalyzationRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AltarCatalyzerBlockEntity extends AltarBlockEntity {

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();

	public ItemStack result = ItemStack.EMPTY;
	private final SculkBehavior behavior = SculkBehavior.createBehavior();
	public int bloom = 0;
	public boolean shouldUpdateClient = false;

	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	@Override
	public void startCrafting(AltarRecipeView recipe) {
		bloom = recipe.getBloom();
		result = recipe.getRecipeResult();
		for (var neighbor : getNeighbors())
			neighbor.startCrafting(recipe);
		super.startCrafting(recipe);
	}

	@Override
	public void tryCraft(BlockState state) {
		if (world == null)
			return;

		var view = new RecipeView();

		var neighbors = getNeighbors();

		if (
			neighbors.size() != 4
			|| neighbors
				.stream()
				.anyMatch(it -> it.heldItem.isEmpty())
		) return;

		var optional = world.getRecipeManager().getFirstMatch(AltarCatalyzationRecipe.TYPE, this, world);

		if (optional.isPresent()) {
			optional.get().tryCraft(view, world);
			startCrafting(view);
			return;
		}

		view.reset();

		if (
			heldItem.getItem() instanceof AltarCraftable craftable &&
			craftable.tryCraft(view, world)
		) {
			startCrafting(view);
			return;
		}
	}

	@Override
	public AltarRecipeView.AltarDirection getDirection(BlockState state) {
		return AltarRecipeView.AltarDirection.CENTER;
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
	public void finishCrafting(BlockState state) {
		world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, 0, 0, 0);
		var stackEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, result.copy());
		result = ItemStack.EMPTY;
		world.spawnEntity(stackEntity);
		world.playSound(pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 1, 1, true);
		if (!world.isClient && bloom > 0) {
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 1, 1, true);
			behavior.addCharge(pos, bloom);
		}
		world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));
		bloom = 0;
		super.finishCrafting(state);
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
				finishCrafting(state);
			}
			world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.25 * ((craftingTick + 30) / 100f), 0);
		} else {
			craftingTick = 0;
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		behavior.write(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		behavior.read(nbt);
	}

	public ArrayList<AltarPedestalBlockEntity> getNeighbors() {
		var out = new ArrayList<AltarPedestalBlockEntity>();
		// TODO accesswidener
		Direction[] directions = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };
		for (Direction direction : directions) {
			var neighbor = getNeighbor(direction);
			if (neighbor != null)
				out.add(neighbor);
		}
		return out;
	}

	@Nullable
	public AltarPedestalBlockEntity getNeighbor(Direction direction) {
		BlockPos pedestalPos = pos.offset(direction, 2);
		if (world.getBlockEntity(pedestalPos) instanceof AltarPedestalBlockEntity pedestal
			&& world.getBlockState(pedestalPos).get(HorizontalFacingBlock.FACING) == direction) {
			return pedestal;
		}
		return null;
	}

	private class RecipeView implements AltarRecipeView {
		protected final HashMap<AltarDirection, ItemStack> RESULTS = new HashMap<>();
		protected ItemStack recipeResult = ItemStack.EMPTY;
		protected int bloom;

		@Override
		public void reset() {
			RESULTS.clear();
			recipeResult = ItemStack.EMPTY;
			bloom = 0;
		}

		@Override
		public ItemStack getItemInPedestal(AltarDirection direction) {
			@Nullable AltarBlockEntity entity = switch (direction) {
				case CENTER -> AltarCatalyzerBlockEntity.this;
				case NORTH -> getNeighbor(Direction.NORTH);
				case SOUTH -> getNeighbor(Direction.SOUTH);
				case EAST -> getNeighbor(Direction.EAST);
				case WEST -> getNeighbor(Direction.WEST);
			};
			if (entity == null)
				return null;
			return entity.heldItem;
		}

		@Override
		public void setResultInPedestal(AltarDirection direction, ItemStack stack) {
			RESULTS.put(direction, stack);
		}

		@Override
		public ItemStack getResultInPedestal(AltarDirection direction) {
			var result = RESULTS.get(direction);
			if (result == null)
				return ItemStack.EMPTY;
			return result;
		}

		@Override
		public void setRecipeResult(ItemStack stack) {
			recipeResult = stack;
		}

		@Override
		public ItemStack getRecipeResult() {
			return recipeResult;
		}

		@Override
		public void setBloom(int bloom) {
			this.bloom = bloom;
		}

		@Override
		public int getBloom() {
			return bloom;
		}
	}
}
