package com.salilvnair.psvprocessor.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.salilvnair.psvprocessor.service.PsvFileReader;
import com.salilvnair.psvprocessor.service.PsvFileReaderImpl;
import com.salilvnair.psvprocessor.util.StopWatch;

public class DummyTest {

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        PsvFileReader psvFileReader = new PsvFileReaderImpl();

        File file = new File("/Users/salilvnair/Workspace/experiments/psvprocessor/src/main/resources/test_pipe.txt");
        long totalTime = 0;
        int chunkSize = 1000000;
        int chunkMaxValue = 100;
        for (int i = 0; i < chunkMaxValue; i++) {
            long from = 1 + (chunkSize*i);
            long to  = from + (chunkSize-1);
            System.out.println("from:"+from);
            System.out.println("to:"+to);
            StopWatch.start();
            List<DummyClass> dummyClasses = psvFileReader.read(file, DummyClass.class, from, to);
            long endTime = StopWatch.elapsed(TimeUnit.SECONDS);
            totalTime = totalTime + endTime;
            System.out.println("read "+dummyClasses.size()+" records in "+endTime+ " seconds.");
            if(chunkSize != dummyClasses.size()) {
                System.out.println("reading completed in "+totalTime+" seconds.Going to exit the process...");
                dummyClasses.clear();
                break;
            }
            dummyClasses.clear();
            System.out.println();
        }
    }

}
