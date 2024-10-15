package tp1_javafx.tp1_prog2_v2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.Cliente;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Files.ClienteData;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import tp1_javafx.tp1_prog2_v2.Files.UserData;
import tp1_javafx.tp1_prog2_v2.Models.Role;
import tp1_javafx.tp1_prog2_v2.Models.User;

import java.io.IOException;
import java.util.Objects;

public class RegisterPageController {

    @FXML
    private TextField username;

    @FXML
    private TextField numCC;

    @FXML
    private TextField numFiscal;

    @FXML
    private TextField telefone;

    @FXML
    private TextField morada;

    @FXML
    private TextField localidade;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> userType;

    @FXML
    private Button registerButton;

    private UserData userData;
    private ClienteData clienteData;
    private DonoEmpresaData donoEmpresaData;

    public RegisterPageController() {
        userData = new UserData();
        userData.load();

        clienteData = new ClienteData();
        clienteData.load();

        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();
    }

    @FXML
    private void onRegisterButtonClick(ActionEvent event) {
        String nome = username.getText();
        String numCCStr = numCC.getText();
        String numFiscalStr = numFiscal.getText();
        String telefoneStr = telefone.getText();
        String moradaStr = morada.getText();
        String localidadeStr = localidade.getText();
        String passwordStr = password.getText();
        Role role = Role.valueOf(userType.getValue());

        if (Role.Cliente.equals(role)) {
            Cliente cliente = new Cliente(nome, numCCStr, numFiscalStr, telefoneStr, moradaStr, localidadeStr);
            clienteData.insert(cliente);
        } else if (Role.DonoEmpresa.equals(role)) {
            DonoEmpresa donoEmpresa = new DonoEmpresa(nome, numCCStr, numFiscalStr, telefoneStr, moradaStr, localidadeStr);
            donoEmpresaData.insert(donoEmpresa);
        }

        // Adiciona user ao login
        userData.register(new User(nome, passwordStr, role));

        clearFields();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registo bem-sucedido");
        alert.setHeaderText(null);
        alert.setContentText("Registo bem-sucedido!");
        alert.showAndWait();

        ((Button) event.getSource()).getScene().getWindow().hide();
        openLoginPage();

    }

    private void clearFields() {
        username.clear();
        numCC.clear();
        numFiscal.clear();
        telefone.clear();
        morada.clear();
        localidade.clear();
        password.clear();
    }

    private void openLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login Frame");
            stage.setScene(new Scene(root, 261, 344));
            stage.setResizable(false);


            LoginPageController loginPageController = loader.getController();
            stage.setOnCloseRequest(e -> loginPageController.closeStage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeStage() {
        ((Stage) registerButton.getScene().getWindow()).close();
    }
}
