package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";
        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                books.add(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Optional<Book> book = Optional.empty();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public boolean save(Book book) {

        String newSql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setDouble(4, book.getPrice());
            preparedStatement.setInt(5, book.getStock());

            int rowsInserted = preparedStatement.executeUpdate();

            return (rowsInserted != 1) ? false : true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Book book) {
        String newSql = "DELETE FROM book WHERE author = ? AND title = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean updateStock(Book book) {
        String updateSql = "UPDATE book SET stock = stock - 1 WHERE author = ? AND title = ? AND stock > 0";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hasSufficientStock(Book book, int quantity) {
        Optional<Book> existingBook = findById(book.getId());
        return existingBook.map(value -> value.getStock() >= quantity).orElse(false);
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(resultSet.getDate("publishedDate").toLocalDate())
                .setPrice(resultSet.getDouble("price"))
                .setStock(resultSet.getInt("stock"))
                .build();
    }
    public boolean saveOrder(String bookTitle, int quantitySold, String saleDate) {
        String query = "INSERT INTO `order` (book_title, quantity_sold, sale_date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookTitle);
            preparedStatement.setInt(2, quantitySold);
            preparedStatement.setDate(3, Date.valueOf(saleDate));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String[]> findAllOrders() {
        String query = "SELECT * FROM `order`";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<String[]> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(new String[]{
                        resultSet.getString("book_title"),
                        String.valueOf(resultSet.getInt("quantity_sold")),
                        resultSet.getDate("sale_date").toString()
                });
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
