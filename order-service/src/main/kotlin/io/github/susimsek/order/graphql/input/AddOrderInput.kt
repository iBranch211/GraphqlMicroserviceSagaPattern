package io.github.susimsek.order.graphql.input

data class AddOrderInput(
    var userId: String,
    var productId: String
)
