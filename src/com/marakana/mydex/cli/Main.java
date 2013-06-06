package com.marakana.mydex.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.marakana.mydex.dao.AddressBook;
import com.marakana.mydex.dao.CompressingContactStreamTranscoder;
import com.marakana.mydex.dao.ContactFileResolver;
import com.marakana.mydex.dao.ContactFileTranscoder;
import com.marakana.mydex.dao.ContactStreamTranscoder;
import com.marakana.mydex.dao.FileBasedAddressBook;
import com.marakana.mydex.dao.InMemoryAddressBook;
import com.marakana.mydex.dao.SerializingContactStreamTranscoder;
import com.marakana.mydex.dao.SimpleContactFileResolver;
import com.marakana.mydex.dao.SimpleContactFileTranscoder;
import com.marakana.mydex.dao.XmlContactStreamTranscoder;
import com.marakana.mydex.domain.Contact;

public class Main {
	private static final String PROMPT = "address-book> ";

	private static final String HELP = "Usage: quit|help|list|get <email>|delete <email>|store <first-name> <last-name> <email> [phone]";

	private static void error(String error, boolean usage) {
		System.err.println("ERROR: " + error);
		if (usage) {
			System.err.println(HELP);
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 3) {
			System.err.println("USAGE: Main [<dir> <bin|xml> [compressed]]");
			return;
		}
		final AddressBook addressBook;
		if (args.length == 0) {
			addressBook = new InMemoryAddressBook();
		} else {
			File dir = new File(args[0]);
			ContactFileResolver contactFileResolver = SimpleContactFileResolver.DEFAULT_INSTANCE;
			ContactStreamTranscoder contactStreamTranscoder = args[1].equals("xml") ? XmlContactStreamTranscoder.DEFAULT_INSTANCE
					: SerializingContactStreamTranscoder.DEFAULT_INSTNACE;
			if (args.length >= 3 && args[2].equals("compressed")) {
				contactStreamTranscoder = new CompressingContactStreamTranscoder(
						contactStreamTranscoder);
			}
			ContactFileTranscoder contactFileTranscoder = new SimpleContactFileTranscoder(
					contactStreamTranscoder);

			addressBook = new FileBasedAddressBook(dir, contactFileResolver,
					contactFileTranscoder);
		}
		System.out.print(PROMPT);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = reader.readLine()) != null) {
			if ("quit".equals(line)) {
				addressBook.close();
				break;
			} else if ("help".equals(line)) {
				System.out.println("OK");
				System.out.println(HELP);
			} else if ("list".equals(line)) {
				for (Contact contact : addressBook.getAll()) {
					System.out.println(contact);
				}
				System.out.println("OK");
			} else {
				String[] request = line.split(" ");
				if (request.length >= 2) {
					if ("get".equals(request[0])) {
						Contact contact = addressBook.getByEmail(request[1]);
						if (contact == null) {
							error("No such contact", false);
						} else {
							System.out.println(contact);
							System.out.println("OK");
						}
					} else if ("delete".equals(request[0])) {
						addressBook.deleteByEmail(request[1]);
						System.out.println("OK");
					} else if ("store".equals(request[0]) && request.length >= 4) {
						Contact contact = new Contact(request[1], request[2], request[3]);
						if (request.length >= 5) {
							contact.setPhone(request[4]);
						}
						addressBook.store(contact);
						System.out.println("OK");
					} else {
						error("Invalid request", true);
					}
				} else {
					error("Invalid request", true);
				}
			}
			System.out.print(PROMPT);
		}
	}
}