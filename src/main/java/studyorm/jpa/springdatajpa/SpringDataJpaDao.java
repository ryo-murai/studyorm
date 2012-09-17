package studyorm.jpa.springdatajpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import studyorm.DataAccessObject;
import studyorm.jpa.models.Customer;
import studyorm.jpa.models.Order;
import studyorm.jpa.springdatajpa.repositories.CustomerRepository;
import studyorm.jpa.springdatajpa.repositories.OrderRepository;

public class SpringDataJpaDao implements DataAccessObject {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public void insert(Long customerId, String customerName,
			String customerEmail, String orderItem, Date orderedDate) {

		Customer customer = customerRepository.save(new Customer(customerId, customerName, customerEmail));
		orderRepository.save(new Order(orderItem, orderedDate, customer));
	}

	@Override
	public void deleteCustomerByPK(Long id) {
		customerRepository.delete(id);
	}

	@Override
	public int deleteOrdersByNameStartWith(String orderNamePrefix) {
    return orderRepository.deleteItemStartingWith(orderNamePrefix);
	}

	@Override
	public void updateCustomerNameByPk(Long id, String newCustomerName) {
		Customer customer = customerRepository.findOne(id);
		customer.setName(newCustomerName);
		customerRepository.save(customer);
	}

	@Override
	public int updateOrdersItemOlderThan(Date orderedDate, String newOrderItem) {
		return orderRepository.SetItemBefore(orderedDate, newOrderItem);
	}

	@Override
	public String queryCustomerNameByPK(Long id) {
		Customer customer = customerRepository.findOne(id);
		return customer != null ? customer.getName() : null;
	}

	@Override
	public List<String> queryOrderItemsByCustomerEmail(String customerEmail) {
		List<Order> orders = orderRepository.findByCustomerEmail(customerEmail);
		List<String> items = new ArrayList<String>();
		for(Order order : orders) {
			items.add(order.getItem());
		}
		return items;
	}
}
