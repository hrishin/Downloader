package com.agoda.downloader;

import com.agoda.downloader.domain.UFR;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ApplicationTests {

	@Test
	public void prepareFilesList() {
		String sources = "http://my.file.com/file, ftp://other.file.com/other, sftp://and.also.this/ending";

		UFRBuilder ufrBuilder = new UFRBuilder();
		List<UFR> filesList = ufrBuilder.urfFromCSV(sources);
		Assert.assertEquals(3, filesList.size());
	}

}
