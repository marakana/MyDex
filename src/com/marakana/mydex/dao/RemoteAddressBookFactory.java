package com.marakana.mydex.dao;

import java.net.InetSocketAddress;

public class RemoteAddressBookFactory implements AddressBookFactory {

	private int port;

	private String hostname;

	@Required
	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}

	@Default("localhost")
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public AddressBook getAddressBook() throws AddressBookException {
		return new RemoteAddressBook(new InetSocketAddress(hostname, port));
	}
}