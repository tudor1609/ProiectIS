package repository.book;

import model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);

    boolean delete(Book book);

    void removeAll();

    boolean updateStock(Book book);

    boolean hasSufficientStock(Book book, int quantity);
    boolean saveOrder(String bookTitle, int quantitySold, String saleDate);

    List<String[]> findAllOrders();
}
