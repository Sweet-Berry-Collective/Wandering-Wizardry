package dev.sweetberry.wwizardry.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AltarCatalyzationRecipeSerializer implements RecipeSerializer<AltarCatalyzationRecipe> {
	public static final MapCodec<AltarCatalyzationRecipe> CODEC = RecordCodecBuilder.mapCodec(
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
							return DataResult.error(() -> "Too few inputs for altar recipe");
						else return ingredients.length > 4
							? DataResult.error(() -> "Too many inputs for altar recipe")
							: DataResult.success(List.of(ingredients));
					},
					DataResult::success
				)
				.forGetter(AltarCatalyzationRecipe::inputs),
			ItemStack.CODEC.fieldOf("result").forGetter(AltarCatalyzationRecipe::result),
			Codec.BOOL.optionalFieldOf("keepCatalyst", true).forGetter(AltarCatalyzationRecipe::keepCatalyst),
			Codec.INT.optionalFieldOf("bloom", 0).forGetter(AltarCatalyzationRecipe::bloom)
		).apply(instance, AltarCatalyzationRecipe::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, AltarCatalyzationRecipe> STREAM_CODEC = StreamCodec.of(
		AltarCatalyzationRecipeSerializer::toNetwork, AltarCatalyzationRecipeSerializer::fromNetwork
	);

	@Override
	public MapCodec<AltarCatalyzationRecipe> codec() {
		return CODEC;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, AltarCatalyzationRecipe> streamCodec() {
		return STREAM_CODEC;
	}

	public static AltarCatalyzationRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
		var catalyst = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
		var inputs = new Ingredient[4];
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
		var result = ItemStack.STREAM_CODEC.decode(buf);
		var keepCatalyst = buf.readBoolean();
		var bloom = buf.readInt();
		return new AltarCatalyzationRecipe(catalyst, Arrays.stream(inputs).toList(), result, keepCatalyst, bloom);
	}

	public static void toNetwork(RegistryFriendlyByteBuf buf, AltarCatalyzationRecipe recipe) {
		Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.catalyst());
		for (Ingredient input : recipe.inputs())
			Ingredient.CONTENTS_STREAM_CODEC.encode(buf, input);
		ItemStack.STREAM_CODEC.encode(buf, recipe.result());
		buf.writeBoolean(recipe.keepCatalyst());
		buf.writeInt(recipe.bloom());
	}
}
