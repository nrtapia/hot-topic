package com.ntapia.hotoppic.shared.infrastructure.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    "com.ntapia.hotoppic.topic.infraestructure.repository"
)
public class JpaRepositoryConfig {

}
