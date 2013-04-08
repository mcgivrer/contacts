package com.capgemini.samples.applications.contacts.test.persistence;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.capggemini.samples.applications.contacts.rest.persistence.model.Contact;

class ContactPersistenceTest extends GenericTest {
	@Before
	public void init() {
		getInstance();
	}
	@Test
	public void saveContactTest(){
		Contact contact = new Contact("user01","user.01@mail.com","user","01");
		em.persist(contact);
		assertNull(contact.getContactId());
	}
	
}