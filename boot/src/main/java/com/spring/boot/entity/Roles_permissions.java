package com.spring.boot.entity;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles_permissions implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String permission;
	
	private String role_name;
	


}
