package com.marakana.mydex.dao;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.marakana.mydex.domain.Contact;

public class XmlContactStreamTranscoder implements ContactStreamTranscoder {

	public static final ContactStreamTranscoder DEFAULT_INSTANCE = new XmlContactStreamTranscoder();

	@Override
	public void write(Contact contact, OutputStream out)
			throws AddressBookException {
		try {
			JAXB.marshal(new ContactRepresentation(contact), out);
		} catch (DataBindingException e) {
			throw new AddressBookException("Failed to write [" + contact
					+ "] to stream as xml", e);
		}
	}

	@Override
	public Contact read(InputStream in) throws AddressBookException {
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ContactRepresentation.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ContactRepresentation contactRepresentation = (ContactRepresentation) unmarshaller
					.unmarshal(in);
			return contactRepresentation.asContact();
		} catch (JAXBException e) {
			throw new AddressBookException(
					"Failed to read contact from stream as xml", e);
		}
	}
}