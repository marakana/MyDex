package com.marakana.mydex.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.marakana.mydex.domain.Contact;

public class CompressingContactStreamTranscoder implements
		ContactStreamTranscoder {
	private final ContactStreamTranscoder contactStreamTranscoder;

	public CompressingContactStreamTranscoder(
			ContactStreamTranscoder contactStreamTranscoder) {
		this.contactStreamTranscoder = contactStreamTranscoder;
	}

	@Override
	public void write(Contact contact, OutputStream out)
			throws AddressBookException {
		try {
			GZIPOutputStream gout = new GZIPOutputStream(out);
			this.contactStreamTranscoder.write(contact, gout);
			gout.flush();
			gout.finish();
		} catch (IOException e) {
			throw new AddressBookException("Failed to write [" + contact
					+ "] to compressed stream", e);
		}

	}

	@Override
	public Contact read(InputStream in) throws AddressBookException {
		try {
			GZIPInputStream gin = new GZIPInputStream(in);
			return this.contactStreamTranscoder.read(gin);
		} catch (IOException e) {
			throw new AddressBookException(
					"Failed to read contact from compressed stream", e);
		}
	}
}