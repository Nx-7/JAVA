package tp1_javafx.tp1_prog2_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.Consulta;
import tp1_javafx.tp1_prog2_v2.Classes.Consultorio;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Classes.EmpresaCuidadosDentarios;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DonoEmpresaMenuController {

    @FXML
    private Pane rootPane;  // Adicione essa linha para referenciar o Pane

    @FXML
    private Label nomeDonoLabel;

    @FXML
    private GridPane frameDonoEmpresaMenu;

    @FXML
    private Button addEmpresas;

    @FXML
    private ComboBox<String> empresasSelect;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField moradaField;

    @FXML
    private TextField localidadeField;

    @FXML
    private TextField telefoneField;

    @FXML
    private Button addConsultorio;

    @FXML
    private Label isAtivoLabel;

    private DonoEmpresa donoEmpresa;
    private DonoEmpresaData donoEmpresaData;
    private EmpresaCuidadosDentarios empresaSelecionada;

    @FXML
    private void initialize() {

        donoEmpresaData = new DonoEmpresaData();
        this.donoEmpresaData.load();

        DonoEmpresa donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());


        nomeDonoLabel.setText("Bem Vindo -  " + donoLogado.getNome() + "!");
        empresasSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                frameDonoEmpresaMenu.setVisible(true);
            } else {
                frameDonoEmpresaMenu.setVisible(false);
            }
        });
        visibilidadeInicial();


        addEmpresas.setOnAction(this::onAddEmpresaButtonClick);
        empresasSelect.setOnAction(this::onEmpresaSelected);
        addConsultorio.setOnAction(this::onGerirConsultoriosButtonClick);
    }

    private void visibilidadeInicial() {
        frameDonoEmpresaMenu.setVisible(false);
    }

    private void onEmpresaSelected(ActionEvent e) {
        String nomeEmpresaSelecionada = empresasSelect.getValue();
        Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresaPeloNome(nomeEmpresaSelecionada);



        empresaOptional.ifPresent(empresa -> {

            if (empresa.isAtivo() == false) {
                isAtivoLabel.setText("Desativada!");
                isAtivoLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else {
                isAtivoLabel.setText("Ativa!");
                isAtivoLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }


            nomeField.setText(empresa.getNome());
            moradaField.setText(empresa.getMorada());
            localidadeField.setText(empresa.getLocalidade());
            telefoneField.setText(empresa.getNumeroTelefone());

            // so mostrar se a empresa for selecionada
            boolean mostrarControlosAdicionais = (nomeEmpresaSelecionada != null);
            nomeField.setVisible(mostrarControlosAdicionais);
            moradaField.setVisible(mostrarControlosAdicionais);
            localidadeField.setVisible(mostrarControlosAdicionais);
            telefoneField.setVisible(mostrarControlosAdicionais);


        });
    }

    @FXML
    private void onGuardarAlteracoesButtonClick(ActionEvent event) {
        String nomeEmpresaSelecionada = empresasSelect.getValue();
        Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresaPeloNome(nomeEmpresaSelecionada);

        empresaOptional.ifPresent(empresa -> {
            empresa.setNome(nomeField.getText());
            empresa.setMorada(moradaField.getText());
            empresa.setLocalidade(localidadeField.getText());
            empresa.setNumeroTelefone(telefoneField.getText());

            loadEmpresas();

            exibirAlertaSucesso("Guardar Alterações", "Dados da Empresa Alterados com Sucesso!");



            donoEmpresaData.save();
            // atualiza dados no fichiero
            DonoEmpresa donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());
            if (donoLogado != null) {
                donoLogado.atualizarEmpresa(empresa, donoEmpresaData);
                donoEmpresaData.save();
            }

        });
    }


    @FXML
    private void onAddEmpresaButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdicionarEmpresaPage.fxml"));
            Parent root = loader.load();

            Stage adicionarEmpresaStage = new Stage();
            adicionarEmpresaStage.setTitle("Adicionar Empresa");
            adicionarEmpresaStage.setScene(new Scene(root, 436, 416));
            adicionarEmpresaStage.setResizable(false);

            AdicionarEmpresaPageController adicionarEmpresaController = loader.getController();
            adicionarEmpresaController.setDonoEmpresa(donoEmpresa);

            adicionarEmpresaStage.setOnCloseRequest(e -> adicionarEmpresaController.closeStage());

            Stage stage = (Stage) frameDonoEmpresaMenu.getScene().getWindow();
            stage.close();
            adicionarEmpresaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void setDonoEmpresa(DonoEmpresa donoEmpresa) {
        this.donoEmpresa = donoEmpresa;
        loadEmpresas();
    }

    @FXML
    void loadEmpresas() {
        // limpa a combobox antes de adicionar as empresa
        empresasSelect.getItems().clear();

        // obtem a lista atualizada das empresas
        List<String> nomesEmpresas = donoEmpresa.getNomesEmpresas();

        // adicionar essas empresas a comobox
        empresasSelect.getItems().addAll(nomesEmpresas);


    }

    @FXML
    private void onGerirConsultoriosButtonClick(ActionEvent event) {
        String nomeEmpresaSelecionada = empresasSelect.getValue();
        Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresaPeloNome(nomeEmpresaSelecionada);

        empresaOptional.ifPresent(empresa -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GerirConsultoriosPage.fxml"));
                Parent root = loader.load();


                Stage gerirConsultoriosStage = new Stage();
                gerirConsultoriosStage.setTitle("Gerir Consultórios");
                gerirConsultoriosStage.setScene(new Scene(root, 510, 449));
                gerirConsultoriosStage.setResizable(false);

                GerirConsultoriosPageController gerirConsultoriosController = loader.getController();
                gerirConsultoriosController.setDonoEmpresa(donoEmpresa);
                gerirConsultoriosController.setEmpresaSelecionada(empresa); // passar empresa selecionada
                gerirConsultoriosController.setEmpresaLabel(nomeEmpresaSelecionada);

                gerirConsultoriosStage.setOnCloseRequest(e -> gerirConsultoriosController.closeStage());

                gerirConsultoriosStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onVisualizarConsultasButtonClick(ActionEvent event) {
        String nomeEmpresa = empresasSelect.getValue();


        if (nomeEmpresa != null) {
            Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresaPeloNome(nomeEmpresa);

            empresaOptional.ifPresent(empresa -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("VerificarConsultasPage.fxml"));
                    Parent root = loader.load();

                    Stage visualizarConsultasStage = new Stage();
                    visualizarConsultasStage.setTitle("Visualizar Consultas");
                    visualizarConsultasStage.setScene(new Scene(root, 600, 400));
                    visualizarConsultasStage.setResizable(false);

                    VisualizarConsultasPageController visualizarConsultasController = loader.getController();

                    // passa a empresa completa para o controlador
                    visualizarConsultasController.setDonoEmpresa(donoEmpresa);
                    visualizarConsultasController.setEmpresa(empresa);

                    visualizarConsultasStage.setOnCloseRequest(e -> visualizarConsultasController.closeStage());

                    visualizarConsultasStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {

            System.out.println("Selecione uma empresa antes de ver as consultas.");
        }
    }

    private void exibirAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


    public void closeStage() {
    }
}