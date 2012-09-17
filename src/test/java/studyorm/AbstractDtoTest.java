package studyorm;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractDtoTest {

	@Autowired
	protected DataAccessObject targetDto;

	@Autowired
	protected TestHelper helper;

	@Value("${classpath:/sql/create-tables.sql}")
	private Resource createTablesSql;

	@Value("${classpath:/sql/insert-records.sql}")
	private Resource insertRecordsSql;

	@Value("${classpath:/sql/drop-tables.sql}")
	private Resource dropTablesSql;

	@Rule
	public TestRule watcher = TestHelper.getLoggingRule(getClass());

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
	public void insertTest() {
		targetDto.insert(
					8983352L, 
					"test insert", 
					"test@insert.org", 
					"test new order insertion", 
					new Date(1234567L));


		// assertion
		List<String> items = 
				targetDto.queryOrderItemsByCustomerEmail("test@insert.org");
		
		assertThat(items.size(), is(1));
		assertThat(items.get(0), is("test new order insertion"));
	}

	@Test
	public void deleteTest() {
		long id = 49494343L;
		targetDto.deleteCustomerByPK(id);
		
		assertThat(targetDto.queryCustomerNameByPK(id), nullValue());
	}

	@Test
	public void deleteBatchTest() {
		int result = targetDto.deleteOrdersByNameStartWith("R");
		assertThat(result, is(2));
		
		result = targetDto.deleteOrdersByNameStartWith("no such items");
		assertThat(result, is(0));
	}
	
	@Test
	public void updateTest() {
		long id = 20102423L;
		String newCustomerName = "Sir. Kent Beck";

		targetDto.updateCustomerNameByPk(id, newCustomerName);
		
		assertThat(targetDto.queryCustomerNameByPK(id), is(newCustomerName));
	}
	
	@Test
	public void updateBatchTest() {
		LocalDate date = new LocalDate(2012, 8, 5);
		String newOrderItem = "NoSQL Distilled";
		int result = targetDto.updateOrdersItemOlderThan(date.toDate(), newOrderItem);

		assertThat(result, is(4));

		List<String> items = targetDto.queryOrderItemsByCustomerEmail("mfowler@mf.com");
		assertThat(items, hasSize(4));
		assertThat(items, is(everyItem(is(newOrderItem))));
	}
	
	@Test
	public void queryTest() {
		String customerName = targetDto.queryCustomerNameByPK(43509845L);
		assertThat(customerName, is("martin fowler"));
	}
	
	@Test
	public void queryManyTest() {
		List<String> items = targetDto.queryOrderItemsByCustomerEmail("kbeck@kent.com");

		assertThat(items, hasSize(4));
		assertThat(items, is(containsInAnyOrder(
			"extreme programming",
			"TDD",
			"Refactoring: Improving the Design of Existing Code",
			"Implementation Patterns")));
	}
}
