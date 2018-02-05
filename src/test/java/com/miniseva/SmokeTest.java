package com.miniseva;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.miniseva.website.HomeController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private HomeController homeController;

    /**
     * Sanity check to ensure that the context is creating controllers.
     *
     * @throws Exception
     */
    @Test
    public void contextLoads() throws Exception {
        assertThat(homeController).isNotNull();
    }

}
