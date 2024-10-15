package tp1_javafx.tp1_prog2_v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.Consulta;
import tp1_javafx.tp1_prog2_v2.Classes.EstadoConsulta;
import tp1_javafx.tp1_prog2_v2.Classes.Funcionario;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;

import java.io.IOException;
import java.util.*;

public class SecretariadoConsultasPageController {

    @FXML
    private ListView<Consulta> consultasListView;

    @FXML
    private Button efetuarConsultaButton;




    private Funcionario funcionarioLogado;
    private DonoEmpresaData donoEmpresaData;
    private Funcionario funcionario;

    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();
        funcionarioLogado = donoEmpresaData.getFuncionarioByUsername(Main.userlogged.getUsername());

        carregarConsultasConsultorio();

        consultasListView.setCellFactory(listView -> new ListCell<Consulta>() {
            @Override
            protected void updateItem(Consulta consulta, boolean empty) {
                super.updateItem(consulta, empty);

                if (empty || consulta == null) {
                    setText(null);
                    setStyle("");
                    setBorder(null);
                } else {
                    setText(consulta.toString());

                    switch (consulta.getEstado()) {
                        case PENDENTE:
                            setStyle("-fx-background-color: rgba(255,255,0,0.2);");
                            break;
                        case CONFIRMADA:
                            setStyle("-fx-background-color: rgba(153,255,153,0.2);");
                            break;
                        case ANULADA:
                            setStyle("-fx-background-color: rgba(255,153,153,0.2);");
                            break;
                        case EFETUADA:
                            setStyle("-fx-background-color: rgba(26,93,0,0.3);");
                            break;
                    }

                    // linha da border
                    setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                }
            }
        });

        efetuarConsultaButton.setDisable(true);

        consultasListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            efetuarConsultaButton.setDisable(newValue == null || newValue.getEstado() == EstadoConsulta.ANULADA || newValue.getEstado() == EstadoConsulta.EFETUADA);

        });

    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        carregarConsultasConsultorio();
    }

    private void carregarConsultasConsultorio() {
        List<Consulta> consultasFuncionario = funcionarioLogado.getConsultasDoConsultorio();
        ObservableList<Consulta> consultasObservableList = FXCollections.observableArrayList(consultasFuncionario);
        consultasListView.setItems(consultasObservableList);

        consultasListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            efetuarConsultaButton.setDisable(newValue == null || newValue.getEstado() == EstadoConsulta.ANULADA  || newValue.getEstado() == EstadoConsulta.EFETUADA);
        });
    }

    @FXML
    private void onEfetuarConsultaButtonClick() {
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();
        if (consultaSelecionada != null && consultaSelecionada.getEstado() == EstadoConsulta.PENDENTE) {
            consultaSelecionada.marcarComoConfirmada();
            donoEmpresaData.save();
            updateConsultasList();


            showAlert("Consulta Confirmada", "A consulta foi Confirmada com sucesso.");
        } else {
            showAlert("Operação Inválida", "Selecione uma consulta pendente para Confirmada.");
        }
    }

    @FXML
    private void onCancelarConsultaButtonClick() {
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();
        if (consultaSelecionada != null && consultaSelecionada.getEstado() == EstadoConsulta.PENDENTE) {
            consultaSelecionada.marcarComoAnulada();
            donoEmpresaData.save();
            updateConsultasList();

            showAlert("Consulta Confirmada", "A consulta foi Confirmada com sucesso.");
        } else {
            showAlert("Operação Inválida", "Selecione uma consulta pendente para Confirmada.");
        }
    }

    private void updateConsultasList() {
        // Obter a lista de consultas de todos os consultórios
        List<Consulta> consultasFuncionario = funcionarioLogado.getConsultasDoConsultorio();
        ObservableList<Consulta> consultasObservableList = FXCollections.observableArrayList(consultasFuncionario);
        consultasListView.setItems(consultasObservableList);

        consultasListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            efetuarConsultaButton.setDisable(newValue == null || newValue.getEstado() == EstadoConsulta.ANULADA  || newValue.getEstado() == EstadoConsulta.EFETUADA);
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}