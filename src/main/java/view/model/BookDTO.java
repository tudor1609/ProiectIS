package view.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookDTO {
    private StringProperty author;
    private StringProperty title;
    private IntegerProperty stock;
    private DoubleProperty price;

    public StringProperty authorProperty() {
        if (author == null) {
            author = new SimpleStringProperty(this, "author");
        }
        return author;
    }

    public void setAuthor(String author) {
        authorProperty().set(author);
    }

    public String getAuthor() {
        return authorProperty().get();
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public IntegerProperty stockProperty() {
        if (stock == null) {
            stock = new SimpleIntegerProperty(this, "stock", 0);
        }
        return stock;
    }

    public void setStock(int stock) {
        stockProperty().set(stock);
    }

    public int getStock() {
        return stockProperty().get();
    }

    public DoubleProperty priceProperty() {
        if (price == null) {
            price = new SimpleDoubleProperty(this, "price", 0.0);
        }
        return price;
    }

    public void setPrice(double price) {
        priceProperty().set(price);
    }

    public double getPrice() {
        return priceProperty().get();
    }
}
