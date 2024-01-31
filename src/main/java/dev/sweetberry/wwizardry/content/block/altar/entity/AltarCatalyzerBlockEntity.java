package dev.sweetberry.wwizardry.content.block.altar.entity;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.gamerule.GameruleInitializer;
import dev.sweetberry.wwizardry.content.recipe.AltarCatalyzationRecipe;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class AltarCatalyzerBlockEntity extends AltarBlockEntity {

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();

	public ItemStack result = ItemStack.EMPTY;
	private final SculkSpreader behavior = SculkSpreader.createLevelSpreader();
	public int bloom = 0;
	public boolean shouldUpdateClient = false;

	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	@Override
	public void startCrafting(AltarRecipeView recipe) {
		var bloomMultiplier = level.getGameRules().getRule(GameruleInitializer.ALTAR_SCULK_SPREAD_MULTIPLIER).get();
		bloom = (int) Math.floor(recipe.getBloom() * bloomMultiplier);
		result = recipe.getRecipeResult();
		for (var neighbor : getNeighbors())
			neighbor.startCrafting(recipe);
		super.startCrafting(recipe);
	}

	@Override
	public void tryCraft(BlockState state) {
		if (level == null)
			return;

		var view = new RecipeView();

		var neighbors = getNeighbors();

		if (
			neighbors.size() != 4
			|| neighbors
				.stream()
				.anyMatch(it -> it.heldItem.isEmpty())
		) return;

		var optional = level.getRecipeManager().getRecipeFor(AltarCatalyzationRecipe.TYPE, this, level);

		if (optional.isPresent()) {
			optional.get().value().tryCraft(view, level);
			startCrafting(view);
			return;
		}

		if (AltarCraftable.EVENT.invoker().tryCraft(view, level))
			startCrafting(view);
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
		level.addParticle(ParticleTypes.SONIC_BOOM, worldPosition.getX() + 0.5, worldPosition.getY() + 5.5, worldPosition.getZ() + 0.5, 0, 0, 0);
		var stackEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 5.5, worldPosition.getZ() + 0.5, result.copy());
		result = ItemStack.EMPTY;
		level.addFreshEntity(stackEntity);
		level.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 5.5, worldPosition.getZ() + 0.5, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 1, 1, true);
		if (!level.isClientSide && bloom > 0) {
			level.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 1, 1, true);
			behavior.addCursors(worldPosition, bloom);
		}
		level.gameEvent(GameEvent.BLOCK_CHANGE, worldPosition, GameEvent.Context.of(state));
		bloom = 0;
		super.finishCrafting(state);
	}

	@Override
	public Block getBlock() {
		return AltarCatalyzerBlock.INSTANCE;
	}

	@Override
	public void tick(Level world, BlockPos pos, BlockState state) {
		behavior.updateCursors(world, pos, world.random, true);
		setChanged();
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
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		behavior.save(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		behavior.load(nbt);
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
		BlockPos pedestalPos = worldPosition.relative(direction, 2);
		if (level.getBlockEntity(pedestalPos) instanceof AltarPedestalBlockEntity pedestal
			&& level.getBlockState(pedestalPos).getValue(HorizontalDirectionalBlock.FACING) == direction) {
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
