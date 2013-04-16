package com.capggemini.samples.applications.contacts.rest.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

/**
 * Generic Data Object Access to implement persistence for a <code>T</code>
 * entity having a specific <code>PK</code> type..
 * 
 * @author FDELORME
 * 
 * @param <T>
 * @param <PK>
 */
public class GenericDao<T, PK> {

	/**
	 * Jpa factory
	 */
	protected static EntityManagerFactory emf = null;
	/***
	 * JPA Manager
	 */
	protected EntityManager em = null;

	/**
	 * current offset for this entity.
	 */
	protected int offset;

	/**
	 * page size for list retrieve and parse.
	 */
	protected int pageSize;

	protected List<T> list;

	/**
	 * Internal Logger.
	 */
	private static Logger logger = Logger.getLogger(GenericDao.class);

	/**
	 * Entity Class name manipulated by this DAO.
	 */
	protected Class<T> entityClass;

	/**
	 * Default Constructor.
	 */
	public GenericDao() {
		ParameterizedType genericSuperClass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) genericSuperClass
				.getActualTypeArguments()[0];
		this.entityClass = entityClass;

		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("contacts");
			logger.debug("Entity Manager Factory Initialiazed");
		}

		init();
	}

	/**
	 * Default parameterized constructor for DAO.
	 * 
	 * @param emf
	 *            EntityManager
	 */
	public GenericDao(EntityManagerFactory emf) {
		this();
		GenericDao.emf = emf;
		init();
	}

	public void init() {
		em = emf.createEntityManager();
		logger.debug("EntityManager initialized.");
	}

	/**
	 * <p>
	 * Retrieve all <code>T</code> entity respecting <code>pageSize</code>.
	 * </p>
	 * 
	 * @return
	 */
	public List<T> getAll() {
		logger.debug(String.format(
				"Retrieve all entities %s without pagination...",
				entityClass.getSimpleName()));
		this.list = getPaginated(0, -1);
		logger.debug("done.");
		return this.list;
	}

	/**
	 * <p>
	 * Retrieve paginated data for T entity.
	 * </p>
	 * 
	 * <p>
	 * If <code>pageSize</code> equal <strong>-1</strong>, pageSize is not added
	 * to request to retrieve all occurrences (performance issue).
	 * </p>
	 * <p>
	 * If <code>offset</code> is greater than <strong>0</strong>, the offset
	 * would not be integrate to request (performance issue).
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public List<T> getPaginated(int offset, int pageSize) {

		logger.debug(String.format("Retrieve entities %s...",
				entityClass.getSimpleName()));

		Query q = em.createQuery("SELECT x from " + entityClass.getSimpleName()
				+ " x");
		if (offset > 0) {
			this.offset = offset;
			q.setFirstResult(offset);
			logger.debug(String.format("set offset=%d pagination", offset));
		}
		if (pageSize != -1) {
			this.pageSize = pageSize;
			q.setMaxResults(pageSize);
			logger.debug(String.format("set pageSize=%d pagination", pageSize));
		}
		this.list = q.getResultList();
		logger.debug("done.");
		return this.list;
	}

	/**
	 * Retrieve the <code>entity</code> T on its primaryKey <code>pk</code>.
	 * 
	 * @param pk
	 *            primary key for th T entity.
	 * @return
	 */
	public T find(PK pk) {
		T entity = null;
		logger.debug(String.format(
				"Retrieve Entity %s with Primary key %s ...", entityClass, pk));
		entity = em.find(entityClass, pk);
		logger.debug("done.");
		return entity;
	}

	/**
	 * Save to persistence the entity in a transaction.
	 * 
	 * @param entity
	 * @return
	 */
	public T save(T entity) {
		logger.debug(String.format("Save entity %s ...",
				entityClass.getSimpleName()));
		try {

			entity = em.merge(entity);
			logger.debug(String.format("insert %s :  %s",
					entityClass.getSimpleName(), entity));
		} catch (PersistenceException e) {
			entity = null;
		}
		logger.debug("done.");
		return entity;
	}

	public T delete(PK pk) {
		logger.debug(String.format("Delete Entity %s on Primary key %s ...",
				entityClass, pk));
		T entity = null;
		try {

			entity = em.find(entityClass, pk);
			em.remove(entity);
			logger.debug(String.format("remove entity %s(%s)", entityClass,
					entity));

		} catch (Throwable t) {
			entity =null;
		}
		logger.debug("done.");
		return entity;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * <p>
	 * Load data from <code>filename</code> a yaml file for test purpose, and
	 * return a <code>List&lt;T&gt;</code> entity.
	 * </p>
	 * 
	 * @param filename
	 * @return
	 */
	public void loadFromYaml(String filename) {
		FileReader fileReader;
		Map<String, Object> persistedEntities = new HashMap<String, Object>();
		try {
			fileReader = new FileReader(this.getClass().getResource("/")
					.getPath()
					+ filename);
			if (filename != null && !filename.equals("") && fileReader != null) {
				boolean trasnsactionActivated = false;
				Yaml yaml = new Yaml();
				try {
					trasnsactionActivated = em.getTransaction() != null
							& em.getTransaction().isActive();
					if (!trasnsactionActivated) {
						em.getTransaction().begin();
					}
					List<?> entities = (List<?>) yaml.load(fileReader);
					for (Object entity : entities) {
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, HashMap<String, Object>> lhm = (LinkedHashMap<String, HashMap<String, Object>>) entity;
						logger.debug(String.format("lhm=%s", lhm));
						ObjectMapper om = new ObjectMapper();
						String className = null;
						String id = null;
						for (String key : lhm.keySet()) {
							className = key.substring(0, key.indexOf("("));
							id = key.substring(key.indexOf("(") + 1,
									key.indexOf(")"));
							Object entityConverted = null;
							try {
								Class<?> entityClassToInstanciate = Class
										.forName(className);
								entityConverted = om.convertValue(lhm.get(key),
										entityClassToInstanciate);

								//entityConverted = 
								em.persist(entityConverted);
								
								persistedEntities.put(id, entityConverted);
								em.persist(entityConverted);
								logger.debug(String.format(
										"Entity[%s]%s => %s", entity.getClass()
												.getSimpleName(), entity,
										entityConverted.toString()));
							} catch (ClassNotFoundException e) {
								logger.fatal(String.format(
										"Unknown class %s for entity %s",
										className, entityConverted), e);
							} catch (PersistenceException e) {
								logger.fatal(
										String.format(
												"Unable to save the entityConverted:%s",
												entityConverted), e);
							}

						}
					}
				} catch (PersistenceException te) {
					logger.fatal(String.format(
							"Error during loading yml file %s",
							filename), te);
				} finally {
					//em.close();
				}
			}
		} catch (FileNotFoundException e) {
			list = null;
			logger.fatal(String.format(
					"Unable to read entities from %s Yaml file", filename));
		}

	}
}
