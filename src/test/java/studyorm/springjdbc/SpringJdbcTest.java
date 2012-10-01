package studyorm.springjdbc;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.AbstractDataOperationsTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/application-context-jdbc.xml",
		"/bean-jdbc-querydsl.xml"})
@TransactionConfiguration
@Transactional
public class SpringJdbcTest extends AbstractDataOperationsTest {
}
