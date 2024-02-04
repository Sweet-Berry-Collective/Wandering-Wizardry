package dev.sweetberry.wwizardry.fabric.mixin;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.TorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TorchBlock.class)
public interface Accessor_TorchBlock {
	@Accessor
	SimpleParticleType getFlameParticle();
}
