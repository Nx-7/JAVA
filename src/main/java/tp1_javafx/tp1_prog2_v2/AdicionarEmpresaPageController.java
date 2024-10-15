package tp1_javafx.tp1_prog2_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Classes.EmpresaCuidadosDentarios;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import tp1_javafx.tp1_prog2_v2.Main;

import java.io.IOException;

public class AdicionarEmpresaPageController {

    @FXML
    private TextField nomeEmpresa;

    @FXML
    private TextField moradaEmpresa;

    @FXML
    private TextField localidadeEmpresa;

    @FXML
    private TextField telefoneEmpresa;

    @FXML
    private Button addEmpresaButton;

    @FXML
    private Button closeAddEmpresa;

    private DonoEmpresa donoEmpresa;
    private DonoEmpresaData donoEmpresaData;




    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();



        addEmpresaButton.setOnAction(this::onAddEmpresaButtonClick);
    }

    @FXML
    private void onAddEmpresaButtonClick(ActionEvent event) {
        String nome = nomeEmpresa.getText();
        String morada = moradaEmpresa.getText();
        String localidade = localidadeEmpresa.getText();
        String telefone = telefoneEmpresa.getText();


        DonoEmpresa donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());


        if (donoLogado != null) {
            EmpresaCuidadosDentarios novaEmpresa = new EmpresaCuidadosDentarios(nome, morada, localidade, telefone);
            donoLogado.adicionarEmpresa(novaEmpresa);


            donoEmpresaData.save();

            novaEmpresa.adicionarAoMapaDonoEmpresa(donoLogado);

            donoEmpresaData.save();


            clearFields();

            Stage stage = (Stage) closeAddEmpresa.getScene().getWindow();
            stage.close();

            openDonoMenu();


        } else {

            showAlert("Erro", "Dono de Empresa não encontrado.");
        }
    }

    @FXML
    private void onCloseAddEmpresa(ActionEvent event) {
        Stage stage = (Stage) closeAddEmpresa.getScene().getWindow();
        stage.close();
        openDonoMenu();

    }

    private void clearFields() {
        nomeEmpresa.setText("");
        moradaEmpresa.setText("");
        localidadeEmpresa.setText("");
        telefoneEmpresa.setText("");
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void setDonoEmpresa(DonoEmpresa donoEmpresa) {
        this.donoEmpresa = donoEmpresa;
    }

    public void closeStage() {

    }

    private void openDonoMenu() {
        DonoEmpresa donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DonoEmpresaMenuPage.fxml"));
            Parent root = loader.load();

            Stage menuDonoStage = new Stage();
            menuDonoStage.setTitle("Menu do Dono");
            menuDonoStage.setScene(new Scene(root, 556, 466));
            menuDonoStage.setResizable(false);

            DonoEmpresaMenuController menuDonoController = loader.getController();
            menuDonoController.setDonoEmpresa(donoLogado); //

            menuDonoStage.setOnCloseRequest(e -> menuDonoController.closeStage());

            menuDonoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
