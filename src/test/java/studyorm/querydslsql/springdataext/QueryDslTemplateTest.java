package studyorm.querydslsql.springdataext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static studyorm.querydslsql.springdataext.QueryDslTemplateUtil.mapping;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.AbstractDataOperationsTest;
import studyorm.querydslsql.beans.Customer;
import studyorm.querydslsql.beans.QCustomer;
import studyorm.querydslsql.beans.QTorder;

import com.mysema.query.sql.SQLQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/application-context-jdbc.xml", "/bean-jdbc-springext-querydsl.xml"})
@TransactionConfiguration
@Transactional
public class QueryDslTemplateTest extends AbstractDataOperationsTest {
	@Autowired
	private QueryDslJdbcTemplate template;

	@Test
	public void testSingleCustomerQuery() throws Throwable {
		final QCustomer customer = QCustomer.customer;
		SQLQuery getCustomer =
				template.newSqlQuery()
				.from(customer)
				.where(customer.custId.eq(43509845L));
		
		Customer aCustomer = 
				template.queryForObject(getCustomer, mapping(Customer.class, customer));

		assertEquals("martin fowler", aCustomer.getName());
	}

	@Test
	public void testJoinQuery() throws Throwable {
		final QTorder order = QTorder.torder;
		QCustomer customer = QCustomer.customer;
		SQLQuery getOrders =
			template.newSqlQuery()
				.from(order)
				.innerJoin(customer)
					.on(order.custId.eq(customer.custId))
				.where(customer.name.eq("martin fowler"));

		List<String> orderItems =
		template.query(getOrders, mapping(String.class, order.item));
		assertEquals(4, orderItems.size());
		assertThat(orderItems, hasItems("PoEAA", "Domain Specific Languages", "Refactoring", "UML Distilled"));
	}
}
