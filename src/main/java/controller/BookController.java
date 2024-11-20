package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.book.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.util.List;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService) {
        this.bookView = bookView;
        this.bookService = bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSellButtonListener(new SellButtonListener());
        this.bookView.addSelectionTableListener(new SelectionTableListener());

        List<String[]> orders = bookService.getAllOrders();
        for (String[] order : orders) {
            bookView.addOrderToObservableList(order);
        }
    }
    private class SelectionTableListener implements ChangeListener {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            BookDTO selectedBookDTO = (BookDTO) newValue;
            System.out.println("Book Author: " + selectedBookDTO.getAuthor() + " Title: " + selectedBookDTO.getTitle());
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null){
                boolean deletionSuccessfull = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSuccessfull){
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Deletion not successful", "Deletion Process", "There was a problem in the deletion process. Please restart the application and try again!");
                }
            } else {
                bookView.addDisplayAlertMessage("Deletion not successful", "Deletion Process", "You need to select a row from table before pressing the delete button!");
            }
        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                try {
                    int sellQuantity = bookView.getSellQuantity();

                    if (sellQuantity <= 0) {
                        bookView.addDisplayAlertMessage("Sale Error", "Invalid Quantity", "The sell quantity must be greater than 0.");
                        return;
                    }

                    if (bookDTO.getStock() >= sellQuantity) {
                        int newStock = bookDTO.getStock() - sellQuantity;
                        bookDTO.setStock(newStock);
                        boolean stockUpdated = bookService.updateStock(BookMapper.convertBookDTOToBook(bookDTO));

                        if (stockUpdated) {
                            bookView.getBookTableView().refresh();

                            String saleDate = java.time.LocalDate.now().toString();
                            boolean orderSaved = bookService.saveOrder(bookDTO.getTitle(), sellQuantity, saleDate);

                            if (orderSaved) {

                                String[] orderDetails = {bookDTO.getTitle(), String.valueOf(sellQuantity), saleDate};
                                bookView.addOrderToObservableList(orderDetails);
                                bookView.addDisplayAlertMessage("Sale Successful", "Book Sold", "Sold " + sellQuantity + " copies of \"" + bookDTO.getTitle() + "\".");
                            } else {
                                bookView.addDisplayAlertMessage("Sale Error", "Order Save Failed", "Failed to save the order. Please try again.");
                            }
                        } else {
                            bookView.addDisplayAlertMessage("Sale Error", "Stock Update Failed", "Failed to update stock. Please try again.");
                        }
                    } else {
                        bookView.addDisplayAlertMessage("Sale Error", "Insufficient Stock", "Not enough stock available.");
                    }
                } catch (NumberFormatException e) {
                    bookView.addDisplayAlertMessage("Sale Error", "Invalid Quantity", "Please enter a valid numeric quantity.");
                }
            } else {
                bookView.addDisplayAlertMessage("Sale Error", "No Book Selected", "Please select a book to sell.");
            }
        }
    }


    private class SaveButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            String price = bookView.getPrice();
            String stock = bookView.getStock();


            if (title.isEmpty() || author.isEmpty() ){
                bookView.addDisplayAlertMessage("Save Error", "Problem at Author or Title fields", "Can not have an empty Title or Author field.");
            } else {
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).setPrice(Integer.parseInt(price)).setStock(Integer.parseInt(stock)).build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook){
                    bookView.addDisplayAlertMessage("Save Successful", "Book Added", "Book was successfully added to the database.");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.addDisplayAlertMessage("Save Error", "Problem at adding Book", "There was a problem at adding the book to the database. Please try again!");
                }
            }
        }
    }
}
