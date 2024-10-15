package tp1_javafx.tp1_prog2_v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import tp1_javafx.tp1_prog2_v2.Classes.*;
import tp1_javafx.tp1_prog2_v2.Files.ClienteData;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;

import java.util.*;

public class VisualizarConsultasPageController {

    @FXML
    private Label empresaLabel;

    @FXML
    private ComboBox<String> consultorioComboBox;

    @FXML
    private ListView<Consulta> consultasListView;

    private ObservableList<Consulta> consultasObservableList;

    private Set<Consultorio> consultorios;
    private DonoEmpresaData donoEmpresaData;
    private ClienteData clienteData;

    private DonoEmpresa donoEmpresa;
    private DonoEmpresa donoLogado;
    private String nomeEmpresa;
    private String nomeConsultorio;

    private EmpresaCuidadosDentarios empresa;

    public void setDonoEmpresa(DonoEmpresa donoEmpresa) {
        this.donoEmpresa = donoEmpresa;
    }

    public void setEmpresa(EmpresaCuidadosDentarios empresa) {
        this.empresa = empresa;
        consultorios = empresa.getConsultorios();

        // Configurar o texto da empresaLabel
        setEmpresaLabel(empresa.getNome());

        // iniciar os dados da ComboBox de consultórios
        initData(new ArrayList<>(empresa.getConsultorios()));
    }



    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();
        donoLogado = donoEmpresaData.getDonoEmpresaByUsername(Main.userlogged.getUsername());


    }
    public void setEmpresaLabel(String nomeEmpresa) {
        empresaLabel.setText(nomeEmpresa);
    }



    public void initData(List<Consultorio> consultorios) {
        consultasObservableList = FXCollections.observableArrayList();
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

        // Preencher a ComboBox com os nomes dos consultórios disponíveis
        consultorioComboBox.getItems().clear();
        for (Consultorio consultorio : consultorios) {
            consultorioComboBox.getItems().add(consultorio.getNome());
        }

    }

    @FXML
    private void onConsultorioComboBoxSelected() {
        String consultorioSelecionado = consultorioComboBox.getValue();
        if (consultorioSelecionado != null) {
            // obtem o consultório selecionado
            Consultorio consultorio = consultorios.stream()
                    .filter(c -> c.getNome().equals(consultorioSelecionado))
                    .findFirst()
                    .orElse(null);
            nomeConsultorio = consultorio.getNome();

            // mostra as consultas do consultório selecionado
            if (consultorio != null) {
                exibirConsultasDoConsultorio(consultorio);
            }
        }
    }



    private void exibirConsultasDoConsultorio(Consultorio consultorio) {
        // Obter a lista de consultas do consultório selecionado
        List<Consulta> consultasDoConsultorio = consultorio.getConsultas();

        // Limpar a ListView e adicionar as consultas do consultório selecionado
        consultasListView.getItems().clear();
        consultasListView.getItems().addAll(consultasDoConsultorio);
    }

    @FXML
    private void confirmarConsulta() {
        // obtem a consulta selecionada da lista
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();

        if (consultaSelecionada != null && consultaSelecionada.getEstado() == EstadoConsulta.PENDENTE) {
            consultaSelecionada.marcarComoConfirmada();


            boolean confirmarConsulta = donoLogado.atualizarEstadoConsulta(consultaSelecionada.getId(), empresa.getNome(), nomeConsultorio, EstadoConsulta.CONFIRMADA );


            if (confirmarConsulta) {
                System.out.println("Consulta confirmada com sucesso!");
                donoEmpresaData.save();
            } else {
                System.out.println("Erro ao confirmar Consulta!");
            }

            // Atualiza a lista de consultas
            updateConsultasList();


            showAlert("Consulta Confirmada", "A consulta foi Confirmada com sucesso.");
        } else {
            showAlert("Operação Inválida", "Selecione uma consulta pendente para Confirmada.");
        }
        donoEmpresaData.save();
    }


    @FXML
    private void anularConsulta() {
        // obtem a consulta selecionada da lista
        Consulta consultaSelecionada = consultasListView.getSelectionModel().getSelectedItem();

        if (consultaSelecionada != null && consultaSelecionada.getEstado() == EstadoConsulta.PENDENTE) {
            consultaSelecionada.marcarComoAnulada();


            boolean anularConsulta = donoLogado.atualizarEstadoConsulta(consultaSelecionada.getId(), empresa.getNome(), nomeConsultorio, EstadoConsulta.ANULADA );


            if (anularConsulta) {
                System.out.println("Consulta Anulada com sucesso!");
                donoEmpresaData.save();
            } else {
                System.out.println("Erro ao Anular a Consulta!");
            }

            // Atualiza a lista de consultas
            updateConsultasList();


            showAlert("Consulta Anulado", "A consulta foi Anulada com sucesso.");
        } else {
            showAlert("Operação Inválida", "Selecione uma consulta pendente para Anular.");
        }
        donoEmpresaData.save();
    }

    // atualiza a lista de consultas na interface do user
    private void updateConsultasList() {
        // Obtem a lista de consultas do consultorio que selecionamos
        String consultorioSelecionado = consultorioComboBox.getValue();
        Consultorio consultorio = consultorios.stream()
                .filter(c -> c.getNome().equals(consultorioSelecionado))
                .findFirst()
                .orElse(null);

        if (consultorio != null) {
            List<Consulta> consultasDoConsultorio = consultorio.getConsultas();

            // Limpar a lista observável e adicionar as consultas do consultório selecionado
            consultasObservableList.clear();
            consultasObservableList.addAll(consultasDoConsultorio);

            // Atualizar a ListView diretamente
            consultasListView.setItems(consultasObservableList);
        }
    }

    // Mostrar alert a confirmar
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        donoEmpresaData.save();
    }


    public void closeStage() {
    }


}
