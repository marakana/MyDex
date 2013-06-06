package com.marakana.mydex.dao;

import java.io.File;

public class FileBasedAddressBookFactory implements AddressBookFactory {

	public static enum StreamType {
		SERIALIZED {
			@Override
			public ContactStreamTranscoder getContactStreamTranscoder() {
				return SerializingContactStreamTranscoder.DEFAULT_INSTNACE;
			}
		},
		XML {
			@Override
			public ContactStreamTranscoder getContactStreamTranscoder() {
				return XmlContactStreamTranscoder.DEFAULT_INSTANCE;
			}
		};
		public abstract ContactStreamTranscoder getContactStreamTranscoder();
	}

	private File dir;
	private ContactFileResolver contactFileResolver;
	private StreamType streamType;
	private boolean compression;

	@Required
	public void setDir(String dir) {
		this.dir = new File(dir);
	}

	@Default(".contact")
	public void setExtension(String extension) {
		this.contactFileResolver = new SimpleContactFileResolver(extension);
	}

	@Default("XML")
	public void setStreamType(String streamType) {
		this.streamType = StreamType.valueOf(streamType);
	}

	@Default("true")
	public void setCompression(String compression) {
		this.compression = Boolean.valueOf(compression).booleanValue();
	}

	@Override
	public AddressBook getAddressBook() throws AddressBookException {
		ContactStreamTranscoder contactStreamTranscoder = this.streamType
				.getContactStreamTranscoder();
		if (this.compression) {
			contactStreamTranscoder = new CompressingContactStreamTranscoder(
					contactStreamTranscoder);
		}
		return new FileBasedAddressBook(this.dir, this.contactFileResolver,
				new SimpleContactFileTranscoder(contactStreamTranscoder));
	}
}