package com.marakana.mydex.dao;

import java.io.File;

public interface ContactFileResolver {
	public File resolve(File dir, String email);

	public File[] getAll(File dir);
}
