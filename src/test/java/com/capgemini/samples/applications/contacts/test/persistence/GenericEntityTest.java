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

	/**
	 * Parse the *.yaml file <code>dataFile</code> to store contains described
	 * data of <code>className</code> type to the persistence.
	 * 
	 * @param dataFile
	 * @param className
	 */
	protected void loadTestData(String dataFile, Class<?> entityClass) {
		
		Yaml yaml = new Yaml(new Constructor(ArrayList.class));
		FileReader dataio;
		try {
			dataio = new FileReader(this.getClass().getResource("/").getPath()
					+ dataFile);
			list = (List<T>) yaml.load(dataio);
			for (T entity : list) {
				
				System.out.println(entity.toString());
				
				//em.persist(entity);
			}

		} catch (FileNotFoundException e) {
			log.fatal("Unable to read file",e);
		}

	}

}
