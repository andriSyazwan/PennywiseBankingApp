package com.backend.pennywise.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.pennywise.entities.User;
import com.backend.pennywise.services.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") long userId) {
		return userService.findByUserId(userId);
	}
	
	@GetMapping
	public List<User> getAllUser(){
		return userService.findAllUsers();
	}
	
	@GetMapping("/username/{username}")
	public User getUser(@PathVariable("username") String username) {
		return userService.findByUsername(username);
	}
	
	@GetMapping("/email/{email}")
	public User getUserFromEmail(@PathVariable("email") String email) {
		return userService.findByEmail(email);
	}
	
	/* ===================== Post Methods ===================== */
	
	@PostMapping("/add")
	public ResponseEntity<User> addNewUser(@RequestBody User user){
		User savedUser = userService.addUser(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedUser.getUserId()).toUri();

		return ResponseEntity.created(location).body(savedUser);
	}
	
	@PostMapping("/login")
	public User userLogin(@RequestBody Map<String, String> loginDetails) {
		String username = loginDetails.get("username");
		String password = loginDetails.get("password");
		
		return userService.authenticateLogin(username, password);
	}
	
	/* ===================== Patch Methods ===================== */
	@PatchMapping("/{id}")
	public ResponseEntity<User> updateUser(@RequestBody Map<String, Object> updates,
											@PathVariable (value = "id") long userId){
		User updatedUser = userService.updateUserDetails(updates, userId);
		
		return ResponseEntity.ok(updatedUser);
	}
	
	
	@PatchMapping("/password/{id}")
	public ResponseEntity<User> updatePassword(@RequestParam String password,
											   @PathVariable(value = "id")long userId) {
		
		User updatedUser = userService.changePassword(password, userId);
		
		return ResponseEntity.ok(updatedUser);
		
	}	
	
	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteUser (@PathVariable(value = "id") Long userId){
		return userService.deleteUserByUserId(userId);
	}
}
