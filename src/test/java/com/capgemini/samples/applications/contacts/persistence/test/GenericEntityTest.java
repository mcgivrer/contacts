/**
 * 
 */
package com.capgemini.samples.applications.contacts.persistence.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

/**
 * Default Generic Test class to initialize JPA engine.
 * 
 * @author fdelorme
 * 
 */
public class GenericEntityTest<T> {
	/**
	 * Internal Logger.
	 */
	private static Logger logger = Logger.getLogger(GenericEntityTest.class);

	/**
	 * Internal Entity Manager and factory to connect to test database.
	 */
	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	/**
	 * Default constructor initializing EMF and EM.
	 */
	public GenericEntityTest() {
		emf = Persistence.createEntityManagerFactory("test-contacts");
		em = emf.createEntityManager();
		logger.debug("Test adatabase connected and ready for operations.");
	}

}
