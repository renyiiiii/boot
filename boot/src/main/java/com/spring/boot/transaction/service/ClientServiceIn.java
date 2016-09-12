package com.spring.boot.transaction.service;

import java.util.List;

import com.spring.boot.entity.Client;

public interface ClientServiceIn {
	public List<Client> findAll2();
	public List<Client> findAll1();
	public void createClient1();
	public void updateClient2();
	public void updateClient1();

}
