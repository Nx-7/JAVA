package tp1_javafx.tp1_prog2_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tp1_javafx.tp1_prog2_v2.Classes.Consultorio;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Classes.EmpresaCuidadosDentarios;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;

public class AdicionarConsultorioPageController {

    private EmpresaCuidadosDentarios empresa;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField enderecoField;

    @FXML
    private TextField localidadeField;

    @FXML
    private TextField telefoneField;

    @FXML
    private TextField especialidadeField;

    @FXML
    private TextField precoConsultaField;


    private DonoEmpresaData donoEmpresaData;
    private DonoEmpresa donoLogado;



    public void setEmpresa(EmpresaCuidadosDentarios empresa) {
        this.empresa = empresa;
    }

    @FXML
    private void initialize() {

        donoEmpresaData = new DonoEmpresaData();
        this.donoEmpresaData.load();
        donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());


    }

    @FXML
    private void onAdicionarButtonClick(ActionEvent event) {
        String nome = nomeField.getText();
        String endereco = enderecoField.getText();
        String localidade = localidadeField.getText();
        String telefone = telefoneField.getText();
        String especialidade = especialidadeField.getText();

        Consultorio consultorio = new Consultorio(nome, endereco, localidade, telefone, especialidade);

        float precoConsulta = Float.parseFloat(precoConsultaField.getText());
        consultorio.setPrecoConsulta(especialidade, precoConsulta);

        empresa.getConsultorios().add(consultorio);


        boolean updated = donoLogado.updateConsultorios(empresa.getNome(), empresa.getConsultorios());

        if (updated) {
            exibirAlertaSucesso("Adicionar Consultório", "Consultório Adiconado com Sucesso!");
        } else {
            exibirAlertaErro("Erro ao Adicionar", "Erro ao adicionar Consultório");
        }

        donoEmpresaData.save();

        enderecoField.getScene().getWindow().hide();
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



