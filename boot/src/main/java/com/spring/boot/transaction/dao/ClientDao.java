package com.spring.boot.transaction.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.spring.boot.entity.Client;
import com.spring.boot.transaction.util.DataSource;

public class ClientDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@DataSource("master")
	public int createClient1(Client client) {
		int f = jdbcTemplate.update("insert into oauth2_client(client_name,client_id,client_secret) values (?,?,?)",
				client.getClient_name(), client.getClient_id(), client.getClient_secret());
	
		return f;
	}
	@DataSource("slave")
	public int createClient2(Client client) {
		int f = jdbcTemplate.update("insert into oauth2_client(client_name,client_id,client_secret) values (?,?,?)",
				client.getClient_name(), client.getClient_id(), client.getClient_secret());
	
		return f;
	}
	
	@DataSource("master")
	public List<Client> findAll1() {
		return jdbcTemplate.query("select * from oauth2_client", new Client());
	}
	@DataSource("slave")
	public List<Client> findAll2() {
		return jdbcTemplate.query("select * from oauth2_client", new Client());
	}
	
	@DataSource("slave")
	public int updateClient2(Client client){
		int f = jdbcTemplate.update("update  oauth2_client set client_secret = ? where client_name = ?",
				client.getClient_secret(),client.getClient_name());
		return f;
	}
	
	@DataSource("master")
	public int updateClient1(Client client){
		int f = jdbcTemplate.update("update  oauth2_client set client_secret = ? where client_name = ?",
				client.getClient_secret(),client.getClient_name());
		return f;
	}

}
