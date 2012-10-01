package studyorm.jpa.springdatajpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static studyorm.jpa.springdatajpa.repositories.CustomerSpecifications.name;
import static studyorm.jpa.springdatajpa.repositories.OrderSpecifications.isOrderedByCustomerName;
import static studyorm.jpa.springdatajpa.repositories.OrderSpecifications.isOrderedInPastDays;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.TestHelper;
import studyorm.jpa.models.Customer;
import studyorm.jpa.models.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/application-context-jpa.xml")
@TransactionConfiguration
@Transactional
public class SpringDataJpaRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestHelper helper;

    @Value("${classpath:/sql/create-tables.sql}")
    private Resource createTablesSql;

    @Value("${classpath:/sql/insert-records.sql}")
    private Resource insertRecordsSql;

    @Value("${classpath:/sql/drop-tables.sql}")
    private Resource dropTablesSql;

    @Rule
    public TestRule watcher = TestHelper
            .getLoggingRule(SpringDataJpaRepositoryTest.class);

    @BeforeTransaction
    public void setupDatabase() {
        helper.runSqlScriptIgnoreError(createTablesSql);
        helper.runSqlScript(insertRecordsSql);
    }

    @AfterTransaction
    public void tearDownDatabase() {
        helper.runSqlScriptIgnoreError(dropTablesSql);
    }

    @Test
    public void testOrderDateAfter() {
        List<Order> orders = orderRepository.findByDateAfter(new Date(0),
                new Sort("id"));
        assertEquals(8, orders.size());
        assertEquals("martin fowler", orders.get(0).getCustomer().getName());
    }

    @Test
    public void testOrderOfCustomerName() {
        List<Order> orders = orderRepository
                .findByCustomerName("martin fowler");
        assertEquals(4, orders.size());
        Order anOrder = orders.get(0);
        assertEquals("martin fowler", anOrder.getCustomer().getName());
        assertNotNull(anOrder.getItem());
    }

    @Test
    public void testSpecificationOrderExecution() {
        List<Order> orders = orderRepository.findAll(
                isOrderedInPastDays(36500), new Sort("id"));
        assertEquals(8, orders.size());
        Customer relation = orders.get(0).getCustomer();
        assertEquals("martin fowler", relation.getName());
    }

    @Test
    public void testSpecificationOrderJoinExecution() {
        List<Order> orders = orderRepository.findAll(
                isOrderedByCustomerName("martin fowler"), new Sort("id"));
        assertEquals(4, orders.size());
        assertEquals("martin fowler", orders.get(0).getCustomer().getName());
    }

    @Test
    public void testSpecificationCustomerExecution() {
        Customer customer = customerRepository.findOne(name("martin fowler"));
        assertNotNull(customer);
        assertEquals("martin fowler", customer.getName());
    }
}
