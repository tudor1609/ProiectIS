package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {
    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache) {
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if (cache.hasResult()) {
            return cache.load();
        }

        List<Book> books = decoratedBookRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if (cache.hasResult()) {
            return cache.load().stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }

        return decoratedBookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.delete(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedBookRepository.removeAll();
    }

    @Override
    public boolean updateStock(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.updateStock(book);
    }

    @Override
    public boolean hasSufficientStock(Book book, int quantity) {
        if (cache.hasResult()) {
            Optional<Book> cachedBook = cache.load().stream()
                    .filter(it -> it.getId().equals(book.getId()))
                    .findFirst();
            if (cachedBook.isPresent()) {
                return cachedBook.get().getStock() >= quantity;
            }
        }

        return decoratedBookRepository.hasSufficientStock(book, quantity);
    }

    @Override
    public boolean saveOrder(String bookTitle, int quantitySold, String saleDate) {

        return decoratedBookRepository.saveOrder(bookTitle, quantitySold, saleDate);
    }

    @Override
    public List<String[]> findAllOrders() {
        return decoratedBookRepository.findAllOrders();
    }

}
