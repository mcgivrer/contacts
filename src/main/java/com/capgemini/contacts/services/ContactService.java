package com.capgemini.contacts.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

import com.capgemini.contacts.persistence.dao.ContactDao;
import com.capgemini.contacts.persistence.model.Contact;

@Path("/contact")
public class ContactService {

	Logger logger = LoggerFactory.getLogger(ContactService.class);

	EntityManagerFactory entityManagerFactory = null;
	EntityManager entityManager = null;

	ContactDao contacts = new ContactDao();

	public ContactService(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Contact> getContactList() {
		logger.debug("Entering ContactService.getContactList()");

		return contacts.getAll();
	}

	@SuppressWarnings("unchecked")
	@Path("/{offset}-{number}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Contact> paginatedContactList(@PathParam("offset") int offset,
			@PathParam("number") int number) {

		return contacts.getPaginated(offset, number);
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Contact insertContact(@FormParam("contactId") Long contactId,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email) {

		Contact contact = new Contact();
		contact.setContactId(contactId);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setEmail(email);

		contacts.save(contact);

		return contact;
	}

	@GET
	@Path("/{contactId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Contact getContact(@PathParam("contactId") long contactId) {
		logger.debug("Entering ContactService.getContact() contactId"
				+ contactId);

		Contact contact = contacts.find(contactId);
		logger.debug("Exiting ContactService.getContact()");

		return contact;
	}

	@PUT
	@Path("/{contactId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Contact updateContact(@PathParam("contactId") long contactId,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email) {
		logger.debug("Entering ContactService.update() contactId" + contactId);

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
	public void deleteContact(@PathParam("contactId") long contactId) {
		logger.debug("Entering ContactService.deleteContact() contactId "
				+ contactId);

		contacts.delete(contactId);
		logger.debug("Exiting ContactService.deleteContact()");
	}

}