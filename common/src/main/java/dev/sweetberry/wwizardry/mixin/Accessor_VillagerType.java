package dev.sweetberry.wwizardry.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(VillagerType.class)
public interface Accessor_VillagerType {
	@Accessor
	static Map<ResourceKey<Biome>, VillagerType> getBY_BIOME() {
		throw new IllegalCallerException("How?");
	}
}
