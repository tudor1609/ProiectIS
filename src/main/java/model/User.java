package model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.stream.Collectors;

public class User {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ListProperty<Role> roles = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public List<Role> getRoles() {
        return roles.get();
    }

    public void setRoles(List<Role> roles) {
        this.roles.setAll(roles);
    }

    public ListProperty<Role> rolesProperty() {
        return roles;
    }

}
