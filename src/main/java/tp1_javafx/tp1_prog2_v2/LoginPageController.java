package tp1_javafx.tp1_prog2_v2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.Cliente;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Classes.Funcionario;
import tp1_javafx.tp1_prog2_v2.Classes.TipoFuncionario;
import tp1_javafx.tp1_prog2_v2.Files.ClienteData;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import tp1_javafx.tp1_prog2_v2.Files.UserData;
import tp1_javafx.tp1_prog2_v2.Models.Role;
import tp1_javafx.tp1_prog2_v2.Models.User;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private UserData userData;
    private DonoEmpresaData donoEmpresaData;
    private ClienteData clienteData;

    public LoginPageController() {
        userData = new UserData();
        userData.load();

        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();

        clienteData = new ClienteData();
        clienteData.load();
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        User usr = userData.login(username.getText(), password.getText());

        if (usr != null) {

            Main.userlogged = usr;
            DonoEmpresa donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());
            Cliente clienteLogado = clienteData.getClienteByUserName(Main.userlogged.getUsername());
            Funcionario funcionarioLogado = donoEmpresaData.getFuncionarioByUsername(Main.userlogged.getUsername());

            if (usr.getRole() == Role.ADMIN) {
                System.out.println("Logged in as admin");
                openAdminPage(usr);
            } else if (usr.getRole() == Role.DonoEmpresa) {
                openDonoEmpresaMenu(donoLogado);
                closeStage();
            } else if (usr.getRole() == Role.Funcionario) {
                if (funcionarioLogado != null && funcionarioLogado.getTipo() == TipoFuncionario.SECRETARIADO) {
                    openSecretariadoConsultasPage(funcionarioLogado);
                } else {
                    openConsultasFuncionarioPage(funcionarioLogado);
                }
            } else if (usr.getRole() == Role.Cliente) {
                openMarcarConsultaPage(clienteLogado);
            }

            closeStage();
        } else {
            exibirAlertaErro("Erro no Login", "Credenciais Inválidas!");
        }
    }


    @FXML
    private void onRegisterButtonClick(ActionEvent event) {
        openRegisterPage();
        closeStage();
    }

    private void openRegisterPage() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("RegisterPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registration Frame");
            stage.setScene(new Scene(root, 431, 431));
            stage.setResizable(false);

            RegisterPageController registerPageController = loader.getController();
            stage.setOnCloseRequest(e -> registerPageController.closeStage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeStage() {
        ((Stage) loginButton.getScene().getWindow()).close();
    }

    private void openDonoEmpresaMenu(DonoEmpresa donoEmpresa) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("DonoEmpresaMenuPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("DonoEmpresaMenu");
            stage.setScene(new Scene(root, 556, 466));
            stage.setResizable(false);

            DonoEmpresaMenuController donoEmpresaMenuController = loader.getController();
            donoEmpresaMenuController.setDonoEmpresa(donoEmpresa);

            stage.setOnCloseRequest(e -> donoEmpresaMenuController.closeStage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMarcarConsultaPage(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("MarcarConsultaPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Marcação de Consulta");
            stage.setScene(new Scene(root, 774, 581));
            stage.setResizable(false);



            MarcarConsultaPageController marcarConsultaPageController = loader.getController();
            marcarConsultaPageController.setCliente(cliente);


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openConsultasFuncionarioPage(Funcionario funcionario) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ConsultasFuncionarioPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Consultas do Funcionário");
            stage.setScene(new Scene(root, 600, 552));
            stage.setResizable(false);

            ConsultasFuncionarioPageController consultasFuncionarioPageController = loader.getController();
            consultasFuncionarioPageController.setFuncionario(funcionario);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSecretariadoConsultasPage(Funcionario funcionario) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("SecretariadoConsultasPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Consultas Do Consultorio");
            stage.setScene(new Scene(root, 600, 361));
            stage.setResizable(false);

            SecretariadoConsultasPageController controller = loader.getController();
            controller.setFuncionario(funcionario);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAdminPage(User usr) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("AdminPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Admin Page");
            stage.setScene(new Scene(root, 800, 600));
            stage.setResizable(false);

            AdminController adminPageController = loader.getController();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }






}
