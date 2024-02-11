package dev.sweetberry.wwizardry.client.render.animation;

@FunctionalInterface
public interface EasingFunction {
	double ease(double time);

	static double linear(double time) {
		return time;
	}

	static double inOutBack(double time) {
		var c1 = 1.70158;
		var c2 = c1 * 1.525;

		return time < 0.5
			? (Math.pow(2 * time, 2) * ((c2 + 1) * 2 * time - c2)) / 2
			: (Math.pow(2 * time - 2, 2) * ((c2 + 1) * (time * 2 - 2) + c2) + 2) / 2;
	}
}
