package dev.sweetberry.wwizardry.content.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public class SimpleTriggerCriterion extends SimpleCriterionTrigger<SimpleTriggerCriterion.Condition> {
	public static final Codec<Condition> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player")
				.forGetter(Condition::player)
		).apply(instance, Condition::new)
	);

	@Override
	public Codec<Condition> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player) {
		trigger(player, conditions -> true);
	}

	public record Condition(Optional<ContextAwarePredicate> player) implements SimpleInstance {}
}
