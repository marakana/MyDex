package com.marakana.mydex.domain;

public class Contact {
	private String firstName;

	private String lastName;

	private final String email;

	private String phone;

	public Contact(String email) {
		if (email == null) {
			throw new NullPointerException("Email must not be null");
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.email == null) ? 0 : this.email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (this.email == null) {
			if (other.email != null)
				return false;
		} else if (!this.email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder(35);
		if (this.getFirstName() != null) {
			out.append(this.getFirstName()).append(' ');
		}
		if (this.getLastName() != null) {
			out.append(this.getLastName()).append(' ');
		}
		out.append('<').append(this.getEmail()).append('>');
		if (this.getPhone() != null) {
			out.append(' ').append(this.getPhone());
		}
		return out.toString();
	}
}