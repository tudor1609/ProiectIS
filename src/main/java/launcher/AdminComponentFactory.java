package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.admin.AdminService;
import service.admin.AdminServiceImpl;
import view.AdminView;

import java.sql.Connection;

public class AdminComponentFactory {

    private static AdminComponentFactory instance;
    private final AdminView adminView;
    private final AdminController adminController;
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public static AdminComponentFactory getInstance(Boolean componentsForTest, Stage stage) {
        if (instance == null) {
            instance = new AdminComponentFactory(componentsForTest, stage);
        }
        return instance;
    }

    private AdminComponentFactory(Boolean componentsForTest, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        this.adminService = new AdminServiceImpl(userRepository, rightsRolesRepository);

        this.adminView = new AdminView(stage);

        this.adminController = new AdminController(adminView, adminService);
    }



    public AdminView getAdminView() {
        return adminView;
    }

    public AdminController getAdminController() {
        return adminController;
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public static AdminComponentFactory getInstance() {
        return instance;
    }
}
