package com.library.library_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.library_backend.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
