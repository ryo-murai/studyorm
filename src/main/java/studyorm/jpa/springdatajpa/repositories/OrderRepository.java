package studyorm.jpa.springdatajpa.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import studyorm.jpa.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	List<Order> findByDateAfter(Date date, Sort sort);

	List<Order> findByDateBefore(Date date);

	List<Order> findByCustomerName(String customerName);

	List<Order> findByCustomerEmail(String customerEmail);

	List<Order> findByItemStartingWith(String itemPrefix);

	@Modifying
	@Query("delete Order o where o.item like CONCAT(:prefix, '%')")
	int deleteItemStartingWith(@Param("prefix")String prefix);
	
	@Modifying
	@Query("update Order o set o.item = :itemName where o.date < :orderedDate")
	int SetItemBefore(@Param("orderedDate")Date date, @Param("itemName")String newItemName);
}
