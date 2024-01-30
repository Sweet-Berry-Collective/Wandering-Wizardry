package dev.sweetberry.wwizardry.content.datagen;

import net.minecraft.feature_flags.FeatureFlagBitSet;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.*;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class ModdedPackProvider implements PackProvider {
	@Override
	public void register(Consumer<PackProfile> profileAdder) {
		profileAdder.accept(PackProfile.of(
			"wwizardry:resources",
			Text.of("Wandering Wizardry Resources"),
			true,
			new PackProfile.PackFactory() {
				@Override
				public ResourcePack openPrimary(String name) {
					return DatagenInitializer.pack;
				}

				@Override
				public ResourcePack open(String name, PackProfile.Info info) {
					return DatagenInitializer.pack;
				}
			},
			new PackProfile.Info(
				Text.empty(),
				PackCompatibility.COMPATIBLE,
				FeatureFlagBitSet.empty(),
				List.of()
			),
			PackProfile.InsertionPosition.TOP,
			true,
			PackSource.PACK_SOURCE_BUILTIN
		));
	}
}
