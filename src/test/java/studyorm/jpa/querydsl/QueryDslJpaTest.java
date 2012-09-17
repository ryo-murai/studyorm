package studyorm.jpa.querydsl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.AbstractDtoTest;
import studyorm.jpa.models.Customer;
import studyorm.jpa.models.Order;
import studyorm.jpa.models.QCustomer;
import studyorm.jpa.models.QOrder;

import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/application-context-jpa.xml", "/dto-jpa-querydsl.xml"})
@TransactionConfiguration
@Transactional
public class QueryDslJpaTest extends AbstractDtoTest {
	@PersistenceContext
    private EntityManager em;

	@Test
	public void testQuerySingle() {
		
        // "select c from Customer c where c.name = :customerName"
		JPQLQuery query = new JPAQuery(em, EclipseLinkTemplates.DEFAULT);
		QCustomer c = QCustomer.customer;
		Customer customer = 
			query.from(c)
				.where(c.name.eq("martin fowler"))
				.uniqueResult(c);
		assertEquals("martin fowler", customer.getName());
	}

	@Test
	public void testQueryMany() {

        // "select o from Order o join o.customer c where c.name = :customerName"
		JPQLQuery query = new JPAQuery(em, HQLTemplates.DEFAULT);
		QOrder o = QOrder.order;
		List<Order> orders =
		query.from(o)
			.where(o.customer.name.eq("martin fowler"))
			.list(o);
        assertEquals(4, orders.size());
        assertEquals("martin fowler", orders.get(0).getCustomer().getName());
	}
}
