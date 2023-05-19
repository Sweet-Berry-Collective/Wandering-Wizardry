package dev.sweetberry.wwizardry.mixin;

import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TorchBlock.class)
public interface Accessor_TorchBlock {
	@Accessor
	ParticleEffect getParticle();
}
