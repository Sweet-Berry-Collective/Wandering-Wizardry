package dev.sweetberry.wwizardry.content.sounds;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

public class SoundInitializer {
	public static final RegistryContext<SoundEvent> SOUNDS = new RegistryContext<>(BuiltInRegistries.SOUND_EVENT);
	public static final Lazy<SoundEvent> DISC_WANDERING = registerSound("music_disc.wandering");
	public static final Lazy<SoundEvent> SNAIL_PLACE = registerSound("entity.snail.place");
	public static final Lazy<SoundEvent> SNAIL_BREAK = registerSound("entity.snail.break");
	public static final Lazy<SoundEvent> SHELL_PLACE = registerSound("block.shell.place");
	public static final Lazy<SoundEvent> SHELL_BREAK = registerSound("block.shell.break");
	public static final Lazy<SoundEvent> SHELL_STEP = registerSound("block.shell.step");
	public static final Lazy<SoundType> SNAIL = Lazy.create(() -> new SoundType(1, 1.25f, SHELL_BREAK.get(), SHELL_STEP.get(), SHELL_PLACE.get(), SHELL_BREAK.get(), SHELL_STEP.get()));

	public static Lazy<SoundEvent> registerSound(String name) {
		var id = WanderingWizardry.id(name);
		return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
	}
}
