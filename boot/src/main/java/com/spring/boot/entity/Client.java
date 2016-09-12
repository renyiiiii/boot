package com.spring.boot.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client implements  RowMapper<Client>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String client_name;
	
	private String client_id;
	
	private String client_secret;

	@Override
	public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Client(rs.getString("client_name"), rs.getString("client_id"), rs.getString("client_secret"));
	}

}
