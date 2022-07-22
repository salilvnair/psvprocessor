package com.salilvnair.psvprocessor.test;

import com.salilvnair.psvprocessor.service.PsvFileReader;
import com.salilvnair.psvprocessor.service.PsvFileReaderImpl;
import com.salilvnair.psvprocessor.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DummyTest {

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        PsvFileReader psvFileReader = new PsvFileReaderImpl();
        StopWatch.start();
        List<DummyClass> dummyClasses = psvFileReader.read(new File("/Users/salilvnair/Workspace/experiments/psvprocessor/src/main/resources/test_pipe.txt"), DummyClass.class);
        System.out.println("read "+dummyClasses.size()+" records in "+StopWatch.elapsed(TimeUnit.SECONDS)+ " seconds.");
    }

}
