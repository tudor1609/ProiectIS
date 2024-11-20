package service.admin;

import model.Right;
import model.Role;
import model.User;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public boolean addUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            System.out.println("User with username " + username + " already exists.");
            return false;
        }

        Role userRole = rightsRolesRepository.findRoleByTitle(role);
        if (userRole == null) {
            System.out.println("Role " + role + " does not exist.");
            return false;
        }

        // Hash the password before saving
        String hashedPassword = hashPassword(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword); // Save the hashed password
        boolean isSaved = userRepository.save(newUser);

        if (isSaved) {
            rightsRolesRepository.addRolesToUser(newUser, List.of(userRole));
        }
        return isSaved;
    }

    @Override
    public boolean deleteUser(Long userId) {

        Optional<User> userOptional = userRepository.findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();

        if (userOptional.isEmpty()) {
            System.out.println("User with ID " + userId + " does not exist.");
            return false;
        }


        User user = userOptional.get();
        userRepository.removeAll();
        return true;
    }

    @Override
    public boolean updateUser(Long userId, String username, String password, String role) {

        Optional<User> userOptional = userRepository.findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();

        if (userOptional.isEmpty()) {
            System.out.println("User with ID " + userId + " does not exist.");
            return false;
        }

        Role userRole = rightsRolesRepository.findRoleByTitle(role);
        if (userRole == null) {
            System.out.println("Role " + role + " does not exist.");
            return false;
        }


        User userToUpdate = userOptional.get();
        userToUpdate.setUsername(username);
        userToUpdate.setPassword(password);
        rightsRolesRepository.addRolesToUser(userToUpdate, List.of(userRole));
        return true;
    }

    @Override
    public boolean addRole(String role) {

        rightsRolesRepository.addRole(role);
        return true;
    }

    @Override
    public boolean deleteRole(Long roleId) {

        Role role = rightsRolesRepository.findRoleById(roleId);
        if (role == null) {
            System.out.println("Role with ID " + roleId + " does not exist.");
            return false;
        }


        return true;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean assignRoleToUser(Long userId, String roleName) {
        Role role = rightsRolesRepository.findRoleByTitle(roleName);
        if (role == null) {
            return false;
        }
        return rightsRolesRepository.addRoleToUser(userId, role.getId());
    }




}
