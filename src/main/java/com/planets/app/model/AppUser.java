/* 
 * AppUser.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package com.planets.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.tamu.framework.model.AbstractCoreUserImpl;

/**
 * Application User entity.
 * 
 */
@Entity
public class AppUser extends AbstractCoreUserImpl {

    @Column(nullable = true, unique = true)
    private String email;

    // encoded password
    @JsonIgnore
    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    /**
     * Constructor for the application user
     * 
     */
    public AppUser() {
        super();
    }

    /**
     * Constructor for application user with id passed.
     * 
     * @param id
     *            Long
     * 
     */
    public AppUser(Long id) {
        super(id);
    }

    /**
     * Constructor for application user with email, firstName, lastName and role passed.
     * 
     * @param email
     *            String
     * @param firstName
     *            String
     * @param lastName
     *            String
     * @param role
     *            String
     * 
     */
    public AppUser(String email, String firstName, String lastName, String role) {
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return firstName
     * 
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            String
     * 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return lastName
     * 
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            String
     * 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
