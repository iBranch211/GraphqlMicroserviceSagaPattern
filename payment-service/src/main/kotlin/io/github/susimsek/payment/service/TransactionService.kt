package io.github.susimsek.payment.service

import io.github.susimsek.mscommonweb.graphql.exception.ResourceNotFoundException
import io.github.susimsek.payment.model.Transaction
import io.github.susimsek.payment.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {

    fun getAllTransactions(): Flow<Transaction> {
        return transactionRepository.findAll().asFlow()
    }

    suspend fun getTransaction(id: String): Transaction {
        return transactionRepository.findById(id)
            .switchIfEmpty(Mono.error((ResourceNotFoundException("Transaction", "id", id))))
            .awaitSingle()
    }
}
