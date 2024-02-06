package dev.sweetberry.wwizardry.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AltarCatalyzationRecipeSerializer implements RecipeSerializer<AltarCatalyzationRecipe> {
	public static final Codec<AltarCatalyzationRecipe> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Ingredient.CODEC_NONEMPTY
				.fieldOf("catalyst")
				.forGetter(AltarCatalyzationRecipe::catalyst),
			Ingredient.CODEC_NONEMPTY
				.listOf()
				.fieldOf("inputs")
				.flatXmap(
					list -> {
						Ingredient[] ingredients = list.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
						if (ingredients.length == 0)
							return DataResult.error(() -> "Too many inputs for altar recipe");
						else return ingredients.length > 4
							? DataResult.error(() -> "Too many inputs for altar recipe")
							: DataResult.success(List.of(ingredients));
					},
					DataResult::success
				)
				.forGetter(AltarCatalyzationRecipe::inputs),
			ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(AltarCatalyzationRecipe::result),
			ExtraCodecs.strictOptionalField(Codec.BOOL, "keepCatalyst", true).forGetter(AltarCatalyzationRecipe::keepCatalyst),
			ExtraCodecs.strictOptionalField(Codec.INT, "bloom", 0).forGetter(AltarCatalyzationRecipe::bloom)
		).apply(instance, AltarCatalyzationRecipe::new)
	);

	@Override
	public Codec<AltarCatalyzationRecipe> codec() {
		return CODEC;
	}

	@Override
	public AltarCatalyzationRecipe fromNetwork(FriendlyByteBuf buf) {
		var catalyst = Ingredient.fromNetwork(buf);
		var inputs = new Ingredient[4];
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = Ingredient.fromNetwork(buf);
		var result = buf.readItem();
		var keepCatalyst = buf.readBoolean();
		var bloom = buf.readInt();
		return new AltarCatalyzationRecipe(catalyst, Arrays.stream(inputs).toList(), result, keepCatalyst, bloom);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, AltarCatalyzationRecipe recipe) {
		recipe.catalyst().toNetwork(buf);
		for (Ingredient input : recipe.inputs())
			input.toNetwork(buf);
		buf.writeItem(recipe.result());
		buf.writeBoolean(recipe.keepCatalyst());
		buf.writeInt(recipe.bloom());
	}
}
