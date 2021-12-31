import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.DentistService;
import services.MedicalRecordService;
import services.UserService;

public class Main extends Application {

    private double xoffset, yoffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserService.initDatabase();
        DentistService.initDatabase();
        MedicalRecordService.initDatabase();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("user_login.fxml"));
        primaryStage.getIcons().add(new Image("images/ToothPicture.png"));
        primaryStage.setTitle("Dentist reservation");
        primaryStage.setScene(new Scene(root, 655, 500));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        root.setOnMousePressed(event->{
            xoffset = event.getScreenX();
            yoffset = event.getScreenY();
        });

        root.setOnMouseDragged(e ->{
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());

        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}