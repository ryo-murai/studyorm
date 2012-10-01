package studyorm.jpa.springdatajpa;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import studyorm.AbstractDataOperationsTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/application-context-jpa.xml",
		"/bean-jpa-springdatajpa.xml"})
@TransactionConfiguration
@Transactional
public class SpringDataJpaTest extends AbstractDataOperationsTest {

}
