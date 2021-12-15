package com.my_virtual_space.staffing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableJpaRepositories
@SpringBootApplication
@EnableAspectJAutoProxy
public class StaffingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaffingApplication.class, args);
	}

}
