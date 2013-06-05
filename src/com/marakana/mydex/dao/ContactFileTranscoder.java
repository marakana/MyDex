package com.marakana.mydex.dao;

import java.io.File;

import com.marakana.mydex.domain.Contact;

public interface ContactFileTranscoder {
	public void write(Contact contact, File file)
			throws AddressBookException;

	public Contact read(File file) throws AddressBookException;
}
