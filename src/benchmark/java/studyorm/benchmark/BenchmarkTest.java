package studyorm.benchmark;

import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.DataAccessObject;
import studyorm.TestHelper;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/application-context-proxy.xml")
@TransactionConfiguration
@Transactional
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "build/reports/benchmarks/benchmark")
@BenchmarkHistoryChart(filePrefix = "build/reports/benchmarks/BenchmarkTest", labelWith = LabelType.CUSTOM_KEY, maxRuns = 20)
public class BenchmarkTest {

	@Autowired
	protected DataAccessObject targetDto;

	@Autowired
	protected TestHelper helper;

	@Value("${classpath:/sql/create-tables.sql}")
	private Resource createTablesSql;

	// TODO: less logging configuration
	@Value("${classpath:/sql/insert-6k.sql}")
	private Resource insertRecordsSql;

	@Value("${classpath:/sql/drop-tables.sql}")
	private Resource dropTablesSql;

	@SuppressWarnings("deprecation")
	@Rule
	public org.junit.rules.MethodRule rule = new BenchmarkRule();

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
	public void insert() {
		targetDto.insert(
				8983352L, 
				"test insert", 
				"test@insert.org", 
				"test new order insertion", 
				new Date(1234567L));
	}

	@Test
	public void delete() {
		targetDto.deleteCustomerByPK(98765432L);
	}

	@Test
	public void deleteBatch() {
		@SuppressWarnings("unused")
		int result = targetDto.deleteOrdersByNameStartWith("ItemName0");
	}

	@Test
	public void update() {
		targetDto.updateCustomerNameByPk(12114210L, "Sir. Kent Beck");
	}

	@Test
	public void updateBatch() {
		LocalDate date = new LocalDate();
		String newOrderItem = "ItemName9999";

		@SuppressWarnings("unused")
		int result = targetDto.updateOrdersItemOlderThan(date.toDate(), newOrderItem);		
	}

	@Test
	public void query() {
		@SuppressWarnings("unused")
		String customerName = targetDto.queryCustomerNameByPK(12114210L);
	}

	@Test
	public void queryMany() {
		@SuppressWarnings("unused")
		List<String> items = targetDto.queryOrderItemsByCustomerEmail("customer2000@example.com");
	}
}
