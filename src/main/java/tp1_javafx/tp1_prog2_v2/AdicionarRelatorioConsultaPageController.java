package tp1_javafx.tp1_prog2_v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdicionarRelatorioConsultaPageController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField precoField;

    @FXML
    private TextArea descricaoArea;

    @FXML
    private ListView<String> servicosAdicionadosListView;

    private ObservableList<String> servicosAdicionadosList;

    private List<Map<String, Object>> resultados;

    @FXML
    private void initialize() {
        servicosAdicionadosList = FXCollections.observableArrayList();
        servicosAdicionadosListView.setItems(servicosAdicionadosList);
        resultados = new ArrayList<>();
    }

    @FXML
    private void adicionarServicoProduto() {
        String nome = nomeField.getText();
        String preco = precoField.getText();
        String descricao = descricaoArea.getText();

        if (!nome.isEmpty()) {
            servicosAdicionadosList.add(nome);
            resultados.add(Map.of("nome", nome, "preco", preco, "descricao", descricao));
            nomeField.clear();
            precoField.clear();
        }
    }

    @FXML
    private void confirmar() {
        nomeField.getScene().getWindow().hide();
        exibirAlertaSucesso("Confirmar Consulta", "Consulta Efetuada com Sucesso!");
    }

    @FXML
    private void cancelar() {
        resultados.clear();
        nomeField.getScene().getWindow().hide();
    }

    public List<Map<String, Object>> getResultados() {
        return resultados;
    }

    private void exibirAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


}
