package com.marakana.mydex.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.marakana.mydex.dao.AddressBook;
import com.marakana.mydex.dao.AddressBookException;
import com.marakana.mydex.dao.AddressBookFactory;
import com.marakana.mydex.domain.Contact;

public class RemoteAddressBookServer implements Runnable {

	private static final String USAGE = "RemoteAddressBookServer <port> <address-book-factory-type> <prop-name>=<prop-value> ...";

	private static final String PROMPT = "address-book> ";

	private static final String HELP = "Usage: quit|help|list|get <email>|delete <email>|store <first-name> <last-name> <email> [phone]";

	public static void main(String[] args) throws IOException,
			AddressBookException {
		if (args.length < 2) {
			System.err.println(USAGE);
			return;
		}
		int port = Integer.parseInt(args[0]);
		AddressBookFactory.Builder builder = new AddressBookFactory.Builder(args[1]);
		for (int i = 2; i < args.length; i++) {
			try {
				builder.setProperty(args[i]);
			} catch (IllegalArgumentException e) {
				System.err.println("ERROR: " + e.getMessage());
				System.err.println(USAGE);
				return;
			}
		}
		AddressBook addressBook = builder.getAddressBookFactory().getAddressBook();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Listening on port " + port);
		final RemoteAddressBookServer server = new RemoteAddressBookServer(
				addressBook, serverSocket);
		server.run();
	}

	private final AddressBook addressBook;

	private final ServerSocket serverSocket;

	public RemoteAddressBookServer(AddressBook addressBook,
			ServerSocket serverSocket) {
		this.addressBook = addressBook;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		while (this.serverSocket.isBound() && !this.serverSocket.isClosed()) {
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Handling connection from "
						+ clientSocket.getRemoteSocketAddress());
				this.handle(clientSocket);
			} catch (SocketException e) {
				if (this.serverSocket.isClosed()) {
					break;
				} else {
					System.err.println("Socket error: " + e.getMessage());
					e.printStackTrace();
				}
			} catch (IOException e) {
				System.err.println("I/O Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("Good-bye");
	}

	public void handle(Socket socket) {
		try {
			try {
				PrintStream out = new PrintStream(socket.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out.print(PROMPT);
				String line;
				while ((line = reader.readLine()) != null) {
					if ("quit".equals(line)) {
						break;
					} else if ("help".equals(line)) {
						out.println("OK");
						out.println(HELP);
					} else {
						try {
							if (this.handle(line.split(" "), out)) {
								out.println("OK");
							} else {
								error(out, "Invalid request", true);
							}
						} catch (AddressBookException e) {
							error(out, e.getMessage(), true);
							e.printStackTrace();
						}
					}
					if (out.checkError()) {
						break;
					}
					out.print(PROMPT);
					out.flush();
				}
			} finally {
				socket.close();
			}
		} catch (IOException e) {
			System.err
					.println("I/O failure while talking to remote client. Aborting.");
			e.printStackTrace();
		}
	}

	private boolean handle(String[] request, PrintStream out)
			throws AddressBookException {
		if (request.length >= 1 && "list".equals(request[0])) {
			for (Contact contact : addressBook.getAll()) {
				out.println(contact);
			}
			return true;
		} else {
			if (request.length >= 2) {
				if ("get".equals(request[0])) {
					Contact contact = addressBook.getByEmail(request[1]);
					if (contact != null) {
						out.println(contact);
					}
					return true;
				} else if ("delete".equals(request[0])) {
					addressBook.deleteByEmail(request[1]);
					return true;
				} else if ("store".equals(request[0]) && request.length >= 4) {
					Contact contact = new Contact(request[1], request[2], request[3]);
					if (request.length >= 5) {
						contact.setPhone(request[4]);
					}
					addressBook.store(contact);
					return true;
				}
			}
		}
		return false;
	}

	private void error(PrintStream out, String error, boolean usage) {
		out.println("ERROR: " + error);
		if (usage) {
			out.println(HELP);
		}
	}
}