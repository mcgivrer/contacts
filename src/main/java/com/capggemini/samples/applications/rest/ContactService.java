package com.capggemini.samples.applications.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/contact")
public class ContactService {

	Logger logger = LoggerFactory.getLogger(ContactService.class);

	EntityManagerFactory entityManagerFactory;

	public ContactService(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Contact> getContactList() {
		logger.debug("Entering ContactService.getContactList()");

		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		Query q = entityManager.createQuery("SELECT x from Contact x");
		logger.debug("Exiting ContactService.getContactList()");

		return (List<Contact>) q.getResultList();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Contact insertContact(@FormParam("contactId") int contactId,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email) {

		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		Contact contact = new Contact();
		contact.setContactId(contactId);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setEmail(email);
		try {
			entityManager.getTransaction().begin();

			entityManager.persist(contact);
			entityManager.getTransaction().commit();
			logger.debug(String.format("insert Contact %s", contact));
		} catch (Throwable t) {
			if (entityManager.getTransaction().isActive())
				entityManager.getTransaction().rollback();
			contact = null;
		} finally {
			entityManager.close();
		}
		return contact;
	}

	@GET
	@Path("/{contactId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Contact getContact(@PathParam("contactId") int contactId) {
		logger.debug("Entering ContactService.getContact() contactId"
				+ contactId);

		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		Contact contact = entityManager.find(Contact.class, contactId);
		logger.debug("Exiting ContactService.getContact()");

		return contact;
	}

	@PUT
	@Path("/{contactId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Contact updateContact(@PathParam("contactId") int contactId,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email) {
		logger.debug("Entering ContactService.update() contactId" + contactId);

		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		Contact contact = new Contact();
		contact.setContactId(contactId);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setEmail(email);
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(contact);
			entityManager.getTransaction().commit();
		} catch (Throwable t) {
			if (entityManager.getTransaction().isActive())
				entityManager.getTransaction().rollback();
			contact = null;
		} finally {
			entityManager.close();
		}
		logger.debug("Exiting ContactService.updateContact()");

		return contact;
	}

	@DELETE
	@Path("/{contactId}")
	public void deleteContact(@PathParam("contactId") int contactId) {
		logger.debug("Entering ContactService.deleteContact() contactId "
				+ contactId);
		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			Contact contact = entityManager.find(Contact.class, contactId);
			logger.debug("remove contact " + contact);
			entityManager.remove(contact);
			logger.debug("After removing " + contact);
			entityManager.getTransaction().commit();
		} catch (Throwable t) {
			if (entityManager.getTransaction().isActive())
				entityManager.getTransaction().rollback();

		} finally {
			entityManager.close();
		}
		logger.debug("Exiting ContactService.deleteContact()");
	}

}