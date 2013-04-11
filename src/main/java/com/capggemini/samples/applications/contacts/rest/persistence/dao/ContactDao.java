/**
 * 
 */
package com.capggemini.samples.applications.contacts.rest.persistence.dao;

import javax.persistence.EntityManagerFactory;

import com.capggemini.samples.applications.contacts.rest.persistence.GenericDao;
import com.capggemini.samples.applications.contacts.rest.persistence.model.Contact;

/**
 * @author FDELORME
 * 
 */
public class ContactDao extends GenericDao<Contact, Long> {

	/**
	 * Default constructor
	 */
	public ContactDao() {
		super();
	}

	/**
	 * Parameterized constructor.
	 * 
	 * @param emf
	 */
	public ContactDao(EntityManagerFactory emf) {
		super(emf);
	}
}
