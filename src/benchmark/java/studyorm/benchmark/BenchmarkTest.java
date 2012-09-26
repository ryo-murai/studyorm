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
@TransactionConfiguration(defaultRollback=false)
@Transactional
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "build/reports/benchmarks/benchmark")
@BenchmarkHistoryChart(filePrefix = "build/reports/benchmarks/BenchmarkTest", labelWith = LabelType.CUSTOM_KEY, maxRuns = 6)
public class BenchmarkTest {

	@Autowired
	protected DataAccessObject targetDto;

	@SuppressWarnings("deprecation")
	@Rule
	public org.junit.rules.MethodRule rule = new BenchmarkRule();

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
		int result = targetDto.deleteOrdersByNameStartWith("ItemName00");
	}

	@Test
	public void update() {
		targetDto.updateCustomerNameByPk(12114210L, "Sir. Kent Beck");
	}

	@Test
	public void updateBatch() {
		LocalDate date = new LocalDate(2012, 8, 15);
		String newOrderItem = "ItemName99999";

		@SuppressWarnings("unused")
		int result = targetDto.updateOrdersItemOlderThan(date.toDate(), newOrderItem);
		// approx 50% of records of order
	}

	@Test
	public void query() {
		@SuppressWarnings("unused")
		String customerName = targetDto.queryCustomerNameByPK(12114210L);
	}

	@Test
	public void queryMany() {
		@SuppressWarnings("unused")
		List<String> items = targetDto.queryOrderItemsByCustomerEmail("customer20000@example.com");
		// 2 records of order
	}
}
