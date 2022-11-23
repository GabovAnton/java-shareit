package ru.practicum.shareit.utils;

import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

public class ClassProperties {

    public static Map<String, Object> getClassProperties(Object obj) {
        Map<String, Object> properties = new HashMap<>();
        ReflectionUtils.doWithFields(obj.getClass(), field -> {

            field.setAccessible(true);

            if (field.get(obj) != null) {
                properties.put(field.getName(), field.get(obj));
            }
        });
        return properties;
    }
}
