package dev.sweetberry.wwizardry.block.entity;

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
import java.util.List;
import java.util.Optional;

public class AltarCatalyzerBlockEntity extends AltarBlockEntity {

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();

	public ItemStack result = ItemStack.EMPTY;

	public boolean keepCatalyst = false;
	private final SculkBehavior behavior = SculkBehavior.createBehavior();
	public int bloom = 0;
	public boolean shouldUpdateClient = false;

	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	@Override
	public void startCrafting(@Nullable Recipe<?> recipe) {
		if (recipe instanceof AltarCatalyzationRecipe altarRecipe) {
			result = altarRecipe.result().copy();
			keepCatalyst = altarRecipe.keepCatalyst();
			bloom = altarRecipe.bloom();
			for (var n : getNeighbors()) {
				n.startCrafting();
			}
		}
		if (recipe instanceof ShapelessRecipe shapelessRecipe) {
			result = shapelessRecipe.getResult(world.getRegistryManager()).copy();
			keepCatalyst = true;
			bloom = 0;
			for (var n : getNeighbors()) {
				n.startCrafting();
			}
		}
		super.startCrafting(recipe);
	}

	@Override
	public void tryCraft(BlockState state) {
		System.out.println("Test 1");

		if (world == null)
			return;

		System.out.println("Test 2");

		if (
			getNeighbors()
				.stream()
				.anyMatch(it -> it.heldItem.isEmpty())
		) return;

		System.out.println("Test 3");

		var optional = (Optional<Recipe<?>>) (Optional<?>) world.getRecipeManager().getFirstMatch(AltarCatalyzationRecipe.TYPE, this, world);

		System.out.println("Test 4");

		var proxy = new ShapelessProxy();

		System.out.println("Test 5");

		var shapeless = world.getRecipeManager()
			.listAllOfType(RecipeType.CRAFTING)
			.stream()
			.filter(it -> it instanceof ShapelessRecipe)
			.map(it -> (ShapelessRecipe) it)
			.filter(it -> it.matches(proxy, world))
			.findFirst();

		System.out.println("Test 6");

		optional.or(() -> shapeless).ifPresent(this::startCrafting);
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
			behavior.addCharge(pos, bloom);
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

	public class ShapelessProxy implements RecipeInputInventory {

		@Override
		public int getWidth() {
			return 2;
		}

		@Override
		public int getHeight() {
			return 2;
		}

		@Override
		public List<ItemStack> getIngredients() {
			return getNeighbors().stream().map(it -> it.heldItem.isOf(WanderingItems.SLOT_CHARM) ? ItemStack.EMPTY : it.heldItem).toList();
		}

		@Override
		public int size() {
			return 4;
		}

		@Override
		public boolean isEmpty() {
			return getIngredients().isEmpty();
		}

		@Override
		public ItemStack getStack(int slot) {
			return getIngredients().get(slot);
		}

		@Override
		public ItemStack removeStack(int slot, int amount) {
			return getIngredients().get(slot);
		}

		@Override
		public ItemStack removeStack(int slot) {
			return getIngredients().get(slot);
		}

		@Override
		public void setStack(int slot, ItemStack stack) {

		}

		@Override
		public void markDirty() {

		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return false;
		}

		@Override
		public void provideRecipeInputs(RecipeMatcher finder) {
			getIngredients().forEach(finder::addInput);
		}

		@Override
		public void clear() {

		}
	}
}
