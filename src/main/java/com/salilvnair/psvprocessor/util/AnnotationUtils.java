package com.salilvnair.psvprocessor.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class AnnotationUtils {
    public static <T extends Annotation> Set<Field> findAnnotatedFields(Class<?> clazz, Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }
        Set<Field> annotatedFields = new LinkedHashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }
        annotatedFields.addAll(findAnnotatedFields(clazz.getSuperclass(), annotation));
        return annotatedFields;
    }
}
