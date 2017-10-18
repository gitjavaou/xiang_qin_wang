package com.atguigu.xiang_qin.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.atguigu.xiang_qin.demo.pojo.User;

public interface UserService {

	int getUserCountForRegist(String userName);

	void saveUser(User user) throws SolrServerException, IOException;

	User getUserForLogin(User userForm);

	void updateUserByExample(User user) throws SolrServerException, IOException;

	User getUserById(Integer userId);

	ArrayList<Map<String, String>> getDataFromSolrIndex(String keywords) throws SolrServerException;

	


}
