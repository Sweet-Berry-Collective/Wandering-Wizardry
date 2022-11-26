package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class AltarPedestalBlockEntity extends BlockEntity {
	public float rand = (float)(Math.floor(Math.random()*Math.PI*4)/4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public static final BlockEntityType<AltarPedestalBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarPedestalBlockEntity::new, AltarPedestalBlock.INSTANCE).build();
	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {

	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		var compound = new NbtCompound();
		heldItem.writeNbt(compound);
		nbt.put("HeldItem", compound);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		var compound = nbt.getCompound("HeldItem");
		heldItem = ItemStack.fromNbt(compound);
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

	@Override
	public void markDirty() {
		if (world != null) {
			if (world.isClient()) {
				MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
			} else {
				((ServerWorld) world).getChunkManager().markForUpdate(pos);
			}
			super.markDirty();
		}
	}
}
