package com.marakana.mydex.dao;

import java.util.Collection;

import javax.sql.DataSource;

import com.marakana.mydex.domain.Contact;

public class DatabaseAddressBook implements AddressBook {

	// TODO: remove this annotation
	@SuppressWarnings("unused")
	private final DataSource dataSource;

	public DatabaseAddressBook(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		throw new UnsupportedOperationException("TODO: implement me");
	}

	@Override
	public Collection<Contact> getAll() throws AddressBookException {
		throw new UnsupportedOperationException("TODO: implement me");
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		throw new UnsupportedOperationException("TODO: implement me");
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		throw new UnsupportedOperationException("TODO: implement me");
	}

	@Override
	public void close() throws AddressBookException {
		throw new UnsupportedOperationException("TODO: implement me");
	}
}
