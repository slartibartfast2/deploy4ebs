package org.mastodon.ebs.deployment.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Erdem.Akdogan on 13.10.2017.
 */
public class PropertyUtils {
    private static Map<String, Map<String, String>> cache = new HashMap<>();
    private static final Logger logger = Logger.getLogger(PropertyUtils.class);

    public static String getValue(String resourceName, String key) {
        Map<String, String> resourceMap = cache.get(resourceName);
        if (resourceMap == null) {
            resourceMap = new HashMap<>();
            cache.put(resourceName, resourceMap);
        }

        String value = resourceMap.get(key);
        if (StringUtils.isEmpty(value)) {
            try {
                Properties properties = loadProperties(resourceName);

                value = properties.getProperty(key);
                logger.trace("found in file. " + key + ": " + value);
                resourceMap.put(key, value);
                cache.put(resourceName, resourceMap);

                return value;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                throw new RuntimeException(key + " property could not be found.");
            }
        } else {
            logger.trace("found in cache. " + key + ": " + value);
            return value;
        }
    }

    public static Map<String, String> getValues(String resourceName) {
        Map<String, String> resourceMap = cache.get(resourceName);
        if (resourceMap == null) {
            resourceMap = new HashMap<>();
            try {

                Properties properties = loadProperties(resourceName);

                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    resourceMap.put(key, value);
                }

                cache.put(resourceName, resourceMap);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                throw new RuntimeException("property file could not be read.");
            }
        }

        return resourceMap;
    }

    public static boolean exists(String resourceName) {
        try {
            loadProperties(resourceName);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return false;
        }
    }

    private static Properties loadProperties(String resourceName) throws Exception {
        Properties properties = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            properties.load(resourceStream);
        }

        return properties;
    }
}
