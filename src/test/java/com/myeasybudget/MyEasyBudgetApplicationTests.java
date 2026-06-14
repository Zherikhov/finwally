package com.myeasybudget;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.myeasybudget.user.application.AuthService;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
                + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
        "app.security.jwt.secret=test-secret-key-that-is-at-least-32-bytes-long"
})
class MyEasyBudgetApplicationTests {

    @MockitoBean
    AuthService authService;

    @Test
    void contextLoads() {
    }

}
