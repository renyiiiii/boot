package com.spring.boot.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User_roles implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String username;
	
	private String role_name;
	


}
