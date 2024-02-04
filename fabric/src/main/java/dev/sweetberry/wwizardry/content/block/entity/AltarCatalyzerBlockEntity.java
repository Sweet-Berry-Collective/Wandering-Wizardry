package dev.sweetberry.wwizardry.content.block.entity;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.gamerule.GameruleInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.net.packet.AltarCraftPacket;
import dev.sweetberry.wwizardry.content.recipe.AltarCatalyzationRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
import java.util.List;

public class AltarCatalyzerBlockEntity extends AltarBlockEntity {

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = BlockEntityType.Builder.of(
		AltarCatalyzerBlockEntity::new,
		AltarCatalyzerBlock.INSTANCE
	).build(null);

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
		if (level.isClientSide || !(level instanceof ServerLevel serverLevel))
			return;

		var payload = new AltarCraftPacket(worldPosition, bloom > 0);

		serverLevel
			.getPlayers(it -> true)
			.forEach(it ->
				PacketRegistry.sendToClient(it, payload)
			);

		var stackEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 5.5, worldPosition.getZ() + 0.5, result.copy());
		result = ItemStack.EMPTY;
		level.addFreshEntity(stackEntity);
		if (bloom > 0)
			behavior.addCursors(worldPosition, bloom);
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
		public boolean ingredientsMatch(List<Ingredient> inputs) {
			var met = new boolean[] {false, false, false, false};
			// Iterate through the cardinal directions
			for (var dir : AltarDirection.cardinals()) {
				// Iterate through the ingredients
				for (var j = 0; j < 4; j++) {
					var heldItem = getItemInPedestal(dir);
					var isSlotCharm = heldItem.getItem() == ItemInitializer.SLOT_CHARM;
					// If this ingredient was already met, skil
					if (!met[j]) {
						// Check if the ingredient is met in this slot
						met[j] = inputs.size() > j
							? inputs.get(j).test(heldItem) || (isSlotCharm && inputs.get(j).test(ItemStack.EMPTY))
							: isSlotCharm;
						// If it was met, break out of the inner loop
						if (met[j]) j = 5;
					}
				}
			}
			// Check if any of them haven't been met
			for (var b : met)
				if (!b)
					return false;
			return true;
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
