package studyorm.jpa.querydsl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import studyorm.DataOperations;
import studyorm.jpa.JpaOperations;
import studyorm.jpa.models.QCustomer;
import studyorm.jpa.models.QOrder;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.impl.JPAQuery;

public class QueryDslJpaOperations extends JpaOperations
        implements
        DataOperations {
    @Autowired
    private JPQLTemplates jpqlTemplates;

    @Override
    public String queryCustomerNameByPK(Long id) {
        QCustomer c = QCustomer.customer;
        return query().from(c).where(c.id.eq(id)).singleResult(c.name);
    }

    @Override
    public List<String> queryOrderItemsByCustomerEmail(String customerEmail) {
        QOrder o = QOrder.order;
        return query().from(o).where(o.customer.email.eq(customerEmail))
                .list(o.item);
    }

    private JPQLQuery query() {
        return new JPAQuery(getEntityManager(), jpqlTemplates);
    }
}
