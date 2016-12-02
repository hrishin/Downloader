package com.agoda.downloader;

import com.agoda.downloader.protocols.Protocol;
import org.junit.Test;

public class ApplicationTests {

    @Test
    public void processors() {
        System.out.print(Runtime.getRuntime().availableProcessors());
        System.out.print(Protocol.isSupported("http"));
    }

}
