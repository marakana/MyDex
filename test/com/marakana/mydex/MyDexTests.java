package com.marakana.mydex;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.marakana.mydex.dao.FileBasedAddressBookTest;
import com.marakana.mydex.dao.InMemoryAddressBookTest;
import com.marakana.mydex.domain.ContactTest;

@RunWith(Suite.class)
@SuiteClasses({ ContactTest.class, FileBasedAddressBookTest.class,
		InMemoryAddressBookTest.class })
public class MyDexTests {

}
