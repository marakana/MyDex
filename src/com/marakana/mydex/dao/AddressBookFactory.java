package com.marakana.mydex.dao;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates address books. Dependencies are resolved using setter methods. That
 * means, that if you want "foo", create a setter method called
 * "public void setFoo(String foo)"
 * 
 * This is something that we should almost never do. Frameworks (such as Spring)
 * already exist that do a much better job at resolving dependencies than what
 * we can build.
 */
public interface AddressBookFactory {

	public static class Builder {

		private static final Pattern PROP_PATTERN = Pattern.compile("([^=]+)=(.+)");

		private final Class<AddressBookFactory> clazz;

		private final AddressBookFactory addressBookFactory;

		private Map<String, Method> setters = new HashMap<String, Method>();

		@SuppressWarnings("unchecked")
		public Builder(String addressBookFactoryType) throws AddressBookException {
			try {
				this.clazz = (Class<AddressBookFactory>) Class
						.forName(addressBookFactoryType);
				this.addressBookFactory = this.clazz.newInstance();

				for (Method method : clazz.getDeclaredMethods()) {
					String name = method.getName();
					if (name.startsWith("set") && method.getParameterTypes().length == 1
							&& method.getParameterTypes()[0] == String.class
							&& method.getReturnType() == void.class
							&& Modifier.isPublic(method.getModifiers())
							&& !Modifier.isStatic(method.getModifiers())) {
						name = name.substring(3);
						name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
						this.setters.put(name, method);
					}
				}
			} catch (ClassNotFoundException | ClassCastException
					| InstantiationException | IllegalAccessException e) {
				throw new AddressBookException(
						"Failed to initialize builder for address book factory type ["
								+ addressBookFactoryType + "]", e);
			}
		}

		public void setProperty(String name, String value)
				throws AddressBookException {
			// each property can be set only once, so remove the setter
			Method method = this.setters.remove(name);
			if (method == null) {
				throw new AddressBookException("No such property [" + name + "] on ["
						+ this.addressBookFactory.getClass().getName() + "]");
			} else {
				try {
					// invoke the setter method with the given value
					method.invoke(this.addressBookFactory, value);
				} catch (Exception e) {
					throw new AddressBookException("Failed to set property [" + name
							+ "]=[" + value + "] on ["
							+ this.addressBookFactory.getClass().getName() + "]", e);
				}
			}
		}

		public void setProperty(String property) throws AddressBookException {
			Matcher m = PROP_PATTERN.matcher(property);
			if (m.matches()) {
				this.setProperty(m.group(1), m.group(2));
			} else {
				throw new IllegalArgumentException("Invalid property [" + property
						+ "]. Expecting property in <name>=<value> format.");
			}
		}

		public AddressBookFactory getAddressBookFactory()
				throws AddressBookException {
			// go through all remaining setters
			for (Map.Entry<String, Method> entry : this.setters.entrySet()) {
				String name = entry.getKey();
				Method method = entry.getValue();
				Default defaultAnnotation = method.getAnnotation(Default.class);
				Required requiredAnnotation = method.getAnnotation(Required.class);
				// if the setter has a default value
				if (defaultAnnotation != null) {
					String value = defaultAnnotation.value();
					try {
						// set the default value
						method.invoke(this.addressBookFactory, value);
					} catch (Exception e) {
						// handle error
						throw new AddressBookException("Failed to set default property ["
								+ name + "]=[" + value + "] on ["
								+ this.addressBookFactory.getClass().getName() + "]", e);
					}
				} else if (requiredAnnotation != null) {
					// the setter is required
					throw new AddressBookException("Required property [" + name
							+ "] has not been set on ["
							+ this.addressBookFactory.getClass().getName() + "]");
				}
			}
			return this.addressBookFactory;
		}
	}

	public AddressBook getAddressBook() throws AddressBookException;
}