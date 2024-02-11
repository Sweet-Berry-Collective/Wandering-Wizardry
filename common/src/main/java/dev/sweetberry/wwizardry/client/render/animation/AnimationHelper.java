package dev.sweetberry.wwizardry.client.render.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class AnimationHelper {
	public static void applyKeyframes(Keyframe[] frames, double time, PoseStack stack) {
		var tuple = applyKeyframes(frames, time);
		var angle = tuple.getB();
		var pos = tuple.getA();
		stack.mulPose(new Quaternionf().rotateXYZ((float)Math.toRadians(angle.x), (float)Math.toRadians(angle.y), (float)Math.toRadians(angle.z)));
		stack.translate(pos.x, pos.y, pos.z);
	}

	public static Tuple<Vec3, Vec3> applyKeyframes(Keyframe[] frames, double time) {
		if (frames.length == 0)
			return new Tuple<>(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
		if (frames.length == 1)
			return new Tuple<>(frames[0].posModifier(), frames[0].posModifier());
		var lastIdx = Math.max(0, Mth.binarySearch(0, frames.length, it -> time <= frames[it].endTime()) - 1);
		var last = frames[lastIdx];
		var nextIdx = Math.min(frames.length - 1, lastIdx + 1);
		var next = frames[nextIdx];
		var timeOffset = (time - last.endTime()) / (next.endTime() - last.endTime());
		return next.apply(last, timeOffset);
	}
}
