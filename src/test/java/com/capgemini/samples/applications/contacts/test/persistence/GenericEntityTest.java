/**
 * 
 */
package com.capgemini.samples.applications.contacts.test.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl;

/**
 * Default Generic Test class to initialize JPA engine.
 * 
 * @author fdelorme
 * 
 */
public class GenericEntityTest<T> {
	
	private static Logger log = Logger.getLogger(GenericEntityTest.class);
	
	
	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	List<T> list = new ArrayList<T>();
	
	public GenericEntityTest() {
		emf = Persistence.createEntityManagerFactory("test-contacts");
		em = emf.createEntityManager();
	}

}
