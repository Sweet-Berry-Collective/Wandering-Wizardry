package dev.sweetberry.wwizardry.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.class_8785;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.dynamic.Codecs;

import java.util.Arrays;
import java.util.List;

public class AltarCatalyzationRecipeSerializer implements RecipeSerializer<AltarCatalyzationRecipe> {
	public static final AltarCatalyzationRecipeSerializer INSTANCE = new AltarCatalyzationRecipeSerializer();
	public static final Codec<AltarCatalyzationRecipe> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Ingredient.field_46096
				.fieldOf("catalyst")
				.forGetter(AltarCatalyzationRecipe::catalyst),
			Ingredient.field_46096
				.listOf()
				.fieldOf("inputs")
				.flatXmap(
					list -> {
						Ingredient[] ingredients = list.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
						if (ingredients.length == 0) {
							return DataResult.error(() -> "Too many inputs for altar recipe");
						} else {
							return ingredients.length > 4
								? DataResult.error(() -> "Too many inputs for altar recipe")
								: DataResult.success(List.of(ingredients));
						}
					},
					DataResult::success
				)
				.forGetter(AltarCatalyzationRecipe::inputs),
			class_8785.field_46092.fieldOf("result").forGetter(AltarCatalyzationRecipe::result),
			Codecs.method_53049(Codec.BOOL, "keepCatalyst", true).forGetter(AltarCatalyzationRecipe::keepCatalyst),
			Codecs.method_53049(Codec.INT, "bloom", 0).forGetter(AltarCatalyzationRecipe::bloom)
		).apply(instance, AltarCatalyzationRecipe::new)
	);

	@Override
	public Codec<AltarCatalyzationRecipe> method_53736() {
		return CODEC;
	}

	@Override
	public AltarCatalyzationRecipe read(PacketByteBuf buf) {
		var catalyst = Ingredient.fromPacket(buf);
		var inputs = new Ingredient[4];
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = Ingredient.fromPacket(buf);
		var result = buf.readItemStack();
		var keepCatalyst = buf.readBoolean();
		var bloom = buf.readInt();
		return new AltarCatalyzationRecipe(catalyst, Arrays.stream(inputs).toList(), result, keepCatalyst, bloom);
	}

	@Override
	public void write(PacketByteBuf buf, AltarCatalyzationRecipe recipe) {
		recipe.catalyst().write(buf);
		for (Ingredient input : recipe.inputs()) {
			input.write(buf);
		}
		buf.writeItemStack(recipe.result());
		buf.writeBoolean(recipe.keepCatalyst());
		buf.writeInt(recipe.bloom());
	}
}
