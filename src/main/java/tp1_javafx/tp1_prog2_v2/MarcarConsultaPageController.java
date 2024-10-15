package tp1_javafx.tp1_prog2_v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import tp1_javafx.tp1_prog2_v2.Classes.*;
import tp1_javafx.tp1_prog2_v2.Files.ClienteData;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MarcarConsultaPageController {

    @FXML
    private ComboBox<String> empresaComboBox;

    @FXML
    private ComboBox<String> consultorioComboBox;

    @FXML
    private ComboBox<String> tipoConsultaComboBox;

    @FXML
    private ComboBox<String> medicoComboBox;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private Spinner<Integer> spinnerHora;

    @FXML
    private Spinner<Integer> spinnerMinuto;

    @FXML
    private ListView<Consulta> consultasListView;

    @FXML
    private Button pagamentoButton;

    @FXML
    private Label precoLabel;

    private Cliente cliente;


    private DonoEmpresa donoEmpresa;
    private DonoEmpresaData donoEmpresaData;
    private ClienteData clienteData;



    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();



        clienteData = new ClienteData();
        clienteData.load();

        loadEmpresas();
        loadConsultorios();
        loadMedicos();

        empresaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadConsultorios();
        });

        consultorioComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadMedicos();
        });

        spinnerHora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        spinnerHora.setEditable(true);

        // Configurar o Spinner de Minuto
        spinnerMinuto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        spinnerMinuto.setEditable(true);



        consultasListView.setVisible(false);
        pagamentoButton.setVisible(false);

    }

    void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @FXML
    private void onAgendarConsultaButtonClick(ActionEvent event) {
        String empresaSelecionada = empresaComboBox.getValue();
        String consultorioSelecionado = consultorioComboBox.getValue();
        String medicoSelecionado = medicoComboBox.getValue();
        String dataSelecionada = dataPicker.getValue().toString();
        int horaSelecionada = spinnerHora.getValue();
        int minutoSelecionado = spinnerMinuto.getValue();

        donoEmpresaData.load();
        clienteData.load();

        // Encontrar o dono que possui a empresa selecionada
        Optional<DonoEmpresa> donoEmpresaOptional = donoEmpresaData.getDonosEmpresa().stream()
                .filter(d -> d.getEmpresas().stream().anyMatch(e -> e.getNome().equals(empresaSelecionada)))
                .findFirst();

        donoEmpresaOptional.ifPresent(donoEmpresa -> {
            // Encontrar a empresa selecionada
            Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresas().stream()
                    .filter(e -> e.getNome().equals(empresaSelecionada))
                    .findFirst();

            empresaOptional.ifPresent(empresa -> {
                // Encontrar o consultório selecionado
                Consultorio consultorioSelecionadoObj = empresa.getConsultorioPelaEspecialidade(consultorioSelecionado);

                if (consultorioSelecionadoObj != null) {
                    // Encontrar o médico (funcionário) selecionado
                    Funcionario medicoSelecionadoObj = consultorioSelecionadoObj.getFuncionarioPeloNome(medicoSelecionado);

                    if (medicoSelecionadoObj != null) {
                        // criar uma nova consulta
                        float preco = consultorioSelecionadoObj.getPrecoConsulta(consultorioSelecionadoObj.getEspecialidade());
                        Consulta novaConsulta = new Consulta(medicoSelecionadoObj, LocalDate.parse(dataSelecionada), LocalTime.of(horaSelecionada, minutoSelecionado), cliente, preco);

                        // adicona consulta ao consultorio
                        consultorioSelecionadoObj.agendarConsulta(novaConsulta);



                        donoEmpresaData.save();

                        exibirAlertaSucesso("Consulta Agendada", "Consulta agendada com sucesso!");

                        limparCampos();
                        consultasListView.setVisible(false);
                        pagamentoButton.setVisible(false);

                    }
                }
            });
        });
    }

    @FXML
    private void loadEmpresas() {
        DonoEmpresaData donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();

        // devolve a lista de todos os donos de empresa
        List<DonoEmpresa> donosEmpresa = donoEmpresaData.getDonosEmpresa();

        // Preenche a ComboBox de empresas com todas as empresas de todos os donos
        ObservableList<String> empresasList = FXCollections.observableArrayList();
        for (DonoEmpresa donoEmpresa : donosEmpresa) {
            for (EmpresaCuidadosDentarios empresa : donoEmpresa.getEmpresas()) {
                if (empresa.isAtivo()) {
                    empresasList.add(empresa.getNome());
                }
            }
        }
        empresaComboBox.setItems(empresasList);
    }

    @FXML
    private void loadConsultorios() {
        String empresaSelecionada = empresaComboBox.getValue();



        if (empresaSelecionada != null) {
            consultorioComboBox.getItems().clear();

            DonoEmpresaData donoEmpresaData = new DonoEmpresaData();
            donoEmpresaData.load();

            List<DonoEmpresa> donosEmpresa = donoEmpresaData.getDonosEmpresa();

            // Encontrar o dono que possui a empresa selecionada
            Optional<DonoEmpresa> donoEmpresaOptional = donosEmpresa.stream()
                    .filter(d -> d.getEmpresas().stream().anyMatch(e -> e.getNome().equals(empresaSelecionada)))
                    .findFirst();

            donoEmpresaOptional.ifPresent(donoEmpresa -> {
                // devolve listas empresas dono
                List<EmpresaCuidadosDentarios> empresas = donoEmpresa.getEmpresas();

                // Encontra a empresa selecionada
                Optional<EmpresaCuidadosDentarios> empresaOptional = empresas.stream()
                        .filter(e -> e.getNome().equals(empresaSelecionada))
                        .findFirst();

                empresaOptional.ifPresent(empresa -> {
                    // deveolve a lista de consultórios da empresa
                    Set<Consultorio> consultorios = empresa.getConsultorios();

                    // Preenche a ComboBox de consultórios
                    for (Consultorio consultorio : consultorios) {
                        consultorioComboBox.getItems().add(consultorio.getEspecialidade());
                    }

                });
            });
        }
    }


    @FXML
    private void loadMedicos() {
        String empresaSelecionada = empresaComboBox.getValue();
        String consultorioSelecionado = consultorioComboBox.getValue();

        if (empresaSelecionada != null && consultorioSelecionado != null) {
            // Limpar dados antigos
            medicoComboBox.getItems().clear();

            DonoEmpresaData donoEmpresaData = new DonoEmpresaData();
            donoEmpresaData.load();

            List<DonoEmpresa> donosEmpresa = donoEmpresaData.getDonosEmpresa();

            // Encontrar o dono que possui a empresa selecionada
            Optional<DonoEmpresa> donoEmpresaOptional = donosEmpresa.stream()
                    .filter(d -> d.getEmpresas().stream().anyMatch(e -> e.getNome().equals(empresaSelecionada)))
                    .findFirst();

            donoEmpresaOptional.ifPresent(donoEmpresa -> {
                // Obter a lista de empresas do dono
                List<EmpresaCuidadosDentarios> empresas = donoEmpresa.getEmpresas();

                // Encontrar a empresa selecionada
                Optional<EmpresaCuidadosDentarios> empresaOptional = empresas.stream()
                        .filter(e -> e.getNome().equals(empresaSelecionada))
                        .findFirst();

                empresaOptional.ifPresent(empresa -> {
                    // Encontrar o consultório selecionado
                    Consultorio consultorioSelecionadoObj = empresa.getConsultorioPelaEspecialidade(consultorioSelecionado);

                    if (consultorioSelecionadoObj != null) {
                        float precoConsulta = consultorioSelecionadoObj.getPrecoConsulta(consultorioSelecionadoObj.getEspecialidade());
                        precoLabel.setText("Preço da Consulta: " + precoConsulta + "€");
                        // Obter a lista de funcionários associados ao consultório
                        List<Funcionario> funcionarios = consultorioSelecionadoObj.getFuncionarios();


                        for (Funcionario funcionario : funcionarios) {
                            if (funcionario.getTipo() != TipoFuncionario.SECRETARIADO) {
                                medicoComboBox.getItems().add(funcionario.getNome());
                            }
                        }
                    }
                });
            });
        }
    }

    @FXML
    private void onVisualizarConsultasButtonClick(ActionEvent event) {
        consultasListView.setVisible(true);
        pagamentoButton.setVisible(true);

        if (cliente != null) {
            List<Consulta> consultasDoCliente = new ArrayList<>();

            donoEmpresaData.load();

            // Percorrer todas as empresas
            for (DonoEmpresa donoEmpresa : donoEmpresaData.getDonosEmpresa()) {
                for (EmpresaCuidadosDentarios empresa : donoEmpresa.getEmpresas()) {
                    // Percorrer todos os consultórios da empresa
                    for (Consultorio consultorio : empresa.getConsultorios()) {
                        // Percorrer todas as consultas do consultório
                        for (Consulta consulta : consultorio.getConsultas()) {
                            // Verificar se o cliente está associado à consulta
                            if (consulta.getCliente() != null && consulta.getCliente().getNome().equals(cliente.getNome())) {
                                consultasDoCliente.add(consulta);
                                consulta.exibirDetalhes();
                            }
                        }
                    }
                }
            }

            if (!consultasDoCliente.isEmpty()) {
                consultasListView.getItems().clear();
                consultasListView.getItems().addAll(consultasDoCliente);

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
            } else {
                // se nao tiver consultas
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sem Consultas Marcadas");
                alert.setHeaderText("Ainda não marcou nenhuma consulta.");
                alert.showAndWait();
            }
        }

    }


    @FXML
    private void onPagarConsultaButtonClick(ActionEvent event) {
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();

        if (consultaSelecionada != null) {

            if (consultaSelecionada.getEstado() == EstadoConsulta.CONFIRMADA || consultaSelecionada.getEstado() == EstadoConsulta.EFETUADA) {
                if (!consultaSelecionada.isPago()) {
                    float valorConsulta = consultaSelecionada.getPreco();

                    Alert confirmacaoAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacaoAlert.setTitle("Confirmação de Pagamento");
                    confirmacaoAlert.setHeaderText(null);
                    confirmacaoAlert.setContentText("Você está prestes a efetuar o pagamento da consulta.\n\nValor a pagar: " + valorConsulta + "€\n\nDeseja continuar?");

                    Optional<ButtonType> result = confirmacaoAlert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        consultaSelecionada.marcarComoPago();
                        donoEmpresaData.save();

                        exibirAlertaSucesso("Pagamento Confirmado", "Pagamento da consulta confirmado com sucesso!");

                        // Atualizar a ListView com as consultas do cliente
                        onVisualizarConsultasButtonClick(null);
                    }
                } else {
                    exibirAlertaErro("Consulta Já Paga", "A consulta já está paga.");
                }
            } else {
                exibirAlertaErro("Consulta não Confirmada", "A consulta precisa estar confirmada ou efetuada para realizar o pagamento.");
            }
        } else {
            exibirAlertaErro("Consulta não Selecionada", "Por favor, selecione uma consulta para confirmar o pagamento.");
        }
    }

    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        // Limpa os campos do formulário
        empresaComboBox.setValue(null);
        consultorioComboBox.getItems().clear();
        medicoComboBox.getItems().clear();
        dataPicker.setValue(null);
        spinnerHora.getValueFactory().setValue(0);
        spinnerMinuto.getValueFactory().setValue(0);
    }




}
