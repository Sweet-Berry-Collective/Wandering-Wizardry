package dev.sweetberry.wwizardry.client;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import dev.sweetberry.wwizardry.block.*;
import dev.sweetberry.wwizardry.client.render.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.component.VoidBagComponent;
import dev.sweetberry.wwizardry.datagen.WanderingDatagen;
import dev.sweetberry.wwizardry.datagen.WoodType;
import dev.sweetberry.wwizardry.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.item.VoidBagItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.resource.Material;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;
import org.quiltmc.qsl.tooltip.api.client.ItemTooltipCallback;

import java.util.List;

public class WanderingClient implements ClientModInitializer {
	public static int tickCounter = 0;
	public static int useItemTick = -1;

	public static final List<Text> VOID_BAG_LOCKED = List.of(
		Text.empty(),
		Text.translatable("wwizardry.void_bag.generic_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.generic_2").formatted(Formatting.DARK_PURPLE),
		Text.empty(),
		Text.translatable("wwizardry.void_bag.locked_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.locked_2").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.locked_3").formatted(Formatting.DARK_PURPLE)
	);
	public static final List<Text> VOID_BAG_UNLOCKED = List.of(
		Text.empty(),
		Text.translatable("wwizardry.void_bag.generic_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.generic_2").formatted(Formatting.DARK_PURPLE),
		Text.empty(),
		Text.translatable("wwizardry.void_bag.unlocked_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.unlocked_2").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.unlocked_3").formatted(Formatting.DARK_PURPLE)
	);

	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockEntityRendererFactories.register(AltarPedestalBlockEntity.TYPE, AltarPedestalBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(AltarCatalyzerBlockEntity.TYPE, AltarCatalyzerBlockEntityRenderer::new);
		ClientWorldTickEvents.END.register((client, world) -> tickCounter++);
		BlockRenderLayerMap.put(RenderLayer.getCutout(),
			AltarPedestalBlock.INSTANCE,
			AltarCatalyzerBlock.INSTANCE,
			CameraBlock.INSTANCE,
			ResonatorBlock.INSTANCE,
			SculkflowerBlock.INSTANCE,
			WanderingBlocks.INDIGO_CAERULEUM,
			WanderingBlocks.MODULO_COMPARATOR,
			WanderingBlocks.REINFORCED_GLASS,
			WanderingBlocks.REINFORCED_GLASS_PANE
		);
		WanderingDatagen.REGISTRY.forEach(dataGenerator -> {
			if (dataGenerator instanceof WoodType woodType) {
				BlockRenderLayerMap.put(RenderLayer.getCutout(), woodType.DOOR, woodType.TRAPDOOR, woodType.SAPLING);
				SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, woodType.SIGN.getTexture()));
				SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, woodType.HANGING_SIGN.getTexture()));
				if (!woodType.fungus) {
					BlockRenderLayerMap.put(RenderLayer.getCutout(), woodType.LEAVES);
					TerraformBoatClientHelper.registerModelLayers(WanderingMod.id(woodType.baseName), false);
				}
			}
		});

		ItemTooltipCallback.EVENT.register(((stack, _player, context, lines) -> {
			if (!stack.isOf(VoidBagItem.INSTANCE))
				return;
			var player = _player == null ? MinecraftClient.getInstance().player : _player;
			if (player == null)
				return;
			var bag = VoidBagComponent.getForPlayer(player);
			lines.addAll(
				1,
				bag.locked ? VOID_BAG_LOCKED : VOID_BAG_UNLOCKED
			);
		}));

		ModelPredicateProviderRegistry.register(VoidBagItem.INSTANCE, WanderingMod.id("void_bag_closed"), (itemStack, clientWorld, livingEntity, i) -> {
			if (!itemStack.isOf(VoidBagItem.INSTANCE)) return 0.0f;
			var client = MinecraftClient.getInstance();
			if (client.player == null)
				return 0.0f;
			var nbt = itemStack.getNbt();
			if (nbt != null && nbt.contains("Locked")) return nbt.getBoolean("Locked") ? 1.0f : 0.0f;
			var bag = VoidBagComponent.getForPlayer(client.player);
			return bag.locked ? 1 : 0;
		});

		ModelPredicateProviderRegistry.register(
			SoulMirrorItem.INSTANCE,
			WanderingMod.id("cracked"),
			(itemStack, clientWorld, livingEntity, i) -> SoulMirrorItem.INSTANCE.isFullyUsed(itemStack) ? 1 : 0
		);
	}
}
