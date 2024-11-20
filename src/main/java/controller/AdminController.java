package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.User;
import service.admin.AdminService;
import view.AdminView;

import java.util.List;

public class AdminController {

    private final AdminView adminView;
    private final AdminService adminService;

    public AdminController(AdminView adminView, AdminService adminService) {
        this.adminView = adminView;
        this.adminService = adminService;

        populateUserTable();
        adminView.addAssignRoleButtonListener(new AssignRoleButtonListener());
        adminView.addAddUserButtonListener(new AddUserButtonListener());
    }

    private void populateUserTable() {
        List<User> users = adminService.getAllUsers();
        adminView.setUsers(users);
    }

    private class AssignRoleButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            User selectedUser = adminView.getSelectedUser();
            String role = adminView.getRoleInput();

            if (selectedUser != null && !role.isEmpty()) {
                boolean success = adminService.assignRoleToUser(selectedUser.getId(), role);

                if (success) {
                    adminView.setUsers(adminService.getAllUsers());
                    adminView.showMessage("Role assigned successfully!");
                } else {
                    adminView.showMessage("Failed to assign role. Please try again.");
                }
            } else {
                adminView.showMessage("Please select a user and enter a role.");
            }
        }
    }

    private class AddUserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getNewUsername();
            String password = adminView.getNewPassword();
            String role = adminView.getRoleInput();

            if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                boolean userAdded = adminService.addUser(username, password, role);

                if (userAdded) {
                    adminView.setUsers(adminService.getAllUsers());
                    adminView.showMessage("User added successfully!");
                } else {
                    adminView.showMessage("Failed to add user. Please try again.");
                }
            } else {
                adminView.showMessage("Please fill in all fields.");
            }
        }
    }
}
