package studyorm.querydslsql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.AbstractDataOperationsTest;
import studyorm.querydslsql.beans.Customer;
import studyorm.querydslsql.beans.QCustomer;
import studyorm.querydslsql.beans.QTorder;
import static studyorm.querydslsql.QueryDslSintaxSupport.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/application-context-jdbc.xml",
        "/bean-jdbc-querydsl.xml" })
@TransactionConfiguration
@Transactional
public class QueryDslSqlTest extends AbstractDataOperationsTest {
    @Autowired
    private DataSource dataSource;

    private static final QTorder order = QTorder.torder;
    private static final QCustomer customer = QCustomer.customer;

    @Test
    public void testSingleCustomerQuery() throws Throwable {
        Connection con = dataSource.getConnection();
        try {
            Customer aCustomer = queryDsl(con).queryFrom(customer)
                    .where(customer.custId.eq(43509845L))
                    .uniqueResult(customer);
            assertNotNull(aCustomer);
            assertEquals("martin fowler", aCustomer.getName());
        } finally {
            con.close();
        }
    }

    @Test
    public void testJoinQuery() throws Throwable {
        Connection con = dataSource.getConnection();
        try {
            List<String> orderItems = queryDsl(con).queryFrom(order)
                    .innerJoin(customer).on(order.custId.eq(customer.custId))
                    .where(customer.name.eq("martin fowler")).list(order.item);

            assertEquals(4, orderItems.size());
            assertThat(
                    orderItems,
                    hasItems("PoEAA", "Domain Specific Languages",
                            "Refactoring", "UML Distilled"));
        } finally {
            con.close();
        }
    }
}
