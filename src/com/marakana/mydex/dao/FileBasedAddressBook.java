package com.marakana.mydex.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.marakana.mydex.domain.Contact;

public class FileBasedAddressBook implements AddressBook {

	private final File dir;
	private final ContactFileResolver contactFileResolver;
	private final ContactFileTranscoder contactFileTranscoder;

	public FileBasedAddressBook(File dir) throws AddressBookException {
		this(dir, SimpleContactFileResolver.DEFAULT_INSTANCE,
				new SimpleContactFileTranscoder(new CompressingContactStreamTranscoder(
						XmlContactStreamTranscoder.DEFAULT_INSTANCE)));
	}

	public FileBasedAddressBook(File dir,
			ContactFileResolver contactFileResolver,
			ContactFileTranscoder contactFileTranscoder) throws AddressBookException {
		if (dir == null) {
			throw new NullPointerException("Dir must not be null");
		} else if (!dir.exists() && !dir.mkdirs()) {
			throw new AddressBookException("No such directory: "
					+ dir.getAbsolutePath());
		} else if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Not a directory: "
					+ dir.getAbsolutePath());
		} else if (contactFileResolver == null) {
			throw new NullPointerException("ContactFileResolver must not be null");
		} else if (contactFileTranscoder == null) {
			throw new NullPointerException("ContactFileTranscoder must not be null");
		} else {
			this.dir = dir;
			this.contactFileResolver = contactFileResolver;
			this.contactFileTranscoder = contactFileTranscoder;
		}
	}

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		File file = this.contactFileResolver.resolve(dir, email);
		return this.contactFileTranscoder.read(file);
	}

	@Override
	public Collection<Contact> getAll() throws AddressBookException {
		File[] files = this.contactFileResolver.getAll(dir);
		List<Contact> contacts = new ArrayList<>(files.length);
		for (int i = 0; i < files.length; i++) {
			contacts.add(this.contactFileTranscoder.read(files[i]));
		}
		Collections.sort(contacts, Contact.FIRST_NAME_LAST_NAME_EMAIL_COMPARATOR);
		return contacts;
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		File file = this.contactFileResolver.resolve(dir, contact.getEmail());
		this.contactFileTranscoder.write(contact, file);
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		File file = this.contactFileResolver.resolve(dir, email);
		if (file.exists() && !file.delete()) {
			throw new AddressBookException("Failed to delete contact by email ["
					+ email + "] from file [" + file.getAbsolutePath() + "]");
		}
	}

	@Override
	public void close() throws AddressBookException {
		// nothing to do
	}
}