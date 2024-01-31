package dev.sweetberry.wwizardry.content.sounds;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundInitializer {
	public static final SoundEvent DISC_WANDERING = registerSound("music_disc.wandering");

	public static void init() {

	}

	public static SoundEvent registerSound(String name) {
		var id = Mod.id(name);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}
}
