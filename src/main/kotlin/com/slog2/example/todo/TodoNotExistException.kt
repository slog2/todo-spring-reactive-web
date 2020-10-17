package com.slog2.example.todo

class TodoNotExistException(override val message: String) : Exception(message)
