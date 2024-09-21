package dev.sweetberry.wwizardry.client.render.animation;

import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

public record Keyframe(EasingFunction function, double endTime, Vec3 posModifier, Vec3 angleModifier) {
	public Tuple<Vec3, Vec3> apply(Keyframe last, double timeOffset) {
		var value = function.ease(timeOffset);
		var pos = last.posModifier.equals(posModifier)
			? posModifier
			: last.posModifier.lerp(posModifier, value);

		var angle = last.angleModifier.equals(angleModifier)
			? angleModifier
			: last.angleModifier.lerp(angleModifier, value);

		return new Tuple<>(pos, angle);
	}
}
