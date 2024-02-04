package dev.sweetberry.wwizardry.content.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.level.GameRules;

public class GameruleInitializer {
	public static final GameRules.Key<DoubleRule> ALTAR_SCULK_SPREAD_MULTIPLIER = GameRuleRegistry.register("wwizardry:altarSculkSpreadMultiplier", GameRules.Category.UPDATES, GameRuleFactory.createDoubleRule(1));

	public static void init() {}
}
