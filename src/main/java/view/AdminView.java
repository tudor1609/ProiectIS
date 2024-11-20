package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.User;

import java.util.List;

public class AdminView {
    private final TableView<User> userTableView;
    private final ObservableList<User> usersObservableList;
    private final TextField roleTextField;
    private final TextField newUsernameField;
    private final PasswordField newPasswordField;
    private final Button assignRoleButton;
    private final Button addUserButton;

    public AdminView(Stage stage) {
        stage.setTitle("Admin Panel");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        userTableView = new TableView<>();
        usersObservableList = FXCollections.observableArrayList();

        initializeUserTable(gridPane);

        roleTextField = new TextField();
        newUsernameField = new TextField();
        newPasswordField = new PasswordField();
        assignRoleButton = new Button("Assign Role");
        addUserButton = new Button("Add User");

        initializeRoleControls(gridPane);

        Scene scene = new Scene(gridPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeUserTable(GridPane gridPane) {
        userTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<User, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(param -> param.getValue().idProperty().asObject());

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(param -> param.getValue().usernameProperty());

        TableColumn<User, String> rolesColumn = new TableColumn<>("Roles");
        rolesColumn.setCellValueFactory(param -> param.getValue().rolesProperty().asString());

        userTableView.getColumns().addAll(idColumn, usernameColumn, rolesColumn);
        userTableView.setItems(usersObservableList);

        gridPane.add(userTableView, 0, 0, 3, 1);
    }

    private void initializeRoleControls(GridPane gridPane) {
        Label roleLabel = new Label("Role:");
        gridPane.add(roleLabel, 0, 1);

        gridPane.add(roleTextField, 1, 1);

        gridPane.add(assignRoleButton, 2, 1);

        Label newUsernameLabel = new Label("New Username:");
        gridPane.add(newUsernameLabel, 0, 2);

        gridPane.add(newUsernameField, 1, 2);

        Label newPasswordLabel = new Label("New Password:");
        gridPane.add(newPasswordLabel, 0, 3);

        gridPane.add(newPasswordField, 1, 3);

        gridPane.add(addUserButton, 2, 3);
    }

    public void setUsers(List<User> users) {
        usersObservableList.setAll(users);
    }

    public User getSelectedUser() {
        return userTableView.getSelectionModel().getSelectedItem();
    }

    public String getRoleInput() {
        return roleTextField.getText();
    }

    public String getNewUsername() {
        return newUsernameField.getText();
    }

    public String getNewPassword() {
        return newPasswordField.getText();
    }

    public void addAssignRoleButtonListener(EventHandler<ActionEvent> listener) {
        assignRoleButton.setOnAction(listener);
    }

    public void addAddUserButtonListener(EventHandler<ActionEvent> listener) {
        addUserButton.setOnAction(listener);
    }

    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
