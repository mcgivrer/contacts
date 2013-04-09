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

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Default Generic Test class to initialize JPA engine.
 * 
 * @author fdelorme
 * 
 */
public class GenericEntityTest<T> {
	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	public GenericEntityTest() {
		emf = Persistence.createEntityManagerFactory("test-contacts");
		em = emf.createEntityManager();
	}

	/**
	 * Parse the *.yaml file <code>dataFile</code> to store contains described data of <code>className</code> type to the
	 * persistence.
	 * 
	 * @param dataFile
	 * @param className
	 */
	protected void loadTestData(String dataFile) {
		List<T> objectList = new ArrayList<T>();
		Yaml yaml = new Yaml(new Constructor(objectList.getClass()));
		FileReader dataio;
		try {
			dataio = new FileReader(this.getClass().getResource("/").getPath()
					+ dataFile);
			objectList = (List) yaml.load(dataio);
			for (Object object : objectList) {
				em.persist(object);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
