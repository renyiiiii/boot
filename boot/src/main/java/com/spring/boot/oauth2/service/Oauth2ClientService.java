package com.spring.boot.oauth2.service;

import java.util.List;

import com.spring.boot.entity.Client;

public interface Oauth2ClientService {
	public Client createClient(Client client);// 创建客户端  
    public Client updateClient(Client client);// 更新客户端  
    public void deleteClient(String client_name);// 删除客户端  
    Client findOne(String client_name);// 根据id查找客户端  
    List<Client> findAll();// 查找所有  
    Client findByClientId(String clientId);// 根据客户端id查找客户端  
    Client findByClientSecret(String clientSecret);//根据客户端安全KEY查找客户端 

}
