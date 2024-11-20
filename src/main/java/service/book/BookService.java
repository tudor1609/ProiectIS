package service.book;

import model.Book;
import java.util.List;

public interface BookService {


    List<Book> findAll();

    Book findById(Long id);

    boolean save(Book book);

    boolean delete(Book book);

    int getAgeOfBook(Long id);

    boolean hasSufficientStock(Book book, int quantity);

    boolean updateStock(Book book);

    boolean sellBook(Book book, int quantity);

    List<String[]> getAllOrders();

    boolean saveOrder(String title, int sellQuantity, String saleDate);
}
