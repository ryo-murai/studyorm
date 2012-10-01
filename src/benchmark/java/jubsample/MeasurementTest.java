package jubsample;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;

public class MeasurementTest {
    @SuppressWarnings("deprecation")
    @Rule
    public org.junit.rules.MethodRule rule = new BenchmarkRule();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Thread.sleep(1000);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Thread.sleep(2000);
    }

    @Before
    public void setUp() throws Exception {
        Thread.sleep(300);
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(500);
    }

    @Test
    public void test0() throws Exception {
    }

    @Test
    public void test70() throws Exception {
        Thread.sleep(70);
    }
}
