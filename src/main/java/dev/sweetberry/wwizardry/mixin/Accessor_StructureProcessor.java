package dev.sweetberry.wwizardry.mixin;

import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureProcessor.class)
public interface Accessor_StructureProcessor {
	@Invoker
	StructureProcessorType<?> callGetType();
}
