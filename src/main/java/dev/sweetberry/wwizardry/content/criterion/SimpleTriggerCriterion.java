package dev.sweetberry.wwizardry.content.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterionTrigger;
import net.minecraft.predicate.ContextAwarePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class SimpleTriggerCriterion extends AbstractCriterionTrigger<SimpleTriggerCriterion.Condition> {
	public static final Codec<Condition> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codecs.method_53048(EntityPredicate.field_47250, "player")
				.forGetter(Condition::comp_2029)
		).apply(instance, Condition::new)
	);

	@Override
	public Codec<Condition> method_54937/*getCodec*/() {
		return CODEC;
	}

	public void trigger(ServerPlayerEntity player) {
		trigger(player, conditions -> true);
	}

	public record Condition(Optional<ContextAwarePredicate> player) implements Conditions {

		@Override
		public Optional<ContextAwarePredicate> comp_2029/*getTriggeringPredicate*/() {
			return player;
		}
	}
}
