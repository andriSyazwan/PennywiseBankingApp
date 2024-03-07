package com.backend.pennywise.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.User;
import com.backend.pennywise.exceptions.IncorrectPasswordException;
import com.backend.pennywise.exceptions.UserNotFoundException;
import com.backend.pennywise.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	Logger logger = LogManager.getLogger(UserService.class);
	
	public User findByUserId(long userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> {
								logger.error("User with userId " + userId + " not found");
								return new UserNotFoundException("User not found");
							});
		
		logger.info("Found user with user id " + userId);
		return user;
	}
	
	public User findByUsername(String username) {
		User user = userRepo.findByUsername(username)
					.orElseThrow(() -> {
						logger.error("User with username: " + username + " not found");
						return new UserNotFoundException("User not found");
					});

		return user;
	}
	
	public User findByEmail(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> {
					logger.error("User with email: " + email + " not found");
					return new UserNotFoundException("User not found");
				});
		
		return user;
	}
	
	public List<User> findAllUsers() {
		List<User> userList = userRepo.findAll();
		
		return userList;
	}

	public User addUser(User user) {
		logger.info("Saved new user");
		return userRepo.save(user);
	}
	
	// Check username against password in database
	public User authenticateLogin(String username, String password) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> {
					logger.error("User with username: " + username + " not found");
					return new UserNotFoundException("User not found");
				});
		
		if (! user.getPassword().equals(password)) {
			logger.error("Wrong password entered for username: " + username);
			throw new IncorrectPasswordException("Incorrect password");
		}
		
		logger.info("Password matched with username");
		return userRepo.save(user);
	}
	
	public User updateUserDetails(Map<String, Object> updates, long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});
		
		if (updates.containsKey("password")) {
			existingUser.setPassword((String)updates.get("password"));
			logger.info("Updating password for user id " + userId);
		}
		
		if (updates.containsKey("email")) {
			existingUser.setEmail((String)updates.get("email"));
			logger.info("Updating email for user id " + userId);
		}
		
		if (updates.containsKey("firstName")) {
			existingUser.setFirstName((String)updates.get("firstName"));
			logger.info("Updating first name for user id " + userId);
		}
		
		if (updates.containsKey("lastName")) {
			existingUser.setLastName((String)updates.get("lastName"));
			logger.info("Updating last name for user id " + userId);
		}
		
		if (updates.containsKey("address")) {
			existingUser.setAddress((String)updates.get("address"));
			logger.info("Updating address for user id " + userId);
		}
		
		return userRepo.save(existingUser);
	}

	public User changePassword(String password, long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});
		
		existingUser.setPassword(password);
		logger.info("Updating password for user id " + userId);
		
		return userRepo.save(existingUser);
	}
	
	public Map<String, Boolean> deleteUserByUserId(long userId) {
		User existingUser = userRepo.findById(userId).orElseThrow(() -> {
			logger.error("User with userId " + userId + " not found");
			return new UserNotFoundException("User not found");
		});
		
		userRepo.delete(existingUser);
		logger.info("Successfully deleted user id " + userId);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
	}

}
