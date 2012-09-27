package studyorm.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import studyorm.DataOperations;
import studyorm.jpa.models.Customer;
import studyorm.jpa.models.Order;

public class JpaOperations implements DataOperations {
    @PersistenceContext
    private EntityManager em;

    protected EntityManager getEntityManager() {
    	return em;
    }
    
    @Override
	public void insert(Long customerId, String customerName, String customerEmail, String orderItem, Date orderedDate) {
    	Customer customer = new Customer(customerId, customerName, customerEmail);
    	em.persist(customer);

    	Order order = new Order(orderItem, orderedDate, customer);
    	em.persist(order);
    	em.flush();
	}

    @Override
	public void deleteCustomerByPK(Long id) {
		Customer customer = em.find(Customer.class, id);
		em.remove(customer);
    	em.flush();
	}

    @Override
	public int deleteOrdersByNameStartWith(String orderNamePrefix) {
		// from JPA2.1, we can do batch update through CriteriaBuilder
		// but it is not supported by the current Hibernate stable release.
		return 
			em.createQuery("delete Order o where o.item like :itemPrefix")
			.setParameter("itemPrefix", orderNamePrefix + "%")
			.executeUpdate();
	}

	@Override
	public void updateCustomerNameByPk(Long id, String newCustomerName) {
		Customer customer = em.find(Customer.class, id);
		customer.setName(newCustomerName);
		em.merge(customer);
    	em.flush();
	}
	
	@Override
	public int updateOrdersItemOlderThan(Date orderedDate, String newOrderItem) {
		// from JPA2.1, we can do batch update through CriteriaBuilder
		// but it is not supported by the current Hibernate stable release.
		return
			em.createQuery("update Order o set o.item = :itemName where o.date < :orderedDate")
			.setParameter("itemName", newOrderItem)
			.setParameter("orderedDate", orderedDate)
			.executeUpdate();
	}
	
	@Override
	public String queryCustomerNameByPK(Long id) {
		Customer customer = em.find(Customer.class, id);
		return customer != null ? customer.getName() : null;
	}

	@Override
	public List<String> queryOrderItemsByCustomerEmail(String customerEmail) {
    	List<Order> orders = 
            em.createQuery("select o from Order o join o.customer c where c.email = :emailAddress", Order.class)
                .setParameter("emailAddress", customerEmail)
                .getResultList();

    	List<String> items = new ArrayList<String>();
    	for(Order order : orders) {
    		items.add(order.getItem());
    	}
    	return items;
	}
}
