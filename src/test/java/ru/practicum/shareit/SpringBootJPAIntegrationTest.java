package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
/*@SpringBootTest(classes = {ShareItApp.class, H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:/h2datatest.sql", "classpath:/SampleDataTESTDB.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)*/

public class SpringBootJPAIntegrationTest {

 /*   @Autowired
    private UserRepository userRepository;

    @Test
    //@Sql({"classpath:/h2datatest.sql", "classpath:/SampleData.sql"})
    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
        List<User> all = userRepository.findAll();
        assertThat(all)
                .isNotEmpty();
    }
*/
}