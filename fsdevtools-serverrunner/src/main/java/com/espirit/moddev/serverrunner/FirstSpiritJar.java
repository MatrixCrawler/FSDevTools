package com.espirit.moddev.serverrunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Optional;

/**
 * @author e-Spirit AG
 */
public enum FirstSpiritJar {
    SERVER("de.espirit.firstspirit.server.CMSServer"),
    ISOLATED_SERVER("de.espirit.common.bootstrap.Bootstrap"),
    WRAPPER("org.tanukisoftware.wrapper.WrapperManager");

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstSpiritJar.class);
    private final String classname;

    // TODO DEVEX-159 "className" ist not a sensible name
    FirstSpiritJar(final String classname) {
        this.classname = classname;
    }

    public Optional<File> tryFindOnClasspath() {
        final String jarName = name().toLowerCase();
        try {
            File jarFile = getJarFileForClass(classname);
            LOGGER.info("FirstSpirit {} jar found in classpath: {}", jarName, jarFile.getPath());
            return Optional.of(jarFile);
        } catch (ClassNotFoundException e) {
            LOGGER.info("FirstSpirit " + jarName + " class not found! Is the " + jarName + " jar file on the classpath?", e);
        } catch (URISyntaxException e) {
            LOGGER.info("FirstSpirit " + jarName + " jar location couldn't be translated to an URI!", e);
        }
        return Optional.empty();
    }

    /**
     * Tries to get the jar file of the class with the given fullQualifiedClassName string.
     * @param fullQualifiedClassName the name of the class the jar file should be located for
     * @return the corresponding jar file
     * @throws ClassNotFoundException if the given class couldn't be found
     * @throws URISyntaxException if the location of the class can not be converted to an URI
     */
    private static File getJarFileForClass(String fullQualifiedClassName) throws ClassNotFoundException, URISyntaxException {
        Class<?> serverClass = Class.forName(fullQualifiedClassName);
        CodeSource serverCodeSource = serverClass.getProtectionDomain().getCodeSource();
        return new File(serverCodeSource.getLocation().toURI().getPath());
    }

    public String getClassname() {
        return classname;
    }
}
