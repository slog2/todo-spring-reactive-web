package com.slog2.example.todo

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap

@Component
class TodoServiceImpl : TodoService {
  companion object {
    // demo 데이터
    val initialTodos = arrayOf(
      Todo(1, "kotlin study"),
      Todo(2, "pull up"),
      Todo(3, "early to bed")
    )
  }
  val todos = ConcurrentHashMap<Int, Todo>(initialTodos.associateBy(Todo::id))

  override fun getTodo(id: Int) = todos[id]?.toMono() ?: Mono.empty()

  override fun searchTodo(nameFilter: String): Flux<Todo> =
    todos.filter {
      it.value.name.contains(nameFilter, true)
    }.map(Map.Entry<Int, Todo>::value).toFlux()

  override fun createTodo(todoMono: Mono<Todo>): Mono<Todo> =
    todoMono.flatMap {
      if (todos[it.id] == null) {
        todos[it.id] = it
        it.toMono()
      } else {
        Mono.error(TodoExistException("Todo ${it.id} already exists"))
      }
    }

  override fun updateTodo(id: Int, todoMono: Mono<Todo>): Mono<Todo> =
    todoMono.flatMap {
      if (todos[id] == null) {
        Mono.error(TodoNotExistException("Todo ${id} not exists"))
      } else {
        todos[id] = it
        it.toMono()
      }
    }

  override fun deleteTodo(id: Int): Mono<Boolean> =
    if (todos[id] == null) {
      Mono.empty()
    } else {
      todos.remove(id)
      true.toMono()
    }
}
