package com.marakana.mydex.dao;

import java.io.File;
import java.io.FileFilter;

import javax.xml.bind.DatatypeConverter;

public class SimpleContactFileResolver implements ContactFileResolver {
	public static final String DEFAULT_EXTENSION = ".contact";
	public static final ContactFileResolver DEFAULT_INSTANCE = new SimpleContactFileResolver();
	private final String extension;
	private final FileFilter fileFilter;

	public SimpleContactFileResolver() {
		this(DEFAULT_EXTENSION);
	}

	public SimpleContactFileResolver(String extension) {
		this.extension = extension;
		this.fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile()
						&& pathname.getName().endsWith(
								SimpleContactFileResolver.this.extension);
			}
		};
	}

	@Override
	public File resolve(File dir, String email) {
		return new File(dir, DatatypeConverter.printHexBinary(email.getBytes())
				+ this.extension);
	}

	@Override
	public File[] getAll(File dir) {
		return dir.listFiles(this.fileFilter);
	}

}