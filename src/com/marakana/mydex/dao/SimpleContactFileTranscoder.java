package com.marakana.mydex.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.marakana.mydex.domain.Contact;

public class SimpleContactFileTranscoder implements ContactFileTranscoder {

	private final ContactStreamTranscoder contactStreamTranscoder;

	public SimpleContactFileTranscoder(
			ContactStreamTranscoder contactStreamTranscoder) {
		this.contactStreamTranscoder = contactStreamTranscoder;
	}

	@Override
	public void write(Contact contact, File file) throws AddressBookException {
		try (FileOutputStream out = new FileOutputStream(file)) {
			this.contactStreamTranscoder.write(contact, out);
		} catch (IOException | AddressBookException e) {
			file.delete();
			throw new AddressBookException("Failed to save [" + contact + "] to ["
					+ file.getAbsolutePath() + "]", e);
		}
	}

	@Override
	public Contact read(File file) throws AddressBookException {
		if (file.exists()) {
			try (FileInputStream in = new FileInputStream(file)) {
				return this.contactStreamTranscoder.read(in);
			} catch (FileNotFoundException e) {
				// fall through
			} catch (IOException | AddressBookException e) {
				throw new AddressBookException("Failed to read contact from ["
						+ file.getAbsolutePath() + "]", e);
			}
		}
		return null;
	}
}
