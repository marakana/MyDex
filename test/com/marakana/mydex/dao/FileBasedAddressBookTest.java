package com.marakana.mydex.dao;

import java.io.File;

//TODO: it would be better to test contact resolvers and transcoders independently of this class
public class FileBasedAddressBookTest extends AbstractAddressBookTest {
	private File dir;

	@Override
	protected AddressBook buildAddressBook() throws AddressBookException {
		this.dir = new File(new File(System.getProperty("java.io.tmpdir")), "test-"
				+ System.currentTimeMillis());
		this.dir.mkdirs();
		return new FileBasedAddressBook(this.dir);
	}

	@Override
	protected void tearDown(AddressBook addressBook) {
		for (File file : this.dir.listFiles()) {
			file.delete();
		}
		this.dir.delete();
	}
}
