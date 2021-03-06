package com.capgemini.contacts.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capgemini.contacts.services.ContactService;

@ApplicationPath("rest")
public class Application extends javax.ws.rs.core.Application{
	Logger logger = LoggerFactory.getLogger(Application.class);

	@Override
	public Set<Object> getSingletons() {
		ContactService contactService = new ContactService();
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(contactService);
		logger.info("ContactsService is ready at work.");
		return singletons;
	}

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        // following code can be used to customize Jersey 1.x JSON provider:
        try {
            Class jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
            resources.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically re-generated by NetBeans REST support to populate
     * given list with all resources defined in the project.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.capgemini.contacts.services.ContactService.class);
    }

}
