package studyorm.springjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import studyorm.DataOperations;

public class SpringJdbcOperations implements DataOperations {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void insert(Long customerId, String customerName,
			String customerEmail, String orderItem, Date orderedDate) {
		this.jdbcTemplate.update(
				"INSERT INTO Customer (cust_id, name, email) VALUES (?,?,?)",
				customerId, customerName, customerEmail);
		this.jdbcTemplate.update(
				"INSERT INTO TOrder (item, date, cust_id) VALUES (?,?,?)",
				orderItem, orderedDate, customerId);
	}

	@Override
	public void deleteCustomerByPK(Long id) {
		this.jdbcTemplate.update("DELETE FROM Customer WHERE cust_id = ?", id);
	}

	@Override
	public int deleteOrdersByNameStartWith(String orderNamePrefix) {
		return this.jdbcTemplate.update("DELETE FROM TOrder WHERE item LIKE ?",
				orderNamePrefix + "%");
	}

	@Override
	public void updateCustomerNameByPk(Long id, String newCustomerName) {
		this.jdbcTemplate.update(
				"UPDATE Customer SET name = ? WHERE cust_id = ?",
				newCustomerName, id);
	}

	@Override
	public int updateOrdersItemOlderThan(Date orderedDate, String newOrderItem) {
		return this.jdbcTemplate.update(
				"UPDATE TOrder SET item = ? WHERE date < ?", newOrderItem,
				orderedDate);
	}

	@Override
	public String queryCustomerNameByPK(Long id) {
		Object[] params = {id};
		return this.jdbcTemplate.queryForObject(
				"SELECT c.name FROM Customer c WHERE c.cust_id = ?", params,
				String.class);
	}

	@Override
	public List<String> queryOrderItemsByCustomerEmail(String customerEmail) {
		Object[] params = {customerEmail};
		return this.jdbcTemplate.query("SELECT o.item AS orderItem "
				+ "FROM TOrder o JOIN Customer c ON o.cust_id = c.cust_id "
				+ "WHERE c.email = ?", params, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getNString("orderItem");
			}
		});
	}
}
