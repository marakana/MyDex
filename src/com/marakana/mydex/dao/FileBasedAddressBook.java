package com.marakana.mydex.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.DatatypeConverter;

import com.marakana.mydex.domain.Contact;

public class FileBasedAddressBook implements AddressBook {

	private static final String EXTENSION = ".contact";

	private static final FileFilter FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isFile() && pathname.getName().endsWith(EXTENSION);
		}
	};

	private final File dir;

	public FileBasedAddressBook(File dir) throws AddressBookException {
		if (dir == null) {
			throw new NullPointerException("Dir must not be null");
		} else if (!dir.exists() && !dir.mkdirs()) {
			throw new AddressBookException("No such directory: "
					+ dir.getAbsolutePath());
		}
		this.dir = dir;
	}

	private File getFileForEmail(String email) {
		if (email == null) {
			throw new NullPointerException("Email must not be null");
		} else if (email.isEmpty()) {
			throw new IllegalArgumentException("Email must not be empty");
		} else {
			return new File(this.dir, DatatypeConverter.printHexBinary(email
					.getBytes()) + EXTENSION);
		}
	}

	private Contact getContactForFile(File file) throws AddressBookException {
		try {
			try (InputStream in = new FileInputStream(file);
					ObjectInputStream actualIn = new ObjectInputStream(
							new GZIPInputStream(in));) {
				return (Contact) actualIn.readObject();
			}
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			throw new AddressBookException("Failed to get contact from file ["
					+ file.getAbsolutePath() + "]", e);
		}
	}

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		File file = this.getFileForEmail(email);
		if (file.exists()) {
			return this.getContactForFile(file);
		} else {
			return null;
		}
	}

	@Override
	public Collection<Contact> getAll() throws AddressBookException {
		File[] files = this.dir.listFiles(FILE_FILTER);
		List<Contact> contacts = new ArrayList<>(files.length);
		for (int i = 0; i < files.length; i++) {
			contacts.add(this.getContactForFile(files[i]));
		}
		return contacts;
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		File file = this.getFileForEmail(contact.getEmail());
		try {
			try (OutputStream out = new FileOutputStream(file);
					ObjectOutputStream actualOut = new ObjectOutputStream(
							new GZIPOutputStream(out));) {
				actualOut.writeObject(contact);
			}
		} catch (IOException e) {
			file.delete();
			throw new AddressBookException("Failed to store [" + contact + "]",
					e);
		}
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		File file = this.getFileForEmail(email);
		if (file.exists() && !file.delete()) {
			throw new AddressBookException(
					"Failed to delete contact by email [" + email + "]");
		}
	}

	@Override
	public void close() throws AddressBookException {
		// do nothing
	}
}