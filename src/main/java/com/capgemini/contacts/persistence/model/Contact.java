package com.capgemini.contacts.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Contact {

	@Id
	@GeneratedValue
	private Long contactId;
	private String username;
	private String firstName;
	private String lastName;
	private String email;

	public Contact() {
		super();
	}

	/**
	 * default parameterized Constructor.
	 * 
	 * @param username
	 * @param email
	 * @param firstname
	 * @param lastname
	 */
	public Contact(String username, String email, String firstname,
			String lastname) {
		this();
		this.username = username;
		this.email = email;
		this.firstName = firstname;
		this.lastName = lastname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String
				.format("Contact[contactId:%d, username:%s, firstName:%s, lastName:%s, email:%s]",
						this.contactId, this.username, this.firstName,
						this.lastName, this.email);
	}
}
