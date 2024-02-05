package dev.sweetberry.wwizardry.content.block.sign;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public interface ModdedSignBlock {
	Set<ResourceLocation> SIGNS = new HashSet<>();

	ResourceLocation getSignId();
}
