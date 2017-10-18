package com.atguigu.xiang_qin.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.xiang_qin.demo.mapper.UserMapper;
import com.atguigu.xiang_qin.demo.pojo.User;
import com.github.abel533.entity.Example;
import com.github.abel533.entity.Example.Criteria;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public int getUserCountForRegist(String userName) {
		
		Example example = new Example(User.class);
		
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userName", userName);
		
		return userMapper.selectCountByExample(example);
	}

	@Override
	public void saveUser(User user) throws SolrServerException, IOException {
		
		//将数据存入数据库中
		userMapper.insertSelective(user);
		
		//把用户的信息保存到索引库中
		//1.创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//2.添加字段
		document.setField("id", user.getUserId());
		document.setField("user_nick", user.getUserNick());
		
		//3.使用solrserver对象执行文档的添加操作
		solrServer.add(document);
		solrServer.commit();
		
	}

	/**
	 * 登录页面的实现
	 */
	@Override
	public User getUserForLogin(User userForm) {
		String userName = userForm.getUserName();
		String userPwd = userForm.getUserPwd();
		
		Example example = new Example(User.class);
		
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userName", userName).andEqualTo("userPwd", userPwd);
		
		List<User> list = userMapper.selectByExample(example);
		
		if (list==null || list.size()==0) {
			return null;
		}
		
		User user = list.get(0);
		return user;
	}

	@Override
	public void updateUserByExample(User user) throws SolrServerException, IOException {
		
		userMapper.updateByPrimaryKeySelective(user);
		
		//获取当前的userId
		Integer userId = user.getUserId();
		
		//根据userid作为文档id去删除
		solrServer.deleteById(userId+"");
		
		//新建一个文档
		SolrInputDocument document = new SolrInputDocument();
		
		//添加字段
		document.addField("id", user.getUserId());
		document.addField("user_nick", user.getUserNick());
		document.addField("user_gender", user.getUserGender());
		document.addField("user_job", user.getUserJob());
		document.addField("user_hometown", user.getUserHometown());
		document.addField("user_desc", user.getUserDesc());
		document.addField("user_pic_group", user.getUserPicGroup());
		document.addField("user_pic_filename", user.getUserPicFilename());
		
		//使用solrserver对象执行文档的添加操作
		solrServer.add(document);
		solrServer.commit();
	}

	@Override
	public User getUserById(Integer userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public ArrayList<Map<String, String>> getDataFromSolrIndex(String keywords) throws SolrServerException {
		
		//创建solrquery对象
		SolrQuery query = new SolrQuery();
		
		//设置查询关键词
		query.setQuery(keywords);
		
		//设置要查询的默认字段
		query.set("df", "user_keywords");
		
		//开启高亮显示
		query.setHighlight(true);
		
		//添加需要高亮显示的字段
		query.addHighlightField("user_nick");
		query.addHighlightField("user_gender");
		query.addHighlightField("user_job");
		query.addHighlightField("user_hometown");

		//设置高亮显示需要的html标签
		query.setHighlightSimplePre("<span style='color:red'>");
		query.setHighlightSimplePost("</span>");
		
		//执行查询
		QueryResponse response = solrServer.query(query);
		
		//获取查询结果
		SolrDocumentList results = response.getResults();
		
		//获取附加了高亮显示的HTML标签的结果
		Map<String, Map<String, List<String>>> highlightingMap = response.getHighlighting();
		
		//最终的的数据集合
		ArrayList<Map<String, String>> finalResult = new ArrayList<>();
		
		//遍历解析查询结果和高亮结果
		for (SolrDocument solrDocument : results) {
			
			//每一条Document对应一条查询记录
			Map<String, String> finalMap = new HashMap<>();
			
			//获取当前文档的id值
			String id = solrDocument.getFieldValue("id")+"";
			
			//遍历SolrDocument中的所有字段
			Collection<String> fieldNames = solrDocument.getFieldNames();
			for (String fieldName : fieldNames) {
				
				//排除_version_字段
				if ("_version_".equals(fieldName)) {
					continue ;
				}
				
				//获取字段值
				String fieldValue = solrDocument.getFieldValue(fieldName)+"";
				
				//加入map中
				finalMap.put(fieldName, fieldValue);
				
				//根据当前文档的id尝试从高亮的map中获取对应的数据
				Map<String, List<String>> highMap = highlightingMap.get(id);
				List<String> highList = highMap.get(fieldName);
				
				//判断highList是否有效
				if (highList != null && highList.size() > 0) {
					//如果有效则获取添加了高亮标签的字段值
					String highRecord = highList.get(0);
					
					//以字段名为键，以高亮标签的字段值为值，存入map中，覆盖原值
					finalMap.put(fieldName, highRecord);
				} 	
			}
			
			//把finalMap存入list中
			finalResult.add(finalMap);
			
		}
		
		return finalResult;
	}

	
	
	
}