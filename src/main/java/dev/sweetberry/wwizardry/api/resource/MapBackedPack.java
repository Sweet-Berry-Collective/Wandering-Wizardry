package dev.sweetberry.wwizardry.api.resource;

import org.jetbrains.annotations.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;

public class MapBackedPack implements PackResources {
	private final Map<ResourceLocation, byte[]> assets = new ConcurrentHashMap<>();
	private final Map<ResourceLocation, byte[]> data = new ConcurrentHashMap<>();
	private final Map<String, byte[]> root = new ConcurrentHashMap<>();

	public void put(PackType type, ResourceLocation id, byte[] bytes) {
		getForType(type).put(id, bytes);
	}

	public void put(String path, byte[] bytes) {
		root.put(path, bytes);
	}

	public void put(PackType type, ResourceLocation id, String text) {
		put(type, id, text.getBytes(StandardCharsets.UTF_8));
	}

	public void put(String path, String text) {
		put(path, text.getBytes(StandardCharsets.UTF_8));
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getRootResource(String... path) {
		return open(root, String.join("/", path));
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
		return open(getForType(type), id);
	}

	@Override
	public void listResources(PackType type, String namespace, String startingPath, ResourceOutput consumer) {
		getForType(type)
			.entrySet()
			.stream()
			.filter(entry ->
				entry.getKey().getNamespace().equals(namespace)
				&& entry.getKey().getPath().startsWith(startingPath)
			).forEach(entry -> {
				var bytes = entry.getValue();
				if (bytes == null)
					return;
                consumer.accept(entry.getKey(), () -> new ByteArrayInputStream(bytes));
            });
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		return getForType(type)
			.keySet()
			.stream()
			.map(ResourceLocation::getNamespace)
			.collect(Collectors.toUnmodifiableSet());
	}

	@Nullable
	@Override
	public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) throws IOException {
		return null;
	}

	@Override
	public String packId() {
		return "Wandering Wizardry Resources";
	}

	@Override
	public void close() {}

	public void clear() {
		root.clear();
		assets.clear();
		data.clear();
	}

	public void clear(PackType type) {
		getForType(type).clear();
	}

	private Map<ResourceLocation, byte[]> getForType(PackType type) {
		if (type == PackType.CLIENT_RESOURCES)
			return assets;
		return data;
	}

	private <T> IoSupplier<InputStream> open(Map<T, byte[]> map, T path) {
		var bytes = map.get(path);
		if (bytes == null)
			return null;
		return () -> new ByteArrayInputStream(bytes);
	}
}
