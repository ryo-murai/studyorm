package studyorm.jpa.springdatajpa.repositories;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import studyorm.jpa.models.Customer;

public class CustomerSpecifications {
	public static Specification<Customer> name(final String name) {
		return new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(root.getModel().getSingularAttribute("name", String.class)), name);
			}
		};
	}
}
