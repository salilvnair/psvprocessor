package com.salilvnair.psvprocessor.service;

import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PsvFileReaderImpl extends BasePsvFileReader {
    @Override
    public <T> List<T> read(File file, Class<T> clazz) throws IOException, InstantiationException, IllegalAccessException {
        Map<String, String> columnNameKeyedFieldNameMap = columnNameKeyedFieldNameMap(clazz);
        List<String> bulkData = new ArrayList<>();
        try (LineIterator lineIterator = org.apache.commons.io.FileUtils.lineIterator(file)) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if(line!=null && line.contains("|")) {
                    bulkData.add(line);
                }
            }
        }
        Map<String, Integer> columns = new HashMap<>();
        Map<Integer, Map<Integer, String>> valueRows = new HashMap<>();

        for (int i = 0; i < bulkData.size(); i++) {
            Map<Integer, String> values = new HashMap<>();
            String line = bulkData.get(i);
            String[] data = line.split("\\|");
            if(i==0) {
                //column line
                for (int c = 0; c < data.length; c++) {
                    columns.put(data[c], c);
                }
            }
            else {
                //value line
                for (int v = 0; v < data.length; v++) {
                    values.put(v, data[v]);
                }
                valueRows.put(i, values);
            }
        }

        List<T> data = new ArrayList<>();
        for(Integer valueIndex : valueRows.keySet()) {
            T t = clazz.newInstance();
            for(String key : columns.keySet()) {
                String fieldName = columnNameKeyedFieldNameMap.get(key);
                if(fieldName == null) {
                    continue;
                }
                Field field = org.springframework.data.util.ReflectionUtils.findRequiredField(clazz, fieldName);
                Map<Integer, String> values = valueRows.get(valueIndex);
                String fieldValue = values.get(columns.get(key));
                org.springframework.data.util.ReflectionUtils.setField(field, t, fieldValue);
            }
            data.add(t);
        }

        return data;
    }
}
