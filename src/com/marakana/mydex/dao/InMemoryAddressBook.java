package com.marakana.mydex.dao;

import static com.marakana.mydex.Util.notNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.marakana.mydex.domain.Contact;

public class InMemoryAddressBook implements AddressBook {

	private final Map<String, Contact> contacts = new HashMap<>();

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		email = notNull(email, "Email");
		return this.contacts.get(email);
	}

	@Override
	public Collection<Contact> getAll() throws AddressBookException {
		List<Contact> result = new ArrayList<>(this.contacts.values());
		Collections.sort(result, Contact.FIRST_NAME_LAST_NAME_EMAIL_COMPARATOR);
		return result;
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		this.contacts.put(contact.getEmail(), notNull(contact, "Contact"));
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		this.contacts.remove(notNull(email, "Email"));
	}

	@Override
	public void close() throws AddressBookException {

	}
}