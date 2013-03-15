package com.capggemini.samples.applications.rest;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("rest")
public class Application extends javax.ws.rs.core.Application{
	Logger logger = LoggerFactory.getLogger(Application.class);

	@Override
	public Set<Object> getSingletons() {
		logger.debug("Entering Application.getSingletons()");
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("contacts");
		ContactService contactService = new ContactService(entityManagerFactory);
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(contactService);
		logger.debug("Exiting Application.getSingletons()");
		return singletons;
	}

}
