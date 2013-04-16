package com.capgemini.samples.applications.contacts.test.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.samples.applications.contacts.persistence.test.GenericEntityTest;
import com.capggemini.samples.applications.contacts.rest.persistence.dao.ContactDao;
import com.capggemini.samples.applications.contacts.rest.persistence.model.Contact;

public class ContactPersistenceTest extends GenericEntityTest<Contact> {

	ContactDao contacts = null;;

	@Before
	public void setup() {
		contacts = new ContactDao(this.emf);
	}

	@After
	public void destroy() {
		contacts = null;
	}

	/*
	 * Initialize persistence !
	 */
	public ContactPersistenceTest() {
		super();
	}

	/**
	 * Verify Entity persist.
	 */
	@Test
	public void saveContactTest() {
		Contact contact = new Contact("user01", "user.01@mail.com", "user",
				"01");
		contacts.save(contact);
		assertEquals("Contact was not correctly initialized", false,
				contact.getContactId()!=null);
	}

	/**
	 * 
	 */
	@Test
	public void loadContactFromYamlTest() {
		contacts.loadFromYaml("data/contact.yaml");
	}

}