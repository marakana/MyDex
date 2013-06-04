package com.marakana.mydex.domain;

public class Contact {
	private String firstName;

	private String lastName;

	private final String email;

	private String phone;

	public Contact(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	// TODO: implement boolean equals(Object)

	// TODO: implement int hashCode()

	// TODO: implement String toString()
}