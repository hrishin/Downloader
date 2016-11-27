package com.agoda.downloader;

import com.agoda.downloader.domain.FileResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ApplicationTests {

	@Test
	public void prepareFilesResources() {
		String sources = "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";

		FileResourceBuilder resourceBuilder = new FileResourceBuilder();
		List<FileResource> fileList = resourceBuilder.frFromCSV(sources);

		Assert.assertEquals(3, fileList.size());
	}

}
