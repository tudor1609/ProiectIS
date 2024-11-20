package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.*;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository {

    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(String role) {
        String query = "INSERT IGNORE INTO " + ROLE + " (role) VALUES (?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
            insertStatement.setString(1, role);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRight(String right) {
        String query = "INSERT IGNORE INTO " + RIGHT + " (right) VALUES (?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
            insertStatement.setString(1, right);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role findRoleByTitle(String role) {
        String query = "SELECT * FROM role WHERE role = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Role(
                        resultSet.getLong("id"),
                        resultSet.getString("role"),
                        new ArrayList<>()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Role findRoleById(Long roleId) {
        String query = "SELECT * FROM " + ROLE + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, roleId);
            ResultSet roleResultSet = statement.executeQuery();
            if (roleResultSet.next()) {
                String roleTitle = roleResultSet.getString("role");
                return new Role(roleId, roleTitle, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Right findRightByTitle(String right) {
        String query = "SELECT * FROM " + RIGHT + " WHERE right = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, right);
            ResultSet rightResultSet = statement.executeQuery();
            if (rightResultSet.next()) {
                Long rightId = rightResultSet.getLong("id");
                String rightTitle = rightResultSet.getString("right");
                return new Right(rightId, rightTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        String query = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Role role : roles) {
                if (role == null) {
                    System.err.println("Role is null for user: " + user.getUsername());
                    continue;
                }
                statement.setLong(1, user.getId());
                statement.setLong(2, role.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Role> findRolesForUser(Long userId) {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT role_id FROM " + USER_ROLE + " WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet userRoleResultSet = statement.executeQuery();
            while (userRoleResultSet.next()) {
                long roleId = userRoleResultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        String query = "INSERT IGNORE INTO " + ROLE_RIGHT + " (role_id, right_id) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifică dacă un utilizator are dreptul de a vinde cărți.
     *
     * @param userId ID-ul utilizatorului.
     * @return true dacă utilizatorul are drept de vânzare, false altfel.
     */
    @Override
    public boolean canSellBooks(Long userId) {
        List<Role> roles = findRolesForUser(userId);
        for (Role role : roles) {
            List<Right> rights = role.getRights();
            for (Right right : rights) {
                if (right.getRight().equals("SELL_BOOK")) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean addRoleToUser(Long userId, Long roleId) {
        String query = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setLong(2, roleId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
