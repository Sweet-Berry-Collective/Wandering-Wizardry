package dev.sweetberry.wwizardry.recipe;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

public class AltarCatalyzationRecipeSerializer implements QuiltRecipeSerializer<AltarCatalyzationRecipe> {
	public static final AltarCatalyzationRecipeSerializer INSTANCE = new AltarCatalyzationRecipeSerializer();

	@Override
	public AltarCatalyzationRecipe read(Identifier id, JsonObject json) {
		var schema = gson.fromJson(json, JsonSchema.class);
		if (schema.catalyst == null) {
			throw new JsonSyntaxException("Missing field 'catalyst'");
		}
		if (schema.inputs == null) {
			throw new JsonSyntaxException("Missing field 'inputs'");
		}
		if (schema.result == null) {
			throw new JsonSyntaxException("Missing field 'result'");
		}
		var catalyst = Ingredient.fromJson(schema.catalyst);
		var inputs = new Ingredient[4];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = Ingredient.fromJson(schema.inputs[i]);
		}

		var result = ShapedRecipe.outputFromJson(schema.result);

		return new AltarCatalyzationRecipe(id, catalyst, inputs, result, schema.keepCatalyst, schema.bloom);
	}

	Gson gson = new Gson();

	@Override
	public JsonObject toJson(AltarCatalyzationRecipe recipe) {
		var obj = new JsonObject();
		obj.add("type", new JsonPrimitive(AltarCatalyzationRecipe.TYPE.id().toString()));
		obj.add("catalyst", recipe.catalyst().toJson());
		var inputs = new JsonArray();
		for (var input : recipe.inputs()) {
			inputs.add(input.toJson());
		}
		obj.add("inputs", inputs);
		ItemStack.CODEC.encode(recipe.result(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty())
				.result()
				.ifPresent(result -> obj.add("result", result));
		obj.addProperty("keepCatalyst", recipe.keepCatalyst());
		obj.addProperty("bloom", recipe.bloom());
		return obj;
	}

	@Override
	public AltarCatalyzationRecipe read(Identifier id, PacketByteBuf buf) {
		var catalyst = Ingredient.fromPacket(buf);
		var inputs = new Ingredient[4];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = Ingredient.fromPacket(buf);
		}
		var result = buf.readItemStack();
		var keepCatalyst = buf.readBoolean();
		var bloom = buf.readInt();
		return new AltarCatalyzationRecipe(id, catalyst, inputs, result, keepCatalyst, bloom);
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

	public static class JsonSchema {
		public JsonObject catalyst;
		public JsonObject[] inputs;
		public JsonObject result;
		public boolean keepCatalyst;
		public int bloom;
	}
}
