package com.marakana.mydex.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import com.marakana.mydex.CombinedComparator;
import com.marakana.mydex.Util;

public class Contact implements Serializable, Comparable<Contact> {

	private static final long serialVersionUID = -8588421653547774251L;

	public static final Comparator<Contact> FIRST_NAME_COMPARATOR = new Comparator<Contact>() {
		@Override
		public int compare(Contact c1, Contact c2) {
			return Util.compare(c1.getFirstName(), c2.getFirstName());
		}
	};

	public static final Comparator<Contact> LAST_NAME_COMPARATOR = new Comparator<Contact>() {
		@Override
		public int compare(Contact c1, Contact c2) {
			return Util.compare(c1.getLastName(), c2.getLastName());
		}
	};

	public static final Comparator<Contact> EMAIL_COMPARATOR = new Comparator<Contact>() {
		@Override
		public int compare(Contact c1, Contact c2) {
			return c1.compareTo(c2);
		}
	};

	public static final Comparator<Contact> FIRST_NAME_LAST_NAME_EMAIL_COMPARATOR = new CombinedComparator<>(
			Arrays.asList(FIRST_NAME_COMPARATOR, LAST_NAME_COMPARATOR,
					EMAIL_COMPARATOR));

	private String firstName;

	private String lastName;

	private final String email;

	private String phone;

	public Contact(String email) {
		if (email == null) {
			throw new NullPointerException("Email must not be null");
		} else if (!email.matches("^[^@]+@[^@]+")) {
			throw new IllegalArgumentException("Invalid email address: [" + email
					+ "]");
		}
		this.email = email;
	}

	public Contact(String firstName, String lastName, String email) {
		this(email);
		this.setFirstName(firstName);
		this.setLastName(lastName);
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

	@Override
	public int compareTo(Contact that) {
		return this.getEmail().compareTo(that.getEmail());
	}
}