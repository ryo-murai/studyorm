package studyorm.querydslsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import studyorm.DataOperations;
import studyorm.querydslsql.beans.Customer;
import studyorm.querydslsql.beans.QCustomer;
import studyorm.querydslsql.beans.QTorder;
import studyorm.querydslsql.beans.Torder;
import static studyorm.querydslsql.QueryDslSintaxSupport.*;

public class QueryDslSqlOperations implements DataOperations {
	@Autowired
	private DataSource dataSource;

	private static final QTorder order = QTorder.torder;
	private static final QCustomer customer = QCustomer.customer;

	@Override
	public void insert(Long customerId, String customerName,
			String customerEmail, String orderItem, Date orderedDate) {
		Customer newCustomer = new Customer();
		newCustomer.setCustId(customerId);
		newCustomer.setName(customerName);
		newCustomer.setEmail(customerEmail);
		Torder newOrder = new Torder();
		newOrder.setCustId(customerId);
		newOrder.setDate(toSql(orderedDate));
		newOrder.setItem(orderItem);
		// orderId is automatically identified by database.
		
		Connection con = getConnection();
		try {
			queryDsl(con).insertInto(customer)
				.populate(newCustomer)
				.execute();
	
			queryDsl(con).insertInto(order)
				.populate(newOrder)
				.execute();
		} finally {
			close(con);
		}
	}

	@Override
	public void deleteCustomerByPK(Long id) {
		Connection con = getConnection();
		try {
			queryDsl(con)
				.delete(customer)
				.where(customer.custId.eq(id))
				.execute();
		} finally {
			close(con);
		}
	}

	@Override
	public int deleteOrdersByNameStartWith(String orderNamePrefix) {
		Connection con = getConnection();
		try {
			return (int)
				queryDsl(con).delete(order)
					.where(order.item.startsWith(orderNamePrefix))
					.execute();
		} finally {
			close(con);
		}
	}

	@Override
	public void updateCustomerNameByPk(Long id, String newCustomerName) {
		Connection con = getConnection();
		try {
			queryDsl(con).update(customer)
				.where(customer.custId.eq(id))
				.set(customer.name, newCustomerName)
				.execute();
		} finally {
			close(con);
		}
	}

	@Override
	public int updateOrdersItemOlderThan(Date orderedDate, String newOrderItem) {
		Connection con = getConnection();
		try {
			return (int)
			queryDsl(con).update(order)
				.where(order.date.before(toSql(orderedDate)))
				.set(order.item, newOrderItem)
				.execute();
		} finally {
			close(con);
		}
	}

	@Override
	public String queryCustomerNameByPK(Long id) {
		Connection con = getConnection();
		try {
			return queryDsl(con)
					.queryFrom(customer)
					.where(customer.custId.eq(id))
					.uniqueResult(customer.name);
		} finally {
			close(con);
		}
	}

	@Override
	public List<String> queryOrderItemsByCustomerEmail(String customerEmail) {
		Connection con = getConnection();
		try {
			return queryDsl(con)
					.queryFrom(order)
						.innerJoin(customer)
						.on(order.custId.eq(customer.custId))
					.where(customer.email.eq(customerEmail))
					.list(order.item);
		} finally {
			close(con);
		}
	}

	private Connection getConnection() {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private java.sql.Date toSql(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}
}
