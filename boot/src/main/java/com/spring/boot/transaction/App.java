package com.spring.boot.transaction;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.boot.entity.Client;
import com.spring.boot.transaction.service.ClientService;
import com.spring.boot.transaction.service.ClientServiceIn;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"applicationContext.xml"})
public class App {
	@Autowired
	ClientServiceIn clientService;

	@Test
	public void test1(){
		List<Client> findAll2 = clientService.findAll2();
		for (Client client : findAll2) {
			System.out.println(client);
		}
	}
	
	@Test
	public void test2(){
		List<Client> findAll2 = clientService.findAll1();
		for (Client client : findAll2) {
			System.out.println(client);
		}
	}
	
	@Test
	public void test3(){
		clientService.updateClient2();
	}
	
	@Test
	public void test4(){
		clientService.updateClient1();
	}
}
