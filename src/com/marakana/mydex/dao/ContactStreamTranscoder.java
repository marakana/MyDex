package com.marakana.mydex.dao;

import java.io.InputStream;
import java.io.OutputStream;

import com.marakana.mydex.domain.Contact;

public interface ContactStreamTranscoder {
	public void write(Contact contact, OutputStream out)
			throws AddressBookException;

	public Contact read(InputStream in) throws AddressBookException;
}
