package com.slog2.example.todo

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.kotlin.core.publisher.onErrorResume
import java.net.URI

@Component
class TodoHandler(val todoService: TodoService) {
  fun get(serverRequest: ServerRequest) =
    todoService.getTodo(serverRequest.pathVariable("id").toInt())
      .flatMap { ok().body(fromObject(it)) }
      .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

  fun search(serverRequest: ServerRequest) =
    ok().body(todoService.searchTodo(serverRequest.queryParam("nameFilter").orElse("")), Todo::class.java)

  fun create(serverRequest: ServerRequest) =
    todoService.createTodo(serverRequest.bodyToMono()).flatMap {
      created(URI.create("/functional/todo/${it.id}")).build()
    }.onErrorResume(Exception::class) {
      badRequest().body(fromObject(ErrorResponse("error creating todo", it.message ?: "error")))
    }

  fun update(serverRequest: ServerRequest) =
    todoService.updateTodo(serverRequest.pathVariable("id").toInt(), serverRequest.bodyToMono()).flatMap {
      created(URI.create("/functional/todo/${it.id}")).build()
    }.onErrorResume(Exception::class) {
      badRequest().body(fromObject(ErrorResponse("error updating todo", it.message ?: "error")))
    }

  fun delete(serverRequest: ServerRequest) =
    todoService.deleteTodo(serverRequest.pathVariable("id").toInt())
      .flatMap { ok().build() }
      .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())
      .onErrorResume(Exception::class) {
        badRequest().body(fromObject(ErrorResponse("error deleting todo", it.message ?: "error")))
      }
}
