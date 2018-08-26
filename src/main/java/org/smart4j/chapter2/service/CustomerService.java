package org.smart4j.chapter2.service;

import java.util.List;
import java.util.Map;

import org.smart4j.chapter2.helper.DataBaseHelper;
import org.smart4j.chapter2.model.Customer;

public class CustomerService {

	/**
	 * 获取全部客户信息
	 * @return
	 */
	public List<Customer> getCustomerList() {
		String sql = "select * from customer";
		return	DataBaseHelper.queryEntityList(Customer.class, sql, null);
	}
	
	/**
	 * 获取客户
	 * @param id
	 * @return
	 */
	public Customer getCustomer(long id) {
		String sql = "select * from customer where id = ?";
		Object obj = 1;
		return DataBaseHelper.getEntity(Customer.class, sql, obj);

	}
	
	/**
	 * 创建客户
	 * @param map
	 * @return
	 */
	public boolean createCustomer(Map<String, Object> map) {
		return DataBaseHelper.insertEntity(Customer.class, map);
	}
	
	/**
	 * 修改客户信息
	 * @param id
	 * @param map
	 * @return
	 */
	public <T> boolean updateCustomer(long id, Map<String, Object> map) {
		return DataBaseHelper.updateEntity(Customer.class, id, map) > 0;
	}
	
	/**
	 * 删除客户
	 * @param id
	 * @return
	 */
	public <T> boolean deleteCustomer(long id) {
		
		
		return DataBaseHelper.deleteEntity(Customer.class, id);
	}
}


