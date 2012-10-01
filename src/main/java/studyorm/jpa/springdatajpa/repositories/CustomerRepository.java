package studyorm.jpa.springdatajpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import studyorm.jpa.models.Customer;

@Repository
public interface CustomerRepository
		extends
			JpaRepository<Customer, Long>,
			JpaSpecificationExecutor<Customer> {
}
