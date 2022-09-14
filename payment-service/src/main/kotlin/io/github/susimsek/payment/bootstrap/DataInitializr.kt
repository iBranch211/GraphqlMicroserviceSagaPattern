package io.github.susimsek.payment.bootstrap

import io.github.susimsek.payment.model.User
import io.github.susimsek.payment.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
@ConditionalOnProperty(
    value = ["command.line.runner.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
internal class DataInitializr(private val userRepository: UserRepository) : CommandLineRunner {

    override fun run(args: Array<String>) {
        userRepository
            .deleteAll()
            .thenMany<Any>(
                Flux
                    .just(
                        User(id = "1", userId = "1", balance = 100.00F),
                        User(id = "2", userId = "2", balance = 200.00F),
                        User(id = "3", userId = "3", balance = 500.00F),
                        User(id = "4", userId = "4", balance = 1000.00F),
                        User(id = "5", userId = "5", balance = 2000.00F)
                    )
                    .flatMap(userRepository::save)
            )
            .subscribe()
    }
}
