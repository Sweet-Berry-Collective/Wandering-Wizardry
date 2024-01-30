package dev.sweetberry.wwizardry.api.resource;

import net.minecraft.resource.ResourceIoSupplier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.ResourcePack;
import net.minecraft.resource.pack.metadata.ResourceMetadataSectionReader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapBackedPack implements ResourcePack {
	private final Map<Identifier, byte[]> assets = new ConcurrentHashMap<>();
	private final Map<Identifier, byte[]> data = new ConcurrentHashMap<>();
	private final Map<String, byte[]> root = new ConcurrentHashMap<>();

	public void put(ResourceType type, Identifier id, byte[] bytes) {
		getForType(type).put(id, bytes);
	}

	public void put(String path, byte[] bytes) {
		root.put(path, bytes);
	}

	public void put(ResourceType type, Identifier id, String text) {
		put(type, id, text.getBytes(StandardCharsets.UTF_8));
	}

	public void put(String path, String text) {
		put(path, text.getBytes(StandardCharsets.UTF_8));
	}

	@Nullable
	@Override
	public ResourceIoSupplier<InputStream> openRoot(String... path) {
		return open(root, String.join("/", path));
	}

	@Nullable
	@Override
	public ResourceIoSupplier<InputStream> open(ResourceType type, Identifier id) {
		return open(getForType(type), id);
	}

	@Override
	public void listResources(ResourceType type, String namespace, String startingPath, ResourceConsumer consumer) {
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
	public Set<String> getNamespaces(ResourceType type) {
		return getForType(type)
			.keySet()
			.stream()
			.map(Identifier::getNamespace)
			.collect(Collectors.toUnmodifiableSet());
	}

	@Nullable
	@Override
	public <T> T parseMetadata(ResourceMetadataSectionReader<T> metaReader) throws IOException {
		return null;
	}

	@Override
	public String getName() {
		return "Wandering Wizardry Resources";
	}

	@Override
	public void close() {}

	public void clear() {
		root.clear();
		assets.clear();
		data.clear();
	}

	public void clear(ResourceType type) {
		getForType(type).clear();
	}

	private Map<Identifier, byte[]> getForType(ResourceType type) {
		if (type == ResourceType.CLIENT_RESOURCES)
			return assets;
		return data;
	}

	private <T> ResourceIoSupplier<InputStream> open(Map<T, byte[]> map, T path) {
		var bytes = map.get(path);
		if (bytes == null)
			return null;
		return () -> new ByteArrayInputStream(bytes);
	}
}
