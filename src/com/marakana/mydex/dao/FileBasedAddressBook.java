package com.marakana.mydex.dao;

import com.marakana.mydex.domain.Contact;

//TODO: implement!
public class FileBasedAddressBook implements AddressBook {

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		throw new UnsupportedOperationException("Implement me!");
	}

	@Override
	public Contact[] getAll() throws AddressBookException {
		throw new UnsupportedOperationException("Implement me!");
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		throw new UnsupportedOperationException("Implement me!");
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		throw new UnsupportedOperationException("Implement me!");
	}

	@Override
	public void close() throws AddressBookException {
		throw new UnsupportedOperationException("Implement me!");
	}
}