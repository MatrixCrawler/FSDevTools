package com.espirit.moddev.serverrunner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author e-Spirit AG
 */
public enum ServerType {

    LEGACY(() -> {
        Map<FirstSpiritJar, Path> jars = new EnumMap<>(FirstSpiritJar.class);
        jars.put(FirstSpiritJar.SERVER, Paths.get("server", "lib", "fs-server.jar"));
        jars.put(FirstSpiritJar.WRAPPER, Paths.get("server", "lib", "wrapper.jar"));
        return jars;
    }),

    ISOLATED(() -> {
        Map<FirstSpiritJar, Path> jars = new EnumMap<>(FirstSpiritJar.class);
        jars.put(FirstSpiritJar.ISOLATED_SERVER, Paths.get("server", "lib-isolated", "fs-isolated-server.jar"));
        jars.put(FirstSpiritJar.WRAPPER, Paths.get("server", "lib-isolated", "wrapper.jar"));
        return jars;
    });

    private final Map<FirstSpiritJar, Path> startUpJars;

    ServerType(final Supplier<Map<FirstSpiritJar, Path>> startUpJars) {
        this.startUpJars = startUpJars.get();
    }

    public Map<FirstSpiritJar, File> resolveJars(final Path serverInstallationDir) {
        return startUpJars.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> serverInstallationDir.resolve(entry.getValue()).toFile()));
    }
}
