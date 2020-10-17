package com.slog2.example.todo

import java.time.LocalDateTime

data class Todo (
  var id: Int = 0,
  var name: String = "",
  var done: Boolean = false,
  var createdAt: LocalDateTime = LocalDateTime.now()
)
