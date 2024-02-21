package dev.sweetberry.wwizardry.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHelper {
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .setLenient()
            .create();

    public static <T> T loadGlobalConfig(T orElse, String file) {
        var path = Path.of("config", file);
        try {
			var str = new String(Files.readAllBytes(path));
			var value = GSON.fromJson(str, (Class<T>) orElse.getClass());
			saveGlobalConfig(value, file);
            return value;
        } catch (IOException e) {
            saveGlobalConfig(orElse, file);
            return orElse;
        }
    }

    public static <T> void saveGlobalConfig(T object, String file) {
        var path = Path.of("config", file).toFile();
        try {
            if (!path.exists())
                path.createNewFile();
            var json = GSON.toJson(object);
			var writer = new FileWriter(path);
			writer.write(json);
			writer.close();
        } catch (IOException ignored) {}
    }
}
