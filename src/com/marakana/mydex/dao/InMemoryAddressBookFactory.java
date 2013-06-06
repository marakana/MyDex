package com.marakana.mydex.dao;

public class InMemoryAddressBookFactory implements AddressBookFactory {

	@Override
	public AddressBook getAddressBook() throws AddressBookException {
		return new InMemoryAddressBook();
	}

}