package com.slog2.example.todo

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TodoService {
  fun getTodo(id: Int) : Mono<Todo>
  fun searchTodo(nameFilter: String) : Flux<Todo>
  fun createTodo(todoMono: Mono<Todo>) : Mono<Todo>
  fun updateTodo(id: Int, todoMono: Mono<Todo>) : Mono<Todo>
  fun deleteTodo(id: Int) : Mono<Boolean>
}
