package dev.sweetberry.wwizardry.content.block.sign;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModdedStandingSignBlock extends StandingSignBlock implements ModdedSignBlock {
	private final ResourceLocation signId;

	public ModdedStandingSignBlock(Properties properties, ResourceLocation signId) {
		super(WoodType.OAK, properties);
		this.signId = signId;
	}

	@Override
	public ResourceLocation getSignId() {
		return signId;
	}
}
