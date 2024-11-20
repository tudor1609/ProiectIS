package repository.book;

import model.Book;
import java.util.List;
import java.util.Optional;

public abstract class BookRepositoryDecorator implements BookRepository {

    protected BookRepository decoratedBookRepository;

    public BookRepositoryDecorator(BookRepository bookRepository) {
        this.decoratedBookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return decoratedBookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return decoratedBookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        return decoratedBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        return decoratedBookRepository.delete(book);
    }

    @Override
    public void removeAll() {
        decoratedBookRepository.removeAll();
    }

    @Override
    public boolean updateStock(Book book) {
        return decoratedBookRepository.updateStock(book);
    }

    @Override
    public boolean hasSufficientStock(Book book, int quantity) {
        return decoratedBookRepository.hasSufficientStock(book, quantity);
    }
}
