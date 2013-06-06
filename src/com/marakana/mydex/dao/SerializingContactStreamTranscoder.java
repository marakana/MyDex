package com.marakana.mydex.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.marakana.mydex.domain.Contact;

public class SerializingContactStreamTranscoder implements
		ContactStreamTranscoder {

	public static final ContactStreamTranscoder DEFAULT_INSTNACE = new SerializingContactStreamTranscoder();

	@Override
	public void write(Contact contact, OutputStream out)
			throws AddressBookException {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(contact);
			oos.flush();
		} catch (IOException e) {
			throw new AddressBookException("Failed to write [" + contact
					+ "] to stream", e);
		}
	}

	@Override
	public Contact read(InputStream in) throws AddressBookException {
		try {
			ObjectInputStream ois = new ObjectInputStream(in);
			return (Contact) ois.readObject();
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			throw new AddressBookException("Failed to read contact from stream", e);
		}
	}
}