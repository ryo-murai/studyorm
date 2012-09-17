package studyorm.querydslsql.springdataext;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.data.jdbc.query.SqlDeleteCallback;
import org.springframework.data.jdbc.query.SqlInsertCallback;
import org.springframework.data.jdbc.query.SqlUpdateCallback;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

import studyorm.DataAccessObject;
import studyorm.querydslsql.beans.Customer;
import studyorm.querydslsql.beans.QCustomer;
import studyorm.querydslsql.beans.QTorder;
import studyorm.querydslsql.beans.Torder;
import static studyorm.querydslsql.springdataext.QueryDslTemplateUtil.*;

public class QueryDslTemplateDto implements DataAccessObject {
	@Autowired
	private QueryDslJdbcTemplate template;

	private static final QTorder order = QTorder.torder;
	private static final QCustomer customer = QCustomer.customer;

	@Override
	public void insert(Long customerId, String customerName,
			String customerEmail, String orderItem, Date orderedDate) {
		final Customer newCustomer = new Customer();
		newCustomer.setCustId(customerId);
		newCustomer.setName(customerName);
		newCustomer.setEmail(customerEmail);
		final Torder newOrder = new Torder();
		newOrder.setCustId(customerId);
		newOrder.setDate(toSql(orderedDate));
		newOrder.setItem(orderItem);
		// orderId is automatically identified by database.

		template.insert(customer, new SqlInsertCallback() {
			@Override
			public long doInSqlInsertClause(SQLInsertClause insert) {
				return insert.populate(newCustomer).execute();
			}
		});

		template.insert(order, new SqlInsertCallback(){
			@Override
			public long doInSqlInsertClause(SQLInsertClause insert) {
				return insert.populate(newOrder).execute();
			}
		});
	}

	@Override
	public void deleteCustomerByPK(final Long id) {
		template.delete(customer, new SqlDeleteCallback() {
			@Override
			public long doInSqlDeleteClause(SQLDeleteClause delete) {
				return delete.where(customer.custId.eq(id)).execute();
			}
		});
	}

	@Override
	public int deleteOrdersByNameStartWith(final String orderNamePrefix) {
		return (int)
		template.delete(order, new SqlDeleteCallback() {
			@Override
			public long doInSqlDeleteClause(SQLDeleteClause delete) {
				return delete
						.where(order.item.startsWith(orderNamePrefix))
						.execute();
			}
		});
	}

	@Override
	public void updateCustomerNameByPk(final Long id, final String newCustomerName) {
		template.update(customer, new SqlUpdateCallback() {
			@Override
			public long doInSqlUpdateClause(SQLUpdateClause update) {
				return update
						.where(customer.custId.eq(id))
						.set(customer.name, newCustomerName)
						.execute();
			}
		});
	}

	@Override
	public int updateOrdersItemOlderThan(final Date orderedDate, final String newOrderItem) {
		return (int)
		template.update(order, new SqlUpdateCallback() {
			@Override
			public long doInSqlUpdateClause(SQLUpdateClause update) {
				return update
						.where(order.date.before(toSql(orderedDate)))
						.set(order.item, newOrderItem)
						.execute();
			}
		});
	}

	@Override
	public String queryCustomerNameByPK(Long id) {
		SQLQuery query = 
				template.newSqlQuery()
					.from(customer)
					.where(customer.custId.eq(id));

		return template.queryForObject(query, mapping(String.class, customer.name));
	}

	@Override
	public List<String> queryOrderItemsByCustomerEmail(String customerEmail) {
		SQLQuery query = 
				template.newSqlQuery()
					.from(order)
						.innerJoin(customer)
						.on(order.custId.eq(customer.custId))
					.where(customer.email.eq(customerEmail));

		return template.query(query, mapping(String.class, order.item));
	}
}
