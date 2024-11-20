package service.admin;

import model.User;
import java.util.List;

public interface AdminService {
    boolean addUser(String username, String password, String role);
    boolean deleteUser(Long userId);
    boolean updateUser(Long userId, String username, String password, String role);
    boolean addRole(String role);
    boolean deleteRole(Long roleId);
    List<User> getAllUsers();
    boolean assignRoleToUser(Long userId, String roleName);
}
