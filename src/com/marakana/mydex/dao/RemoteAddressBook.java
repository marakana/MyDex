package com.marakana.mydex.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.marakana.mydex.Util;
import com.marakana.mydex.domain.Contact;

//TODO: known issue, does not support null/empty first name, last name, and phone
public class RemoteAddressBook implements AddressBook {
	private static final String EXPECTED_PROMPT = "address-book> ";

	private static final String OK_RESPONSE = "OK";

	private static final String ERROR_RESPONSE = "ERROR";

	private final Socket socket;

	private final PrintStream out;

	private final BufferedReader in;

	public RemoteAddressBook(InetSocketAddress inetSocketAddress)
			throws AddressBookException {
		try {
			this.socket = new Socket(inetSocketAddress.getHostName(),
					inetSocketAddress.getPort());
			this.out = new PrintStream(socket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			throw new AddressBookException(
					"Failed to create socket to remote client", e);
		}
	}

	@Override
	public Contact getByEmail(String email) throws AddressBookException {
		List<String> response = this.request("get %s\n",
				Util.notNull(email, "Email"));
		return response.isEmpty() ? null : Contact.parse(response.get(0));
	}

	@Override
	public List<Contact> getAll() throws AddressBookException {
		List<String> response = this.request("list\n");
		List<Contact> contacts = new ArrayList<Contact>(response.size());
		for (String line : response) {
			contacts.add(Contact.parse(line));
		}
		// Collections.sort(contacts);
		return contacts;
	}

	@Override
	public void store(Contact contact) throws AddressBookException {
		this.request("store %s %s %s %s\n", contact.getFirstName(),
				contact.getLastName(), contact.getEmail(), contact.getPhone());
	}

	@Override
	public void deleteByEmail(String email) throws AddressBookException {
		this.request("delete %s\n", Util.notNull(email, "Email"));
	}

	private List<String> request(String request, Object... args)
			throws AddressBookException {
		try {
			this.waitForPrompt();
			this.out.printf(request, args);
			List<String> response = new ArrayList<String>();
			while (true) {
				String line = this.in.readLine();
				if (line == null) {
					throw new AddressBookException(
							"Unexpected end of input on submitting request [" + request
									+ "]: " + line);
				} else if (line.startsWith(OK_RESPONSE)) {
					return response;
				} else if (line.startsWith(ERROR_RESPONSE)) {
					throw new AddressBookException(
							"Encountered an error while submitting request [" + request
									+ "]: " + line);
				} else {
					response.add(line);
				}
			}
		} catch (IOException e) {
			throw new AddressBookException("Failed to submit request [" + request
					+ "]", e);
		}
	}

	private void waitForPrompt() throws AddressBookException, IOException {
		for (int i = 0; i < EXPECTED_PROMPT.length(); i++) {
			int ch = this.in.read();
			char expected = EXPECTED_PROMPT.charAt(i);
			if (ch != expected) {
				throw new AddressBookException("Expecting prompt [" + EXPECTED_PROMPT
						+ "], but instead of [" + expected + "] we got [" + ch + "]");
			}
		}
	}

	@Override
	public void close() throws AddressBookException {
		try {
			this.socket.close();
		} catch (IOException e) {
			throw new AddressBookException("Failed to close", e);
		}
	}
}