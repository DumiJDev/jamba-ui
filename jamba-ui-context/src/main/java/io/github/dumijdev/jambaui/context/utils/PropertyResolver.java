package io.github.dumijdev.jambaui.context.utils;

public class PropertyResolver {
    public static <T> T resolve(String propertyName, Class<?> clazz) {
        if (propertyName == null || propertyName.isEmpty()) {
            return null;
        } else if (propertyName.matches("[$][{][A-Za-z0-9.-_]+[}]")) {
            var key = propertyName
                    .replaceAll("\\$", "")
                    .replaceAll("\\{", "")
                    .replaceAll("}", "");

            return (T) Types.convert(ConfigurationManager.getInstance().getProperty(key), clazz);
        } else {
            if (String.class.equals(clazz)) {
                return (T) propertyName;
            } else {
                return null;
            }
        }
    }

    public static String resolve(String propertyName) {
        return resolve(propertyName, String.class);
    }
}
