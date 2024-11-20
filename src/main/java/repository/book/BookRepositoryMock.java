package repository.book;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {

    private final List<Book> books;
    private final List<String[]> orders;

    public BookRepositoryMock() {
        books = new ArrayList<>();
        orders = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public void removeAll() {
        books.clear();
        orders.clear();
    }

    @Override
    public boolean updateStock(Book book) {
        Optional<Book> existingBook = findById(book.getId());
        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            bookToUpdate.setStock(book.getStock());
            bookToUpdate.setPrice(book.getPrice());
            return true;
        }
        return false;
    }

    @Override
    public boolean hasSufficientStock(Book book, int quantity) {
        Optional<Book> existingBook = findById(book.getId());
        return existingBook.map(value -> value.getStock() >= quantity).orElse(false);
    }

    @Override
    public boolean saveOrder(String bookTitle, int quantitySold, String saleDate) {

        orders.add(new String[]{bookTitle, String.valueOf(quantitySold), saleDate});
        return true;
    }

    @Override
    public List<String[]> findAllOrders() {

        return new ArrayList<>(orders);
    }
}
