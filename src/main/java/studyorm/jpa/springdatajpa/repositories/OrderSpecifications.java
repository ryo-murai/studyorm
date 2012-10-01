package studyorm.jpa.springdatajpa.repositories;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import studyorm.jpa.models.Customer;
import studyorm.jpa.models.Order;

public class OrderSpecifications {
	public static Specification<Order> isOrderedByCustomerName(
			final String customerName) {
		return new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				Join<Order, Customer> join = root.join(root.getModel()
						.getSingularAttribute("customer", Customer.class));
				return cb.equal(join.get("name"), customerName);
			}
		};
	}

	public static Specification<Order> isOrderedInPastDays(final long days) {
		return new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				long previousPastDays = System.currentTimeMillis() - 36000000
						* 24 * days;
				return cb.greaterThanOrEqualTo(
						root.get(root.getModel().getSingularAttribute("date",
								Date.class)), new Date(previousPastDays));
			}
		};
	}
}
