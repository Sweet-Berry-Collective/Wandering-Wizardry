package dev.sweetberry.wwizardry.content.sounds;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class SoundInitializer {
	public static final RegistryContext<SoundEvent> SOUNDS = new RegistryContext<>(BuiltInRegistries.SOUND_EVENT);
	public static final Lazy<SoundEvent> DISC_WANDERING = registerSound("music_disc.wandering");

	public static Lazy<SoundEvent> registerSound(String name) {
		var id = WanderingWizardry.id(name);
		return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
	}
}
