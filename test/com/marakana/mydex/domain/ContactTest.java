package com.marakana.mydex.domain;

import static junit.framework.Assert.*;

import org.junit.Test;

public class ContactTest {

	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullEmail() {
		new Contact(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithEmptyEmail() {
		new Contact("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithInvalidEmail() {
		new Contact("invalid email address");
	}

	@Test
	public void testConstructorWithRegularEmail() {
		String email = "valid@email.address.com";
		Contact contact = new Contact(email);
		assertEquals(email, contact.getEmail());
	}

	@Test
	public void testEqualsWithNull() {
		assertFalse(new Contact("valid@email.address.com").equals(null));
	}

	@Test
	public void testEqualsYes() {
		assertEquals(new Contact("same@email.address.com"), new Contact(
				"same@email.address.com"));
	}

	@Test
	public void testEqualsNot() {
		assertFalse(new Contact("one@email.address.com").equals(new Contact(
				"two@email.address.com")));
	}
}
