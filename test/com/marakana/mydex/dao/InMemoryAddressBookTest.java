package com.marakana.mydex.dao;

public class InMemoryAddressBookTest extends AbstractAddressBookTest {
	@Override
	protected AddressBook buildAddressBook() {
		return new InMemoryAddressBook();
	}
}