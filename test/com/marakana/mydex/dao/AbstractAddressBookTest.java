package com.marakana.mydex.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.marakana.mydex.domain.Contact;
import com.marakana.mydex.domain.ContactBuilder;

public abstract class AbstractAddressBookTest {
	private AddressBook addressBook;

	protected abstract AddressBook buildAddressBook() throws Exception;

	protected void tearDown(AddressBook addressBook) throws Exception {

	}

	protected Contact getJohnSmith() {
		return new ContactBuilder("john@smith.com").withFirstName("John")
				.withLastName("Smith").withPhone("+14156477000").build();
	}

	protected <T> void assertEqualsOrNull(T expected, T actual) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected, actual);
		}
	}

	protected void assertEqualsContacts(Contact expected, Contact actual) {
		assertEqualsOrNull(expected.getFirstName(), actual.getFirstName());
		assertEqualsOrNull(expected.getLastName(), actual.getLastName());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEqualsOrNull(expected.getPhone(), actual.getPhone());
	}

	@Before
	public void setUp() throws Exception {
		this.addressBook = buildAddressBook();
	}

	@After
	public void tearDown() throws Exception {
		this.tearDown(this.addressBook);
		this.addressBook.close();
		this.addressBook = null;
	}

	@Test
	public void testStore() throws AddressBookException {
		Contact contact = getJohnSmith();
		this.addressBook.store(contact);
		this.assertEqualsContacts(contact,
				this.addressBook.getByEmail(contact.getEmail()));
	}

	@Test(expected = NullPointerException.class)
	public void testStoreNullEmail() throws AddressBookException {
		this.addressBook.store(null);
	}

	@Test
	public void testListOfOne() throws AddressBookException {
		Contact contact = getJohnSmith();
		this.addressBook.store(contact);
		Collection<Contact> contacts = this.addressBook.getAll();
		assertEquals(1, contacts.size());
		assertEqualsContacts(contact, contacts.iterator().next());
	}

	@Test
	public void testDeleteByEmail() throws AddressBookException {
		String email = "john@smith.com";
		Contact contact = new Contact(email);
		this.addressBook.store(contact);
		this.addressBook.deleteByEmail(email);
		assertNull(this.addressBook.getByEmail(email));
		assertEquals(0, this.addressBook.getAll().size());
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteByNullEmail() throws AddressBookException {
		this.addressBook.deleteByEmail(null);
	}
}