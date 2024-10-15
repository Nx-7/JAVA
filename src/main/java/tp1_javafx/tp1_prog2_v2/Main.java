package tp1_javafx.tp1_prog2_v2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import tp1_javafx.tp1_prog2_v2.Files.UserData;
import tp1_javafx.tp1_prog2_v2.Models.Role;
import tp1_javafx.tp1_prog2_v2.Models.User;

import java.io.IOException;

public class Main extends Application {
    private UserData userData;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DonoEmpresaData donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();


        UserData userData = new UserData();
        userData.load();

        userData.register(new User("root", "root", Role.ADMIN));

        userData.save();

        launch();
    }
    public static User userlogged;
}