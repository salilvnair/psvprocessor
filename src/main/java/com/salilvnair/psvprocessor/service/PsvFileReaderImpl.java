package com.salilvnair.psvprocessor.service;

import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

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
        return processBulkData(clazz, columnNameKeyedFieldNameMap, bulkData);
    }

    public <T> List<T> read(File file, Class<T> clazz, long from, long to) throws IOException, InstantiationException, IllegalAccessException {
        Map<String, String> columnNameKeyedFieldNameMap = columnNameKeyedFieldNameMap(clazz);
        List<String> bulkData = new ArrayList<>();
        try (LineIterator lineIterator = org.apache.commons.io.FileUtils.lineIterator(file)) {
            int lineNumber = 0;
            while (lineIterator.hasNext()) {
                if(lineNumber == 0) {
                    String line = lineIterator.nextLine();
                    if(line!=null && line.contains("|")) {
                        bulkData.add(line);
                    }
                }
                lineNumber++;
                if(from > lineNumber) {
                    lineIterator.nextLine();
                    continue;
                }
                String line = lineIterator.nextLine();
                if(line!=null && line.contains("|")) {
                    bulkData.add(line);
                }
                if(from == to) {
                    break;
                }
                from++;
            }
        }
        return processBulkData(clazz, columnNameKeyedFieldNameMap, bulkData);
    }

    private <T> List<T> processBulkData(Class<T> clazz, Map<String, String> columnNameKeyedFieldNameMap, List<String> bulkData) throws InstantiationException, IllegalAccessException {
        Map<String, Integer> columns = new HashMap<>();
        Map<Integer, Map<Integer, String>> valueRows = new HashMap<>();

        for (int i = 0; i < bulkData.size(); i++) {
            Map<Integer, String> values = new HashMap<>();
            String line = bulkData.get(i);
            String[] data = line.split("\\|");
            for (int v = 0; v < data.length; v++) {
                if(i == 0) {
                    //column line data
                    columns.put(data[v], v);
                }
                else {
                    //value line data
                    values.put(v, data[v]);
                }
            }
            if(i > 0) {
                //value rows
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
