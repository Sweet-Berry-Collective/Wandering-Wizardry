package dev.sweetberry.wwizardry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Badges {
	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

	private static final Gson GSON = new Gson();

	private static final Map<UUID, Text> BADGES_CACHE = new HashMap<>();

	private static final String BASE_URL = "https://badges.wwizardry.sweetberry.dev";

	private static final Style FONT = Style.EMPTY.withFont(WanderingMod.id("badges"));
	private static final Style STYLE_DEV = FONT.withHoverEvent(translated("wwizardry.badge.developer"));
	private static final Text DEV = Text.literal("\u2000").setStyle(STYLE_DEV);
	private static final Style STYLE_ARTIST = FONT.withHoverEvent(translated("wwizardry.badge.artist"));
	private static final Text ARTIST = Text.literal("\u2001").setStyle(STYLE_ARTIST);
	private static final Style STYLE_CONTRIBUTOR = FONT.withHoverEvent(translated("wwizardry.badge.contributor"));
	private static final Text CONTRIBUTOR = Text.literal("\u2002").setStyle(STYLE_CONTRIBUTOR);

	private static final Map<String, Text> MAP = Map.of(
		"D", DEV,
		"A", ARTIST,
		"C", CONTRIBUTOR
	);

	@Nullable
	public static Text getBadgeFor(UUID player) {
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
		return new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(key));
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
