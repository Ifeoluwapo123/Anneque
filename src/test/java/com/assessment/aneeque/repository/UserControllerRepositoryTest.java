package com.assessment.aneeque.repository;

import com.assessment.aneeque.Gender;
import com.assessment.aneeque.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserControllerRepositoryTest {

    @Autowired
    private UserRepository userRepositoryTest;

    private User user;

    @AfterEach
    void tearDown() {
        userRepositoryTest.deleteAll();
    }

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("Adeyinka Adewale")
                .email("adeyinkadewale@gmail.com")
                .gender(Gender.MALE.name())
                .password("uuuu")
                .build();

        userRepositoryTest.save(user);
    }

    @Test
    void findByUsername() {
        userRepositoryTest.save(user);
        assertThat(userRepositoryTest.findByUsername(user.getUsername()).isPresent()).isTrue();
        assertThat(userRepositoryTest.findByUsername("user").isPresent()).isFalse();
    }

    @Test
    void findByEmail() {
        userRepositoryTest.save(user);
        assertThat(userRepositoryTest.findByEmail(user.getEmail()).isPresent()).isTrue();
        assertThat(userRepositoryTest.findByEmail("user@gmail.com").isPresent()).isFalse();
    }

    @Test
    void findByUsernameAndEmail() {
        userRepositoryTest.save(user);
        assertThat(userRepositoryTest.findByUsernameOrEmail(user.getUsername(), user.getEmail()).isPresent()).isTrue();
        assertThat(userRepositoryTest.findByUsernameOrEmail("user", user.getEmail()).isPresent()).isTrue();
        assertThat(userRepositoryTest.findByUsernameOrEmail(user.getUsername(), "user@gmail.com").isPresent()).isTrue();
    }

    @Test
    void findByPassword() {
        userRepositoryTest.save(user);
        assertThat(userRepositoryTest.findByPassword(user.getPassword()).isPresent()).isTrue();
    }
}