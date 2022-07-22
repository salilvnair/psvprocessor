package com.salilvnair.psvprocessor.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PsvFileReader {
    <T> List<T> read(File file, Class<T> clazz) throws IOException, InstantiationException, IllegalAccessException;
}
