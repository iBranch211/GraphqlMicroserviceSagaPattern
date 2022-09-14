package io.github.susimsek.order.bootstrap

import io.github.susimsek.order.model.Product
import io.github.susimsek.order.repository.ProductRepository
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
internal class DataInitializr(private val productRepository: ProductRepository) : CommandLineRunner {

    override fun run(args: Array<String>) {
        productRepository
            .deleteAll()
            .thenMany<Any>(
                Flux
                    .just(
                        Product(id = "1", productId = "1", price = 50.00F),
                        Product(id = "2", productId = "2", price = 75.00F),
                        Product(id = "3", productId = "3", price = 100.00F),
                        Product(id = "4", productId = "4", price = 500.00F),
                        Product(id = "5", productId = "5", price = 100.00F),
                    )
                    .flatMap(productRepository::save)
            )
            .subscribe()
    }
}
