package th.team.stock.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "th.team.stock.api",
    "th.team.stock.services"
})
@EnableJpaRepositories(basePackages = "th.team.stock.repositories")
@EntityScan(basePackages = "th.team.stock.models")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}