package com.my_virtual_space.staffing;

import com.my_virtual_space.staffing.repositories.GenericRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaRepositories(repositoryBaseClass = GenericRepositoryImpl.class)
public class StaffingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffingApplication.class, args);
    }

}


