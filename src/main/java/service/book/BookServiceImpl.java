package service.book;

import model.Book;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        return bookRepository.delete(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    @Override
    public boolean hasSufficientStock(Book book, int quantity) {
        return bookRepository.hasSufficientStock(book, quantity);
    }

    @Override
    public boolean updateStock(Book book) {
        return bookRepository.updateStock(book);
    }

    @Override
    public boolean sellBook(Book book, int quantity) {
        if (book == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid book or quantity for sale");
        }

        if (hasSufficientStock(book, quantity)) {
            double totalSalePrice = book.getPrice() * quantity;

            System.out.println("Total Sale Price: " + totalSalePrice);

            book.setStock(book.getStock() - quantity);
            if (updateStock(book)) {
                String saleDate = LocalDate.now().toString();
                return bookRepository.saveOrder(book.getTitle(), quantity, saleDate);
            }
        }

        System.out.println("Not enough stock available for book: " + book.getTitle());
        return false;
    }
    @Override
    public boolean saveOrder(String bookTitle, int quantitySold, String saleDate) {
        return bookRepository.saveOrder(bookTitle, quantitySold, saleDate);
    }
    public List<String[]> getAllOrders() {
        return bookRepository.findAllOrders();
    }




}
