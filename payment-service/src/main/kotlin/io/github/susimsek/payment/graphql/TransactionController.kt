package io.github.susimsek.payment.graphql

import io.github.susimsek.payment.model.Transaction
import io.github.susimsek.payment.service.TransactionService
import kotlinx.coroutines.flow.toList
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Controller
@Validated
class TransactionController(
    private val transactionService: TransactionService
) {

    @QueryMapping
    suspend fun transactions(): List<Transaction> {
        return transactionService.getAllTransactions().toList()
    }

    @QueryMapping
    suspend fun transaction(@Argument @NotBlank id: String): Transaction {
        return transactionService.getTransaction(id)
    }
}
