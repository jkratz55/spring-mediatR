package io.jkratz.spring.mediator

interface QueryHandler<in TQuery, TResponse> where TQuery: Query<TResponse> {

    fun handle(query: TQuery): TResponse
}