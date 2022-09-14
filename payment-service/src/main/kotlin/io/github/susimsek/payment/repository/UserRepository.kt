package io.github.susimsek.payment.repository

import io.github.susimsek.payment.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : ReactiveMongoRepository<User, String>, ReactiveQuerydslPredicateExecutor<User>
