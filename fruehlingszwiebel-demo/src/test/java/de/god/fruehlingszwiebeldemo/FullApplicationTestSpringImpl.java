package de.god.fruehlingszwiebeldemo;

import de.god.fruehlingszwiebeldemo.api.car.CarService;
import de.god.fruehlingszwiebeldemo.domain.FullApplicationTest;
import de.god.fruehlingszwiebeldemo.domain.car.CarRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This is an implementation of the FullApplicationTest using the Spring-Services.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FullApplicationTestSpringImpl.FullApplicationTestSpringConfig.class)
public class FullApplicationTestSpringImpl extends FullApplicationTest {

    @Configuration
    @ComponentScan(basePackages = "de.god.fruehlingszwiebeldemo")
    @EnableJpaRepositories(basePackages = "de.god.fruehlingszwiebeldemo.repository", considerNestedRepositories = true)
    public static class FullApplicationTestSpringConfig {

    }
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Override
    protected CarRepository getCarRepository() {
        return carRepository;
    }

    @Override
    protected CarService getCarService() {
        return carService;
    }


    public FullApplicationTestSpringImpl() {
    }
}
