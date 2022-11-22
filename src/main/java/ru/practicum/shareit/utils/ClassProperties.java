package ru.practicum.shareit.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassProperties {

    public static Map<String, Object> getClassProperties(Object obj, boolean setAccessible) {
        Map<String, Object> properties = new HashMap<>();
        ReflectionUtils.doWithFields(obj.getClass(), field -> {

            if (setAccessible) {
                field.setAccessible(true);
            }
          /*  Class<?> type = field.getType();
            Class<? extends Field> aClass = field.getClass();*/
            properties.put(field.getName(), field.get(obj));
        });
        return properties;
    }
}
