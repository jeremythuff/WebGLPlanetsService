/* 
 * AppUserRepoCustom.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package com.planets.app.model.repo;

import com.planets.app.model.AppUser;

/**
 * 
 */
public interface AppUserRepoCustom {

    /**
     * method to create user based on id
     * 
     * @param uin
     *            Long
     */
    public AppUser create(Long uin);

    public AppUser create(String email, String firstName, String lastName);
    
    public AppUser create(String email, String firstName, String lastName, String password);
    
    /**
	 * method to delete application user
	 * 
	 * @param       user        AppUser
	 */
	public void delete(AppUser user);

}
