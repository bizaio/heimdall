package io.biza.heimdall.admin;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = "io.biza.heimdall.data.persistence.model")
@EnableJpaRepositories(basePackages = "io.biza.heimdall.persistence.repository")
@EnableTransactionManagement
public class RepositoryConfiguration {


}
