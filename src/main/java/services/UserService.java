package services;

import exceptions.*;
import model.User;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Pattern;
import static services.FileSystemService.getPathToFile;

public class UserService {

    private static ObjectRepository<User> userRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("users_database.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }

    public static void addUser(String username, String password, String firstName,
                               String secondName, String phoneNumber, String address, String role) throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        checkUserAlreadyExist(username);
        checkAllFieldsAreCompleted(username, password, firstName, secondName,phoneNumber, role);
        checkPasswordFormatException(password);
        userRepository.insert(new User(username, encodePassword(username, password), firstName, secondName, phoneNumber, address, role));
    }

    public static void checkAllFieldsAreCompleted(String username, String password, String firstName,
                                                  String secondName, String phoneNumber, String role) throws FieldNotCompletedException {

        if (username.trim().isEmpty() || password.trim().isEmpty() || firstName.trim().isEmpty() || secondName.trim().isEmpty()
                || phoneNumber.trim().isEmpty() || role.trim().isEmpty())
            throw new FieldNotCompletedException();
    }

    public static void checkUserAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    public static boolean stringContainsNumber(String s)
    {
        return Pattern.compile( "[0-9]" ).matcher( s ).find();
    }
    public static boolean stringContainsUpperCase(String s)
    {
        return Pattern.compile( "[A-Z]" ).matcher( s ).find();
    }

    public static void checkPasswordFormatException(String password) throws WeakPasswordException {
        checkIfPasswordContainsAtLeast8Characters(password);
        checkIfPasswordContainsAtLeast1Digit(password);
        checkIfPasswordContainsAtLeast1UpperCase(password);
    }

    private static void checkIfPasswordContainsAtLeast8Characters(String password) throws WeakPasswordException {
        if (password.length() < 8)
            throw new WeakPasswordException("8 characters");
    }

    private static void checkIfPasswordContainsAtLeast1Digit(String password) throws WeakPasswordException {
        if (!stringContainsNumber(password))
            throw new WeakPasswordException("one digit");
    }

    private static void checkIfPasswordContainsAtLeast1UpperCase(String password) throws WeakPasswordException {
        if (!stringContainsUpperCase(password))
            throw new WeakPasswordException("one upper case");
    }

    public static void checkPassword(String password, String username) throws WrongPasswordException {
        boolean flag = false;
        for (User user : userRepository.find())
            if (Objects.equals(username, user.getUsername()))
                if (Objects.equals(encodePassword(username,password), user.getPassword()))
                    flag = true;

        if(!flag)
            throw new WrongPasswordException();
    }

    public static void checkUserDoesAlreadyExist(String username) throws UsernameDoesNotExistsException {
        boolean flag = false;
        for (User user : userRepository.find())
            if (Objects.equals(username, user.getUsername())) {
                flag = true;
                break;
            }

        if(!flag)
            throw new UsernameDoesNotExistsException(username);
    }

    public static int loginUser(String username, String password) throws UsernameDoesNotExistsException, WrongPasswordException, FieldNotCompletedException {
        checkUserDoesAlreadyExist(username);
        checkPassword(password,username);

        String encryptedPassword = encodePassword(username, password);

        Integer x = getValueBasedOnRoleSelected(username, encryptedPassword);
        if (x != null) return x;

        return 0;
    }

    @Nullable
    private static Integer getValueBasedOnRoleSelected(String username, String encryptedPassword) {
        for (User user : UserService.userRepository.find())
            if (Objects.equals(username, user.getUsername()) && Objects.equals(encryptedPassword, user.getPassword()))
                if (user.getRole().equals("Patient")) {
                    return 1;
                } else {
                    return 2;
                }

        return null;
    }

    static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
    public static ObjectRepository<User>  getUsers() {
        return userRepository;
    }
    public static Nitrite getDatabase() {
        return database;
    }
}
