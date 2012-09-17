package studyorm;

import javax.sql.DataSource;

import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

@Component
public class TestHelper {

	@Autowired
	private DataSource dataSource;

	@SuppressWarnings("deprecation")
	private org.springframework.jdbc.core.simple.SimpleJdbcTemplate getTemplate() {
		// https://jira.springsource.org/browse/SPR-9235
		// this is planed to be 3.2M2
		return new org.springframework.jdbc.core.simple.SimpleJdbcTemplate(
				dataSource);
	}

	public void runSqlScriptIgnoreError(Resource script) {
		runSqlScript(script, true);
	}

	public void runSqlScript(Resource script) {
		runSqlScript(script, false);
	}

	public void runSqlScript(Resource script, boolean continueOnFailure) {
		SimpleJdbcTestUtils.executeSqlScript(getTemplate(), script, continueOnFailure);
	}

	public static <T> TestRule getLoggingRule(Class<T> loggerSourceType) {
		return getLoggingRule(LoggerFactory.getLogger(loggerSourceType));
	}

	public static TestRule getLoggingRule(final Logger logger) {
		return new TestWatcher() {
			@Override
			protected void starting(Description description) {
				super.starting(description);
				logger.info("#### test case: " + description.getMethodName()
						+ " is starting #####");
			}
		};
	}
}
