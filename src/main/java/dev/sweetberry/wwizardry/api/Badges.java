package dev.sweetberry.wwizardry.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.sweetberry.wwizardry.Mod;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

public class Badges {
	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

	private static final Gson GSON = new Gson();

	private static final Map<UUID, Component> BADGES_CACHE = new HashMap<>();

	private static final String BASE_URL = "https://badges.wwizardry.sweetberry.dev";

	private static final Style FONT = Style.EMPTY.withFont(Mod.id("badges"));
	private static final Style STYLE_DEV = FONT.withHoverEvent(translated("wwizardry.badge.developer"));
	private static final Component DEV = Component.literal("\u200B").setStyle(STYLE_DEV);
	private static final Style STYLE_ARTIST = FONT.withHoverEvent(translated("wwizardry.badge.artist"));
	private static final Component ARTIST = Component.literal("\u200C").setStyle(STYLE_ARTIST);
	private static final Style STYLE_CONTRIBUTOR = FONT.withHoverEvent(translated("wwizardry.badge.contributor"));
	private static final Component CONTRIBUTOR = Component.literal("\u200D").setStyle(STYLE_CONTRIBUTOR);

	private static final Map<String, Component> MAP = Map.of(
		"D", DEV,
		"A", ARTIST,
		"C", CONTRIBUTOR
	);

	@Nullable
	public static Component getBadgeFor(UUID player) {
		if (BADGES_CACHE.containsKey(player))
			return BADGES_CACHE.get(player);

		try {
			var name = makeRequest(player);

			if (!MAP.containsKey(name))
				BADGES_CACHE.put(player, null);

			var badge = MAP.get(name);
			BADGES_CACHE.put(player, badge);

			return badge;
		} catch (IOException | InterruptedException | JsonSyntaxException ignored) {}

		BADGES_CACHE.put(player, null);

        return null;
	}

	private static HoverEvent translated(String key) {
		return new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(key));
	}

	private static String makeRequest(UUID uuid) throws IOException, InterruptedException {
		var url = url(uuid);
		var request = HttpRequest.newBuilder(URI.create(url)).build();
		var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
		var json = GSON.fromJson(response, JsonObject.class);
		return json.get("badges").getAsJsonArray().get(0).getAsString();
	}

	private static String url(UUID uuid) {
		return BASE_URL + "/" + uuid.toString().replaceAll("-", "") + ".json";
	}
}
