package com.example.lab5.controller;

import com.example.lab5.model.Book;
import com.example.lab5.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Получение всех книг
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Получение книги по ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " not found");
        }
    }

    // Создание новой книги
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Обновление книги
    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setYear(bookDetails.getYear());
            bookRepository.save(book);
            return "Объект обновлён";
        } else {
            return "Объекта нет, создайте его";
        }
    }

    // Удаление книги
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);
            return "Удаление объекта завершено успешно!";
        } else {
            return "Такой книги нет!";
        }
    }
}