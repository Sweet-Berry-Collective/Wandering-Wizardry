package dev.sweetberry.wwizardry.content.block.sign;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModdedWallSignBlock extends WallSignBlock implements ModdedSignBlock {
	private final ResourceLocation signId;

	public ModdedWallSignBlock(Properties properties, ResourceLocation signId) {
		super(WoodType.OAK, properties);
		this.signId = signId;
	}

	@Override
	public ResourceLocation getSignId() {
		return signId;
	}
}
