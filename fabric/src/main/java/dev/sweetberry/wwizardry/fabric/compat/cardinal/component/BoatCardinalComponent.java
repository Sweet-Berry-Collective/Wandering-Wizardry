package dev.sweetberry.wwizardry.fabric.compat.cardinal.component;

import dev.sweetberry.wwizardry.content.component.BoatComponent;
import net.minecraft.world.entity.vehicle.Boat;

public class BoatCardinalComponent extends ProxyComponent<BoatComponent> {
	public Boat boat;

	public BoatCardinalComponent(Boat boat) {
		super(new BoatComponent(boat));
		this.boat = boat;
	}
}
