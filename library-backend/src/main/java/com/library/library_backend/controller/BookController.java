package com.library.library_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.library_backend.model.Book;
import com.library.library_backend.repository.BookRepository;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class BookController {

	@Autowired
	private BookRepository repo;

	// PUBLIC: anyone can see books
	@GetMapping
	public List<Book> getAllBooks() {
		return repo.findAll();
	}

	// ADMIN: add book
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public Book addBook(@RequestBody Book book) {
		return repo.save(book);
	}

	// ADMIN: delete book
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable Long id) {
		repo.deleteById(id);
	}

	// ADMIN: update book
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public Book updateBook(@PathVariable Long id, @RequestBody Book updated) {
		return repo.findById(id).map(book -> {
			book.setTitle(updated.getTitle());
			book.setAuthor(updated.getAuthor());
			book.setCategory(updated.getCategory());
			book.setAvailable(updated.isAvailable());
			book.setBorrower(updated.getBorrower());
			return repo.save(book);
		}).orElseThrow(() -> new RuntimeException("Book not found"));
	}

	// USER + ADMIN: Borrow book
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping("/{id}/borrow")
	public Book borrowBook(@PathVariable Long id, @RequestParam String borrowerName) {
		return repo.findById(id).map(book -> {
			if (!book.isAvailable()) {
				throw new RuntimeException("Book is already borrowed");
			}
			book.setAvailable(false);
			book.setBorrower(borrowerName);
			return repo.save(book);
		}).orElseThrow(() -> new RuntimeException("Book not found"));
	}

	// USER + ADMIN: Return book
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping("/{id}/return")
	public Book returnBook(@PathVariable Long id) {
		return repo.findById(id).map(book -> {
			book.setAvailable(true);
			book.setBorrower(null);
			return repo.save(book);
		}).orElseThrow(() -> new RuntimeException("Book not found"));
	}
}
