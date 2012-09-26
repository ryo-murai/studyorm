package studyorm.benchmark;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
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
@BenchmarkMethodChart(filePrefix = "build/reports/benchmarks/measurement")
public class MeasurementTest {
	@SuppressWarnings("deprecation")
	@Rule
	public org.junit.rules.MethodRule rule = new BenchmarkRule();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sleep(1000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sleep(2000);
	}

	@Before
	public void setUp() throws Exception {
		sleep(100);
	}

	@After
	public void tearDown() throws Exception {
		sleep(200);
	}

	@BeforeTransaction
	public void beforeTx() {
		sleep(10);
	}
	
	@AfterTransaction
	public void afterTx() {
		sleep(20);
	}

	@BenchmarkOptions
	@Test
	public void test() {
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
