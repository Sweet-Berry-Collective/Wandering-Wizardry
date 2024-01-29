package dev.sweetberry.wwizardry.content.sounds;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class SoundInitializer {
	public static final SoundEvent DISC_WANDERING = registerSound("music_disc.wandering");

	public static void init() {

	}

	public static SoundEvent registerSound(String name) {
		var id = Mod.id(name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}
}
