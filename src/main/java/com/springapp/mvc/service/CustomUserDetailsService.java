package com.springapp.mvc.service;

import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom service for retrieving users from a custom datasource, such as a database.
 * <p>
 * This custom service must implement Spring's {@link org.springframework.security.core.userdetails.UserDetailsService}
 */
//@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
	


	//private UserDAO userDAO = new UserDAO();


    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
//    public CustomUserDetailsService(UserRepositry repositry) {
//        this.repositry = repositry;
//    }

    /**
	 * Retrieves a user record containing the user's credentials and access. 
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {


        System.out.println("----------------------" + username);
        // Declare a null Spring User
		UserDetails user = null;
		
		try {

            System.out.println("------------2-------" + username);
			// Search database for a user that matches the specified username
			// You can provide a custom DAO to access your persistence layer
			// Or use JDBC to access your database
			// DbUser is our custom domain user. This is not the same as Spring's User
			com.springapp.mvc.model.DbUser dbUser = repository.fetchUser(username);

            System.out.println("------------3-------" + username);
			
			// Populate the Spring User object with details from the dbUser
			// Here we just pass the username, password, and access level
			// getAuthorities() will translate the access level to the correct role type
            List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
            authList.add(new GrantedAuthorityImpl("ROLE_USER"));

			user =  new User(
					dbUser.getUsername(), 
					dbUser.getPassword().toLowerCase(),
					true,
					true,
					true,
					true,
					authList);

            System.out.println(dbUser.getPassword() + " " +  user.getPassword());

		} catch (Exception e) {

			throw new UsernameNotFoundException("Error in retrieving user");
		}

        // Return user to Spring for processing.
		// Take note we're not the one evaluating whether this user is authenticated or valid
		// We just merely retrieve a user that matches the specified username
		return user;
	}
	
	/**
	 * Retrieves the correct ROLE type depending on the access level, where access level is an Integer.
	 * Basically, this interprets the access value whether it's for a regular user or admin.
	 * 
	 * @param access an integer value representing the access of the user
	 * @return collection of granted authorities
//	 */
//	 public Collection<GrantedAuthority> getAuthorities(Integer access) {
//			// Create a list of grants for this user
//			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
//
//			// All users are granted with ROLE_USER access
//			// Therefore this user gets a ROLE_USER by default
//			logger.debug("Grant ROLE_USER to this user");
//			authList.add(new GrantedAuthorityImpl("ROLE_USER"));
//
//			// Check if this user has admin access
//			// We interpret Integer(1) as an admin user
//			if ( access.compareTo(1) == 0) {
//				// User has admin access
//				logger.debug("Grant ROLE_ADMIN to this user");
//				authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
//			}
//
//			// Return list of granted authorities
//			return authList;
//	  }
}
