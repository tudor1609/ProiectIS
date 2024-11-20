import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello world!");


        Book book_fram = new BookBuilder()
                .setTitle("Fram Ursul Polar")
                .setAuthor("Cezar Petrescu")
                .setPublishedDate(LocalDate.of(2003, 9, 16))
                .build();

        System.out.println("Book created: " + book_fram.getAuthor() + " - " + book_fram.getTitle());

        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();

        BookRepository bookRepository = new BookRepositoryMySQL(connection);
        boolean saveSuccessful = bookRepository.save(book_fram);
        if (saveSuccessful) {
            System.out.println("Book saved successfully!");
        } else {
            System.out.println("Failed to save the book.");
        }

        System.out.println("Books in database:");
        System.out.println(bookRepository.findAll());
    }
}
