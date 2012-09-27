package studyorm.jpa;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.AbstractDataOperationsTest;
import studyorm.jpa.models.Customer;
import studyorm.jpa.models.Order;

@ContextConfiguration(locations={"/application-context-jpa.xml", "/bean-jpa.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@Transactional
public class JpaTest extends AbstractDataOperationsTest {

    @PersistenceContext
    private EntityManager em;

	@Test
	public void testPersist() {
        Customer[] customers = {
        	new Customer(83509845L, "martin fowler", "mfowler@mf.com"),
        	new Customer(90102423L, "kent beck", "kbeck@kent.com")
        };
        for(Customer c : customers) {
        	em.persist(c);
        }
        em.flush();

        Order[] orders = {
        	new Order("PoEAA", new Date(), customers[0]),
        	new Order("Domain Specific Languages", new Date(), customers[0]),
        	new Order("Refactoring", new Date(), customers[0]),
        	new Order("UML Distilled", new Date(), customers[0]),
        	new Order("extreme programming", new Date(), customers[1]),
        	new Order("TDD", new Date(), customers[1]),
        	new Order("Refactoring: Improving the Design of Existing Code", new Date(), customers[1]),
        	new Order("Implementation Patterns", new Date(), customers[1]),
        };
        for(Order o : orders) {
        	em.persist(o);
        }
    }

    @Test
    public void testFind() throws Exception {
        Customer emp = em.find(Customer.class, 43509845L);
        assertEquals("martin fowler", emp.getName());
    }

    @Test
    public void testJpql() throws Exception {
        List<Order> orders =
            em.createQuery("select o from Order o join o.customer c where c.name = :customerName", Order.class)
                .setParameter("customerName", "martin fowler")
                .getResultList();

        assertEquals(4, orders.size());
        assertEquals("martin fowler", orders.get(0).getCustomer().getName());
    }
}
