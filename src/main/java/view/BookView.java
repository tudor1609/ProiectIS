package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.BookDTO;

import java.util.List;

public class BookView {
    private TableView<BookDTO> bookTableView;
    private ObservableList<BookDTO> booksObservableList;
    private TextField authorTextField;
    private TextField titleTextField;
    private TextField stockTextField;
    private TextField sellQuantityTextField;
    private TextField priceTextField;
    private Label authorLabel;
    private Label titleLabel;
    private Label stockLabel;
    private Label sellQuantityLabel;
    private Label priceLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button sellButton;

    public BookView(Stage primaryStage, List<BookDTO> bookDTOS){
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 1300, 720);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(bookDTOS);
        initTableView(gridPane);

        initSaveOptions(gridPane);

        primaryStage.show();
    }

    private TableView<String[]> orderTableView;
    private ObservableList<String[]> ordersObservableList;

    private void initTableView(GridPane gridPane) {

        bookTableView = new TableView<>();
        bookTableView.setPlaceholder(new Label("No rows to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<BookDTO, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn,priceColumn, stockColumn);
        bookTableView.setItems(booksObservableList);
        gridPane.add(bookTableView, 0, 0, 5, 1);


        orderTableView = new TableView<>();
        orderTableView.setPlaceholder(new Label("No orders to display"));

        TableColumn<String[], String> orderTitleColumn = new TableColumn<>("Book Title");
        orderTitleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> orderQuantityColumn = new TableColumn<>("Quantity Sold");
        orderQuantityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> orderDateColumn = new TableColumn<>("Sale Date");
        orderDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        orderTableView.getColumns().addAll(orderTitleColumn, orderQuantityColumn, orderDateColumn);

        ordersObservableList = FXCollections.observableArrayList();
        orderTableView.setItems(ordersObservableList);

        gridPane.add(orderTableView, 0, 2, 5, 1);
    }


    private void initSaveOptions(GridPane gridPane){
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);

        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);

        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel, 5, 1);

        stockTextField = new TextField();
        gridPane.add(stockTextField, 6, 1);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel, 7, 1);

        priceTextField = new TextField();
        gridPane.add(priceTextField, 8, 1);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 9, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 10, 1);


        sellQuantityLabel = new Label("Sell Quantity");
        gridPane.add(sellQuantityLabel, 11, 1);

        sellQuantityTextField = new TextField();
        gridPane.add(sellQuantityTextField, 12, 1);


        sellButton = new Button("Sell");
        gridPane.add(sellButton, 13, 1);
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
    }

    public void addSelectionTableListener(ChangeListener<BookDTO> selectionTableListener){
        bookTableView.getSelectionModel().selectedItemProperty().addListener(selectionTableListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener) {
        sellButton.setOnAction(sellButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }

    public String getStock(){ return stockTextField.getText();}
    public String getPrice(){ return priceTextField.getText();}


    public int getSellQuantity() {
        return Integer.parseInt(sellQuantityTextField.getText());
    }
    public int getQuantityToSell() {
        return Integer.parseInt(sellQuantityTextField.getText());
    }

    public void updateBookStock(BookDTO bookDTO, int newStock) {
        bookDTO.setStock(newStock);
        this.booksObservableList.set(this.booksObservableList.indexOf(bookDTO), bookDTO);
    }

    public ObservableList<BookDTO> getBooksObservableList(){
        return booksObservableList;
    }

    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }

    public TableView<BookDTO> getBookTableView(){
        return bookTableView;
    }

    public void addOrderToObservableList(String[] orderDetails) {
        ordersObservableList.add(orderDetails);
    }

}
