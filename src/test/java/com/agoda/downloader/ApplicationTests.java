package com.agoda.downloader;

import com.agoda.downloader.protocols.Protocol;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.out;

public class ApplicationTests {

    @Test
    public void processors() {
        File test = new File("downloaddir");
        test.mkdir();
        System.out.print(test.getAbsoluteFile());
    }

}
