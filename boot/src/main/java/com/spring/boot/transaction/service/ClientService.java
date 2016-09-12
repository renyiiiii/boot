package com.spring.boot.transaction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.spring.boot.entity.Client;
import com.spring.boot.transaction.dao.ClientDao;
public class ClientService implements ClientServiceIn{
	@Autowired
	ClientDao clientDao;
	@Autowired
	TransactionTemplate transactionTemplate;
	
	public List<Client> findAll2() {
		return clientDao.findAll2();
	}
	
	public List<Client> findAll1() {
		return clientDao.findAll1();
	}
	
	public void createClient1(){
		Client client = new Client("d1dddd11", "d1dddd11", "");
		int f = clientDao.createClient1(client);
		System.out.println(f);
	}
	
	@Transactional
	public void updateClient2(){
		Client client1 = new Client("d2cn2", "ci1", "p");
		int f1 = clientDao.updateClient2(client1);
		System.out.println(f1);
		Client client = new Client("d2cn1", "ci1", "dfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffs");
		int f = clientDao.updateClient2(client);
		System.out.println(f);
	}
	
	
	public void updateClient1(){
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {

			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				try{
					Client client1 = new Client("d1cn2", "ci1", "p11");
					int f1 = clientDao.updateClient1(client1);
					System.out.println(f1);
					Client client = new Client("d1cn1", "ci1", "dfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffsdfffffffs");
					int f = clientDao.updateClient1(client);
					System.out.println(f);
				
				}catch(RuntimeException e){
					status.setRollbackOnly();
					throw e;
				}
				
			}
		});
		
		
	}
}
