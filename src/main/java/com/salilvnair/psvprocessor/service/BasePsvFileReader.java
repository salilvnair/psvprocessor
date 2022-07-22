package com.salilvnair.psvprocessor.service;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.salilvnair.psvprocessor.annotation.PsvColumn;
import com.salilvnair.psvprocessor.util.AnnotationUtils;

public abstract class BasePsvFileReader implements PsvFileReader {

    public Map<String, String> columnNameKeyedFieldNameMap(Class<?> clazz) {
        Set<Field> psvColumnFields = AnnotationUtils.findAnnotatedFields(clazz, PsvColumn.class);
        return psvColumnFields.stream().collect(Collectors.toMap(p -> p.getAnnotation(PsvColumn.class).value(), Field::getName));
    }

}
