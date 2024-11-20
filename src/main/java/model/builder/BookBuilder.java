package model.builder;

import model.Book;

import java.time.LocalDate;

public class BookBuilder {
    private Book book;

    public BookBuilder() {
        book = new Book();
    }

    public BookBuilder setId(Long id) {
        book.setId(id);
        return this;
    }
    public BookBuilder setTitle(String title) {
        book.setTitle(title);
        return this;
    }
    public BookBuilder setAuthor(String author) {
        book.setAuthor(author);
        return this;
    }

    public BookBuilder setPublishedDate(LocalDate publishedDate) {
        book.setPublishedDate(publishedDate);
        return this;
    }
    public BookBuilder setStock(int stock){
        book.setStock(stock);
        return this;
    }

    public BookBuilder setPrice(double price){
        book.setPrice(price);
        return this;
    }
    public Book build() {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalStateException("Title must be set before building the Book.");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new IllegalStateException("Author must be set before building the Book.");
        }
        return book;
    }
}
