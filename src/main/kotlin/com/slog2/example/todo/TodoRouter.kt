package com.slog2.example.todo

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.router

@Component
class TodoRouter(private val todoHandler: TodoHandler) {
  @Bean
  fun todoRoutes(): RouterFunction<*> = router {
    "/functional".nest {
      "/todo".nest {
        GET("/{id}", todoHandler::get)
        POST("/", todoHandler::create)
        PUT("/{id}", todoHandler::update)
        DELETE("/{id}", todoHandler::delete)
      }

      "/todos".nest {
        GET("/", todoHandler::search)
      }
    }
  }

}
