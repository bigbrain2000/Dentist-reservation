package servicesTests;

import exceptions.fields.FieldNotCompletedException;
import exceptions.password.WeakPasswordException;
import exceptions.password.WrongPasswordException;
import exceptions.username.UsernameAlreadyExistsException;
import exceptions.username.UsernameDoesNotExistsException;
import model.User;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import services.FileSystemService;
import services.UserService;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private final String USERNAME = "DOODS2022";
    private final String PASSWORD = "Helicopter123";
    private final String FIRST_NAME = "Roncea";
    private final String SECOND_NAME = "Marius-Alexandru";
    private final String PHONE_NUMBER = "1234567890";
    private final String ADDRESS = "Caracal";
    private final String ROLE = "Dentist";

    @BeforeEach
    @DisplayName("Check if UserRepository path is correct created and database is initialized")
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER=".test-users_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }

    @AfterEach
    @DisplayName("Check if User database is closed after every test")
    public void tearDown() {UserService.getDatabase().close();}

    @Test
    @DisplayName("Check if User database is initialized and there are no clients")
    public void testUserRepositoryIsInitializedAndNoUserIsPersisted() {
        assertThat(UserService.getUserList()).isNotNull();
        assertThat(UserService.getUserList()).isEmpty();
    }

    @Test
    @DisplayName("Return the first name of registered user based on username")
    public void testUserFirstNameIsReturned() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();

        String firstName = UserService.getUserFirstName(USERNAME);
        assertEquals(FIRST_NAME ,firstName);
    }

    @Test
    @DisplayName("Return the second name of registered user based on username")
    public void testUserSecondNameIsReturned() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();

        String secondName = UserService.getUserSecondName(USERNAME);
        assertEquals(SECOND_NAME ,secondName);
    }

    @Test
    @DisplayName("Return the phone number of registered user based on username")
    public void testUserPhoneNumberIsReturned() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();

        String phoneNumber = UserService.getUserPhoneNumber(USERNAME);
        assertEquals(PHONE_NUMBER ,phoneNumber);
    }

    @Test
    @DisplayName("Return the address of registered user based on username")
    public void testUserAddressIsReturned() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();

        String address = UserService.getUserAddress(USERNAME);
        assertEquals(ADDRESS ,address);
    }

    @Test
    @DisplayName("Return the role of registered user based on username")
    public void testUserRoleIsReturned() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();

        String role = UserService.getUserRole(USERNAME);
        assertEquals(ROLE ,role);
    }

    @Test
    @DisplayName("Check if user is successfully removed from UserRepository")
    public void testUserIsDeletedFromDB() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();
        UserService.deleteUser(USERNAME);

        assertTrue(UserService.getUserList().isEmpty());
    }

    @Test
    @DisplayName("Check if user is successfully added to User database")
    public void testUserIsAddedToDB() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {

        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);

        assertThat(UserService.getUserList()).isNotEmpty();
        assertThat(UserService.getUserList()).size().isEqualTo(1);
        User user = UserService.getUserList().get(0);
        assertThat(user).isNotNull();

        assertAll("Should check all user data",
                () -> assertEquals(USERNAME, user.getUsername()),
                () -> assertEquals(user.getPassword(), UserService.encodePassword(USERNAME, PASSWORD)),
                () -> assertEquals(FIRST_NAME, user.getFirstName()),
                () -> assertEquals(SECOND_NAME, user.getSecondName()),
                () -> assertEquals(PHONE_NUMBER, user.getPhoneNumber()),
                () -> assertEquals(ADDRESS, user.getAddress()),
                () -> assertEquals(ROLE, user.getRole()));
    }

    @Test
    @DisplayName("User fields can not be empty")
    public void testAllFieldsAreCompleted() {
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted("", PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE));
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted(USERNAME, "", FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE));
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted(USERNAME, PASSWORD, "", SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE));
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted(USERNAME, PASSWORD, FIRST_NAME, "", PHONE_NUMBER, ADDRESS, ROLE));
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted(USERNAME, PASSWORD, SECOND_NAME, SECOND_NAME, "", ADDRESS, ROLE));
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted(USERNAME, PASSWORD, "", SECOND_NAME, PHONE_NUMBER, "", ROLE));
        assertThrows(FieldNotCompletedException.class, () -> UserService.checkAllFieldsAreCompleted(USERNAME, PASSWORD, "", SECOND_NAME, PHONE_NUMBER, ADDRESS, ""));
    }

    @Test
    @DisplayName("Multiple users can not have the same username")
    public void testUsernameAlreadyExists() {
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            testUserIsAddedToDB();
            UserService.checkUserAlreadyExists(USERNAME);
        });
    }

    @Test
    @DisplayName("Check if password contains numbers")
    public void testStringContainsNumber() {
        assertTrue(UserService.stringContainsNumber(PASSWORD));
    }

    @Test
    @DisplayName("Check if password contains an upper case")
    public void testStringContainsUpperCase() {
        assertTrue(UserService.stringContainsUpperCase(PASSWORD));
    }

    @Test
    @DisplayName("Check if password contains an upper case")
    public void testPasswordFormat() throws WeakPasswordException, UsernameAlreadyExistsException, FieldNotCompletedException {
        testUserIsAddedToDB();
        UserService.checkPasswordFormatException(PASSWORD);
    }

    @Test
    @DisplayName("Password must contains at least 8 characters")
    public void testPasswordContainsAtLeast8Characters() {
        assertThrows(WeakPasswordException.class, () -> UserService.addUser(USERNAME, "Heli1", FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE));
    }

    @Test
    @DisplayName("Password must contains at least 1 digit")
    public void testPasswordContainsAtLeast1Digit() {
        assertThrows(WeakPasswordException.class, () -> UserService.addUser(USERNAME, "Helicopter", FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE));
    }

    @Test
    @DisplayName("Password must contains at least 1 upper case")
    public void testPasswordContainsAtLeast1UpperCase() {
        assertThrows(WeakPasswordException.class, () -> UserService.addUser(USERNAME, "helicopter123", FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE));
    }

    @Test
    @DisplayName("Handle login action based on register role")
    public void testLoginUser() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException, UsernameDoesNotExistsException, WrongPasswordException {
        String PATIENT_ROLE = "Patient";
        String PATIENT_NAME = "PC20000";

        testUserIsAddedToDB();
        assertThat(UserService.getUserList()).isNotEmpty();
        assertThat(UserService.getUserList()).size().isEqualTo(1);
        int resultDentistRole = UserService.loginUser(USERNAME, PASSWORD);
        assertEquals(ROLE, verifyRole(resultDentistRole));

        UserService.addUser(PATIENT_NAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, PATIENT_ROLE);
        assertThat(UserService.getUserList()).isNotEmpty();
        assertThat(UserService.getUserList()).size().isEqualTo(2);
        int resultPatientRole = UserService.loginUser(PATIENT_NAME, PASSWORD);
        assertEquals(PATIENT_ROLE, verifyRole(resultPatientRole));
    }

    @NotNull
    @Contract(pure = true)
    private String verifyRole(int result) {

        if(result == 1)
            return  "Patient";
        else if(result == 2)
            return  "Dentist";
        else
            return  "";
    }

    @Test
    @DisplayName("Get a list with all the dentists name")
    public void testDentistsFirstNameListIsReturned() throws UsernameAlreadyExistsException, FieldNotCompletedException, WeakPasswordException {
        testUserIsAddedToDB();
        //another example
        UserService.addUser("Mircea", PASSWORD, "Popescu", SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);

        ArrayList<String> dentistFirstNameList = UserService.getDentistsFirstNameList();

        assertEquals(FIRST_NAME, dentistFirstNameList.get(0));
        assertEquals("Popescu", dentistFirstNameList.get(1));
    }

    @Test
    @DisplayName("Password encoding")
    public void testPasswordEncoding() {assertNotEquals(PASSWORD, UserService.encodePassword(USERNAME, USERNAME)); }
}
