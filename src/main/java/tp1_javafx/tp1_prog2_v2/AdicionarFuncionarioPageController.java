package tp1_javafx.tp1_prog2_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.*;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import tp1_javafx.tp1_prog2_v2.Files.UserData;
import tp1_javafx.tp1_prog2_v2.Models.Role;
import tp1_javafx.tp1_prog2_v2.Models.User;

import java.io.IOException;
import java.util.Optional;

public class AdicionarFuncionarioPageController {

    @FXML
    private TextField nome;

    @FXML
    private TextField numCC;

    @FXML
    private TextField numFiscal;

    @FXML
    private TextField telemovel;

    @FXML
    private TextField morada;

    @FXML
    private TextField localidade;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> tipoComboBox;

    @FXML
    private ComboBox<String> consultoriosComboBox;

    private String nomeEmpresa;
    private DonoEmpresa donoEmpresa;
    private String consultorioSelecionado;
    private UserData userData;
    private DonoEmpresaData donoEmpresaData;
    private DonoEmpresa donoLogado;
    private EmpresaCuidadosDentarios empresaAtual;

    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();

        userData = new UserData();
        userData.load();

        donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }


    public void setDonoEmpresa(DonoEmpresa donoEmpresa) {
        this.donoEmpresa = donoEmpresa;
    }

    public void setConsultorioSelecionado(String consultorioSelecionado) {
        this.consultorioSelecionado = consultorioSelecionado;
    }

    @FXML
    private void onAdicionarFuncionarioButtonClick(ActionEvent event) {
        if (nomeEmpresa != null && consultorioSelecionado != null) {
            Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresaPeloNome(nomeEmpresa);

            empresaOptional.ifPresent(empresa -> {
                Consultorio consultorio = empresa.getConsultorioPeloNome(consultorioSelecionado);

                if (camposValidos() && consultorio != null) {
                    Funcionario novoFuncionario = new Funcionario(
                            nome.getText(),
                            numCC.getText(),
                            numFiscal.getText(),
                            telemovel.getText(),
                            morada.getText(),
                            localidade.getText()
                    );

                    novoFuncionario.setTipo(TipoFuncionario.valueOf(tipoComboBox.getValue()));



                    // Adicionar funcionário ao consultório
                    boolean addFuncionario = donoLogado.associarFuncionariosAoConsultorio(nomeEmpresa, consultorioSelecionado, novoFuncionario);

                    if (addFuncionario) {
                        exibirAlertaSucesso("Funcionario Adicionado", "Funcionario Adicionado com sucesso!");

                        donoEmpresaData.save();

                    } else {
                        exibirAlertaErro("Falha ao Adicionar", "Falha ao Adicionar Funcionário!");

                    }


                    donoEmpresaData.save();

                    User novaConta = new User(nome.getText(), password.getText(), Role.Funcionario);

                    userData.register(novaConta);
                    openConsultorioMenu(empresa, consultorioSelecionado);

                    closeStage();




                }
            });
        }
    }


    private boolean camposValidos() {
        return !nome.getText().isEmpty() &&
                !numCC.getText().isEmpty() &&
                !numFiscal.getText().isEmpty() &&
                !telemovel.getText().isEmpty() &&
                !morada.getText().isEmpty() &&
                !localidade.getText().isEmpty() &&
                !password.getText().isEmpty() &&
                tipoComboBox.getValue() != null;
    }

    public void closeStage() {
        Stage stage = (Stage) nome.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void openConsultorioMenu(EmpresaCuidadosDentarios empresa, String consultorioSelecionado) {
        empresaAtual = empresa;

        DonoEmpresa donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GerirConsultoriosPage.fxml"));
            Parent root = loader.load();

            Stage menuDonoStage = new Stage();
            menuDonoStage.setTitle("Gerir Consultorios");
            menuDonoStage.setScene(new Scene(root, 510, 449));
            menuDonoStage.setResizable(false);

            GerirConsultoriosPageController gerirConsultoriosPageController = loader.getController();
            gerirConsultoriosPageController.setDonoEmpresa(donoLogado);
            gerirConsultoriosPageController.setEmpresaSelecionada(empresa);
            gerirConsultoriosPageController.setEmpresaLabel(nomeEmpresa);
            gerirConsultoriosPageController.loadConsultorios();

            menuDonoStage.setOnCloseRequest(e -> gerirConsultoriosPageController.closeStage());

            menuDonoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exibirAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
