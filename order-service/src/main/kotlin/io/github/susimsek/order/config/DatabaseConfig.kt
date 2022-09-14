package io.github.susimsek.order.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration(proxyBeanMethods = false)
@EnableReactiveMongoRepositories("io.github.susimsek.order.repository")
class DatabaseConfig
