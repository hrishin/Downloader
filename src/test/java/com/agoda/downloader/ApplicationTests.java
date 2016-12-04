package com.agoda.downloader;

import org.junit.Test;

import java.io.File;

public class ApplicationTests {

    @Test
    public void processors() {
        File test = new File("downloaddir");
        test.mkdir();
        System.out.print(test.getAbsoluteFile());
    }

}
