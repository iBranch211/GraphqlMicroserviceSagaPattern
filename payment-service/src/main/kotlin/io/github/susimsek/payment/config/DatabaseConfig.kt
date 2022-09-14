package io.github.susimsek.payment.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration(proxyBeanMethods = false)
@EnableReactiveMongoRepositories("io.github.susimsek.payment.repository")
class DatabaseConfig
