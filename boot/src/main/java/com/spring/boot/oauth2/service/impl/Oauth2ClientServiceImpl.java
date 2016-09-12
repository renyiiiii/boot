package com.spring.boot.oauth2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.spring.boot.entity.Client;
import com.spring.boot.oauth2.service.Oauth2ClientService;

@Service
public class Oauth2ClientServiceImpl implements Oauth2ClientService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Client createClient(Client client) {
		int f = jdbcTemplate.update("insert into oauth2_client(client_name,client_id,client_secret) values (?,?,?)",
				client.getClient_name(), client.getClient_id(), client.getClient_secret());
		if (f == 1) {
			return findOne(client.getClient_name());
		}
		return null;
	}

	@Override
	public Client updateClient(Client client) {
		int f = jdbcTemplate.update("update oauth2_client set client_id = ? ,client_secret = ?  where client_name = ?",
				client.getClient_id(), client.getClient_secret(), client.getClient_name());
		if (f == 1) {
			return findOne(client.getClient_name());
		}
		return null;
	}

	@Override
	public void deleteClient(String client_name) {
		jdbcTemplate.update("delete from oauth2_client where client_name = ?", client_name);
	}

	@Override
	public Client findOne(String client_name) {
		try {
			return jdbcTemplate.queryForObject("select * from oauth2_client where client_name = ?",
					new Object[] { client_name }, new Client());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Client> findAll() {
		return jdbcTemplate.query("select * from oauth2_client", new Client());
	}

	@Override
	public Client findByClientId(String client_id) {
		try {
			return jdbcTemplate.queryForObject("select * from oauth2_client where client_id = ?",
					new Object[] { client_id }, new Client());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Client findByClientSecret(String client_secret) {
		try {
			return jdbcTemplate.queryForObject("select * from oauth2_client where client_secret = ?",
					new Object[] { client_secret }, new Client());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
