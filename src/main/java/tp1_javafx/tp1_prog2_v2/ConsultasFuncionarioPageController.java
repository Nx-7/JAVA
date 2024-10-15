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

public class ConsultasFuncionarioPageController {

    @FXML
    private ListView<Consulta> consultasListView;

    @FXML
    private Button efetuarConsultaButton;

    @FXML
    private Label servicosLabel;

    @FXML
    private TextArea descricaoLabel;


    private Funcionario funcionarioLogado;
    private DonoEmpresaData donoEmpresaData;
    private Funcionario funcionario;

    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();
        funcionarioLogado = donoEmpresaData.getFuncionarioByUsername(Main.userlogged.getUsername());

        carregarConsultasFuncionario();

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

                    // Defina cores com base no estado da consulta
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

            if (newValue != null) {
                servicosLabel.setText("Serviços: " + getServicosString(newValue));
                descricaoLabel.setText("Descrição: " + newValue.getDescricao());
            } else {
                servicosLabel.setText("Serviços: ");
                descricaoLabel.setText("Descrição: ");
            }
        });

    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        carregarConsultasFuncionario();
    }

    private void carregarConsultasFuncionario() {
        List<Consulta> consultasFuncionario = funcionarioLogado.getConsultasAssociadas();
        ObservableList<Consulta> consultasObservableList = FXCollections.observableArrayList(consultasFuncionario);
        consultasListView.setItems(consultasObservableList);

        consultasListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            efetuarConsultaButton.setDisable(newValue == null || newValue.getEstado() == EstadoConsulta.ANULADA  || newValue.getEstado() == EstadoConsulta.EFETUADA);
        });
    }

    @FXML
    private void onEfetuarConsultaButtonClick() {
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();
        if (consultaSelecionada != null) {
            if (consultaSelecionada.getEstado() != EstadoConsulta.CONFIRMADA) {
                showAlert("Erro Na Consulta", "A consulta tem que ser primeiro Confirmada pelo Secretariado.");
            } else if (consultaSelecionada.getEstado() == EstadoConsulta.EFETUADA) {
                showAlert("Erro Na Consulta", "A Consulta já foi Efetuada!");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdicionarRelatorioPage.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setTitle("Informações da Consulta");

                try {
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);

                    AdicionarRelatorioConsultaPageController controller = loader.getController();
                    stage.showAndWait();

                    List<Map<String, Object>> resultados = controller.getResultados();
                    if (!resultados.isEmpty()) {
                        for (Map<String, Object> map : resultados) {
                            String nome = (String) map.get("nome");
                            String precoStr = (String) map.get("preco");
                            String descricao = (String) map.get("descricao");

                            try {
                                Float preco = Float.parseFloat(precoStr);

                                // Atualizar a consulta com as informações
                                consultaSelecionada.getProdutosServicosComplementares().put(nome, preco);
                                float novoPreco = consultaSelecionada.getPreco() + preco;
                                consultaSelecionada.setPreco(novoPreco);
                                consultaSelecionada.setDescricao(descricao);
                            } catch (NumberFormatException e) {
                                showAlert("Erro", "Digite um valor numérico válido para o preço.");
                            }
                        }

                        consultaSelecionada.marcarComoEfetuada();
                        donoEmpresaData.save();
                        carregarConsultasFuncionario();
                        showAlert("Consulta Efetuada", "A consulta foi marcada como efetuada com sucesso.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void onCancelarConsultaButtonClick() {
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();
        if (consultaSelecionada != null) {
            if (consultaSelecionada.getEstado() != EstadoConsulta.CONFIRMADA) {
                showAlert("Erro no Cancelamento", "A consulta deve estar confirmada para ser cancelada.");
            } else {
                String razaoCancelamento = showInputDialog("Cancelar Consulta", "Digite a razão para o cancelamento:");

                if (razaoCancelamento != null && !razaoCancelamento.isEmpty()) {
                    consultaSelecionada.setEstado(EstadoConsulta.ANULADA);
                    consultaSelecionada.setDescricao("Consulta cancelada. Razão: " + razaoCancelamento);
                    donoEmpresaData.save();
                    carregarConsultasFuncionario();
                    showAlert("Consulta Cancelada", "A consulta foi cancelada com sucesso.");
                } else {
                    showAlert("Erro no Cancelamento", "Digite uma razão válida para o cancelamento.");
                }
            }
        }
    }



    private String getServicosString(Consulta consulta) {
        StringBuilder servicos = new StringBuilder();
        Map<String, Float> produtosServicos = consulta.getProdutosServicosComplementares();

        for (Map.Entry<String, Float> entry : produtosServicos.entrySet()) {
            servicos.append(entry.getKey()).append(" (").append(entry.getValue()).append("), ");
        }

        return servicos.length() > 0 ? servicos.substring(0, servicos.length() - 2) : "";
    }




    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String showInputDialog(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}