package com.salilvnair.psvprocessor.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.salilvnair.psvprocessor.service.PsvFileReader;
import com.salilvnair.psvprocessor.service.PsvFileReaderImpl;

public class DummyTest {

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        PsvFileReader psvFileReader = new PsvFileReaderImpl();
        File file = new File("/Users/salilvnair/Workspace/experiments/psvprocessor/src/main/resources/10million_row_dummy_pipe_data.txt");
        int chunkSize = 1000000;
        int chunkMaxValue = 100;
        for (int i = 0; i < chunkMaxValue; i++) {
            long from = 1 + (chunkSize*i);
            long to  = from + (chunkSize-1);
            List<DummyClass> dummyClasses = psvFileReader.read(file, DummyClass.class, from, to);

            //do something with data
            processDummyClasses(dummyClasses);

            if(chunkSize != dummyClasses.size()) {
                dummyClasses.clear();
                break;
            }
        }
    }

    private static void processDummyClasses(List<DummyClass> dummyClasses) {
        //do something with data
    }

}
