package repository.user;

import model.Right;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.id, u.username, u.password FROM user u";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new UserBuilder()
                        .setId(resultSet.getLong("id"))
                        .setUsername(resultSet.getString("username"))
                        .setPassword(resultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                        .build();

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }



    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> notification = new Notification<>();

        // Folosirea PreparedStatement pentru prevenirea SQL Injection
        String query = "SELECT * FROM " + USER + " WHERE `username` = ? AND `password` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(resultSet.getString("username"))
                        .setPassword(resultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                        .build();

                notification.setResult(user);
            } else {
                notification.addError("Invalid username or password!");
            }
        } catch (SQLException e) {
            notification.addError("Something went wrong with the database.");
            e.printStackTrace();
        }

        return notification;
    }

    @Override
    public boolean save(User user) {
        String query = "INSERT INTO " + USER + " (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long userId = generatedKeys.getLong(1);
                    user.setId(userId);
                    rightsRolesRepository.addRolesToUser(user, user.getRoles());
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void removeAll() {
        String query = "DELETE FROM " + USER + " WHERE id >= 0";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String query = "SELECT 1 FROM " + USER + " WHERE `username` = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
