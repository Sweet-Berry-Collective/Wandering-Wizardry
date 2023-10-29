package dev.sweetberry.wwizardry.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class WanderingGameRules {
	public static final GameRules.Key<GameRules.IntRule> ALTAR_SCULK_SPREAD_MULTIPLIER = GameRuleRegistry.register("wwizardry:altarSculkSpreadMultiplier", GameRules.Category.UPDATES, GameRuleFactory.createIntRule(10));

	public static void init() {}
}
