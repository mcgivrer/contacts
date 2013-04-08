/**
 * 
 */
package com.capgemini.samples.applications.contacts.test.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Default Generic Test class to initialize JPA engine.
 * 
 * @author fdelorme
 * 
 */
public class GenericTest {
	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	private void initialize() {
		emf = Persistence.createEntityManagerFactory("test-contacts");
		em = emf.createEntityManager();
	}

	public void getInstance() {
		this.initialize();
	}

}
