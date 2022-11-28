package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import io.github.sweetberrycollective.wwizardry.recipe.AltarRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.sculk.SculkBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.ArrayList;

public class AltarCatalyzerBlockEntity extends BlockEntity implements Inventory {
	public float rand = (float) (Math.floor(Math.random() * Math.PI * 4) / 4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public boolean crafting = false;

	public int craftingTick = 0;

	public ItemStack result = ItemStack.EMPTY;

	public boolean keepCatalyst = false;
	private final SculkBehavior behavior = SculkBehavior.createBehavior();
	public int bloom = 0;

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();
	public boolean shouldUpdateClient = false;

	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	public void startCrafting(AltarRecipe recipe) {
		result = recipe.result().copy();
		crafting = true;
		craftingTick = 0;
		keepCatalyst = recipe.keepCatalyst();
		bloom = recipe.bloom();
		markDirty();
		var neighbors = getNeighbors();
		for (var n : neighbors) {
			n.startCrafting();
		}
		world.updateNeighbors(pos, AltarCatalyzerBlock.INSTANCE);
	}

	public void tryCraft() {
		var optional = world.getRecipeManager().getFirstMatch(AltarRecipe.TYPE, this, world);
		optional.ifPresent(this::startCrafting);
	}

	public void cancelCraft() {
		result = ItemStack.EMPTY;
		crafting = false;
		craftingTick = 0;
		bloom = 0;
		var neighbors = getNeighbors();
		for (var n : neighbors) {
			n.cancelCraft();
		}
		world.updateNeighbors(pos, AltarCatalyzerBlock.INSTANCE);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		behavior.updateCharges(world, pos, world.random, true);
		markDirty();
		if (crafting) {
			if (++craftingTick >= 100) {
				craftingTick = 0;
				crafting = false;
				if (!keepCatalyst) heldItem = ItemStack.EMPTY;
				world.updateNeighbors(pos, state.getBlock());
				world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, 0, 0, 0);
				var stackEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, result.copy());
				result = ItemStack.EMPTY;
				world.spawnEntity(stackEntity);
				world.playSound(pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 2, 1, true);
				if (!world.isClient && bloom > 0) {
					System.out.println(world);
					world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 2, 1, true);
					if (state.get(WanderingBlocks.NATURALLY_GENERATED)) {
						behavior.addCharge(pos, bloom);
					}
				}
				world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.create(state));
				bloom = 0;
				markDirty();
			}
			world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.25 * ((craftingTick + 30) / 100f), 0);
		} else {
			craftingTick = 0;
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		var compound = new NbtCompound();
		heldItem.writeNbt(compound);
		nbt.put("HeldItem", compound);
		nbt.putBoolean("crafting", crafting);
		nbt.putBoolean("keepCatalyst", keepCatalyst);
		behavior.write(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		var compound = nbt.getCompound("HeldItem");
		heldItem = ItemStack.fromNbt(compound);
		crafting = nbt.getBoolean("crafting");
		keepCatalyst = nbt.getBoolean("keepCatalyst");
		behavior.read(nbt);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return toNbt();
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	public ArrayList<AltarPedestalBlockEntity> getNeighbors() {
		var out = new ArrayList<AltarPedestalBlockEntity>();
		if (world == null) return out;
		var north = world.getBlockEntity(pos.north(2));
		var northState = world.getBlockState(pos.north(2));
		if (north instanceof AltarPedestalBlockEntity be && northState.get(HorizontalFacingBlock.FACING) == Direction.NORTH) out.add(be);
		var south = world.getBlockEntity(pos.south(2));
		var southState = world.getBlockState(pos.south(2));
		if (south instanceof AltarPedestalBlockEntity be && southState.get(HorizontalFacingBlock.FACING) == Direction.SOUTH) out.add(be);
		var east = world.getBlockEntity(pos.east(2));
		var eastState = world.getBlockState(pos.east(2));
		if (east instanceof AltarPedestalBlockEntity be && eastState.get(HorizontalFacingBlock.FACING) == Direction.EAST) out.add(be);
		var west = world.getBlockEntity(pos.west(2));
		var westState = world.getBlockState(pos.west(2));
		if (west instanceof AltarPedestalBlockEntity be && westState.get(HorizontalFacingBlock.FACING) == Direction.WEST) out.add(be);
		return out;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return heldItem == ItemStack.EMPTY || crafting;
	}

	@Override
	public ItemStack getStack(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		item.setCount(64);
		return item;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		return item;
	}

	@Override
	public ItemStack removeStack(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		return item;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (crafting) return;
		heldItem = stack.copy();
		heldItem.setCount(1);
		tryCraft();
	}

	@Override
	public void markDirty() {
		if (world != null) {
			if (world.isClient()) {
				shouldUpdateClient = true;
			} else {
				((ServerWorld) world).getChunkManager().markForUpdate(pos);
			}
			super.markDirty();
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public void clear() {
		heldItem = ItemStack.EMPTY;
	}
}
