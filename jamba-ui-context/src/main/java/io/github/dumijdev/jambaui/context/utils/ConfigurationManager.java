package io.github.dumijdev.jambaui.context.utils;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private final Map<String, String> props = new HashMap<>();

    private ConfigurationManager() {
        loadProperties("application.properties");
        loadProperties("application.yaml");
        loadProperties("application.yml");
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }

        return instance;
    }

    public void loadProperties(String fileName) {
        System.out.println("Loading properties from " + fileName);
        if (props.isEmpty()) {
            if (fileName.endsWith(".properties")) {
                var properties = new PropertyLoader(fileName);

                for (var prop : properties.getProperties()) {
                    props.put(prop.getKey(), prop.getValue().toString());
                }
            } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
                var yamlLoader = new YamlLoader(fileName);
                for (var prop : yamlLoader.getProperties()) {
                    props.put(prop.getKey(), prop.getValue().toString());
                }
            }
        }
    }

    public String getProperty(String key) {
        return props.get(key);
    }

    public <T> T getProperty(String key, Class<T> clazz) {
        var value = getProperty(key);

        return Types.convert(value, clazz);
    }
}
