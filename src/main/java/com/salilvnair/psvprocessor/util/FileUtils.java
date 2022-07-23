package com.salilvnair.psvprocessor.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public enum SizeUnit {
        B(1),
        KB(1024),
        MB(1024*1024),
        GB(1024*1024*1024);
        private final long byteSize;
        SizeUnit(long byteSize) {
            this.byteSize = byteSize;
        }

        public long byteSize() {
             return byteSize;
        }
    }

    public static Long fileSize(String fileName, SizeUnit sizeUnit) {

        Path path = Paths.get(fileName);

        try {
            if(sizeUnit == null) {
                sizeUnit = SizeUnit.B;
            }
            long bytes = Files.size(path);
            return bytes/ sizeUnit.byteSize();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;

    }

    public static Long fileSize(File file, SizeUnit sizeUnit) {
        return fileSize(file.getAbsolutePath(), sizeUnit);
    }

}
