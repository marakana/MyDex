package com.marakana.mydex.dao;

import javax.xml.bind.annotation.XmlRootElement;

import com.marakana.mydex.domain.Contact;

@XmlRootElement(name = "contact")
public class AdaptedContact {
	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	public AdaptedContact(Contact contact) {
		this.setEmail(contact.getEmail());
		this.setFirstName(contact.getFirstName());
		this.setLastName(contact.getLastName());
		this.setPhone(contact.getPhone());
	}

	public AdaptedContact() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Contact asContact() {
		Contact contact = new Contact(this.getEmail());
		contact.setFirstName(this.getFirstName());
		contact.setLastName(this.getLastName());
		contact.setPhone(this.getPhone());
		return contact;
	}
}
