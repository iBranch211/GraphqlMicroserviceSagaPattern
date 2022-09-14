package io.github.susimsek.mscommonweb.graphql.exception.resolver

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import io.github.susimsek.mscommonweb.graphql.exception.FieldError
import io.github.susimsek.mscommonweb.graphql.exception.ResourceAlreadyExistsException
import io.github.susimsek.mscommonweb.graphql.exception.ResourceNotFoundException
import io.github.susimsek.mscommonweb.graphql.exception.ValidationException
import org.springframework.graphql.client.FieldAccessException
import org.springframework.graphql.execution.DataFetcherExceptionResolver
import org.springframework.graphql.execution.ErrorType
import reactor.core.publisher.Mono
import javax.validation.ConstraintViolationException

class ReactiveGraphqlExceptionResolver : DataFetcherExceptionResolver {
    override fun resolveException(
        ex: Throwable,
        env: DataFetchingEnvironment
    ): Mono<List<GraphQLError>> {
        return when (ex) {
            is FieldAccessException -> {
                val response = ex.response
               val errors = response.errors.map {
                       error ->
                   val extensions = error.extensions
                   val classification = extensions["classification"]
                   val errorType = ErrorType.values()
                       .firstOrNull { it.name == classification } ?: ErrorType.INTERNAL_ERROR
                  GraphqlErrorBuilder.newError(env)
                       .message(error.message).errorType(errorType).build()
               }
                Mono.just(errors)
            }

            is ResourceNotFoundException -> {
                val error = GraphqlErrorBuilder.newError(env)
                    .message(ex.message).errorType(ErrorType.NOT_FOUND).build()
                Mono.just(listOf(error))
            }

            is ResourceAlreadyExistsException -> badRequestException(ex, env)

            is ValidationException -> badRequestException(ex, env)

            is ConstraintViolationException -> {
                val errors = ex.constraintViolations.map {
                    FieldError(
                    property = it.propertyPath.reduce { _, second -> second }.toString(),
                    message = it.message
                )
                }

                val error = GraphqlErrorBuilder.newError(env)
                    .message("Invalid Input").errorType(graphql.ErrorType.ValidationError)
                    .extensions(mapOf("errors" to errors))
                    .build()
                Mono.just(listOf(error))
            }

            else -> {
                Mono.empty()
            }
        }
    }

    private fun badRequestException(ex: Throwable, env: DataFetchingEnvironment): Mono<List<GraphQLError>> {
        val error = GraphqlErrorBuilder.newError(env)
            .message(ex.message).errorType(ErrorType.BAD_REQUEST).build()
        return Mono.just(listOf(error))
    }
}
