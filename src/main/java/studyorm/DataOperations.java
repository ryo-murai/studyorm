package studyorm;

import java.util.Date;
import java.util.List;

public interface DataOperations {
	public abstract void insert(Long customerId, String customerName,
			String customerEmail, String orderItem, Date orderedDate);

	public abstract void deleteCustomerByPK(Long id);

	public abstract int deleteOrdersByNameStartWith(String orderNamePrefix);

	public abstract void updateCustomerNameByPk(Long id, String newCustomerName);

	public abstract int updateOrdersItemOlderThan(Date orderedDate,
			String newOrderItem);

	public abstract String queryCustomerNameByPK(Long id);

	public abstract List<String> queryOrderItemsByCustomerEmail(
			String customerEmail);
}