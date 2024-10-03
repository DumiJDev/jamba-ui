package io.github.dumijdev.jambaui.context.utils;

public class Types {
    @SuppressWarnings("unchecked")
    public static  <T> T convert(Object value, Class<T> type) {
        if (value == null)
            return null;

        if (int.class.equals(type) || Integer.class.equals(type)) {
            return (T) Integer.valueOf(value.toString());
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            return (T) Long.valueOf(value.toString());
        } else if (float.class.equals(type) || Float.class.equals(type)) {
            return (T) Float.valueOf(value.toString());
        } else if (double.class.equals(type) || Double.class.equals(type)) {
            return (T) Double.valueOf(value.toString());
        } else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            return (T) Boolean.valueOf(value.toString());
        } else if (String.class.equals(type)) {
            return (T) value;
        }

        return null;
    }
}
