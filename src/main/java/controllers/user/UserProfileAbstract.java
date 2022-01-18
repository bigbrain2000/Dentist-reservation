package controllers.user;

import controllers.login.LoginController;
import javafx.scene.control.TextField;
import model.User;
import org.jetbrains.annotations.NotNull;
import services.UserService;

import java.util.Objects;

public abstract class UserProfileAbstract {
    private static final User loggedUser = LoginController.getLoggedUser();

    public static void deleteUserFromDB() {
        UserService.deleteUser(loggedUser.getUsername());
    }

    public void setTextFieldsForProfileBasedOnUsername(@NotNull TextField usernameField, @NotNull TextField firstNameField, @NotNull TextField secondNameField,
                                                       @NotNull TextField addressField, @NotNull TextField phoneNumberField, @NotNull TextField roleField) {
        for (User user : UserService.getUsers().find())
            if (Objects.equals(loggedUser.getUsername(), user.getUsername())) {
                setTextFieldsForProfile(usernameField, firstNameField, secondNameField, addressField, phoneNumberField, roleField);

                setTextFieldsForProfileNotEditable(usernameField, firstNameField, secondNameField, addressField, phoneNumberField, roleField);
            }
    }

    public void setTextFieldsForProfile(@NotNull TextField usernameField, @NotNull TextField firstNameField, @NotNull TextField secondNameField,
                                        @NotNull TextField addressField, @NotNull TextField phoneNumberField, @NotNull TextField roleField) {

        usernameField.setText(loggedUser.getUsername());
        firstNameField.setText(loggedUser.getFirstName());
        secondNameField.setText(loggedUser.getSecondName());
        addressField.setText(loggedUser.getAddress());
        phoneNumberField.setText(loggedUser.getPhoneNumber());
        roleField.setText(loggedUser.getRole());
    }

    public void setTextFieldsForProfileNotEditable(@NotNull TextField usernameField, @NotNull TextField firstNameField, @NotNull TextField secondNameField,
                                                   @NotNull TextField addressField, @NotNull TextField phoneNumberField, @NotNull TextField roleField) {
        usernameField.setEditable(false);
        firstNameField.setEditable(false);
        secondNameField.setEditable(false);
        addressField.setEditable(false);
        phoneNumberField.setEditable(false);
        roleField.setEditable(false);
    }
}
