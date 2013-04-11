package com.capgemini.samples.applications.contacts.test.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.capggemini.samples.applications.contacts.rest.persistence.model.Contact;

public class ContactPersistenceTest extends GenericEntityTest<Contact> {

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
		em.persist(contact);
		assertEquals("Contact was not correctly initialized", 0,
				contact.getContactId());
	}
	@Test
	public void loadContactTest() {
		loadTestData("data/contact.yaml", Contact.class);
	}


}