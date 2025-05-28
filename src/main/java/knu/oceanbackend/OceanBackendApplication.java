package knu.oceanbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OceanBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanBackendApplication.class, args);
    }

}
