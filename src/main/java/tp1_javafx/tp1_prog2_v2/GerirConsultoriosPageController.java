package tp1_javafx.tp1_prog2_v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tp1_javafx.tp1_prog2_v2.Classes.Consultorio;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Classes.EmpresaCuidadosDentarios;
import tp1_javafx.tp1_prog2_v2.Classes.Funcionario;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GerirConsultoriosPageController {

    @FXML
    private Button addConsultorio;

    @FXML
    private Button addFuncionario;

    @FXML
    private Label empresaLabel;

    @FXML
    private ComboBox<String> consultoriosSelect;

    @FXML
    private ListView<String> funcionariosListView;





    private DonoEmpresa donoEmpresa; // Certifique-se de definir o DonoEmpresa antes de exibir esta página
    private EmpresaCuidadosDentarios empresaSelecionada;
    public void setEmpresaSelecionada(EmpresaCuidadosDentarios empresa) {
        this.empresaSelecionada = empresa;
        loadConsultorios();
    }

    @FXML
    private void initialize() {
        // LOAD dos funcionarios
        consultoriosSelect.setOnAction(e -> {
            String consultorioSelecionado = consultoriosSelect.getValue();
            Optional<Consultorio> consultorioOptional = getConsultorioSelecionado(consultorioSelecionado);

            consultorioOptional.ifPresent(consultorio -> {
                // Obter a lista de funcionários associados ao consultório
                ObservableList<Funcionario> funcionarios = FXCollections.observableArrayList(consultorio.getFuncionarios());
                // Converter a lista de funcionários para uma lista de strings (por exemplo, usando os nomes)
                ObservableList<String> nomesFuncionarios = FXCollections.observableArrayList();
                for (Funcionario funcionario : funcionarios) {
                    nomesFuncionarios.add(funcionario.getNome() + " - " + funcionario.getTipo());
                }
                // Atualizar a ListView com os nomes dos funcionários
                funcionariosListView.setItems(nomesFuncionarios);
            });
        });
    }



    @FXML
    private void onAdicionarConsultorioButtonClick(ActionEvent event) {
        // Verificar se há uma empresa selecionada
        if (empresaSelecionada != null) {
            // Carregar o FXML da janela de adição de consultório
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdicionarConsultorioPage.fxml"));
            Parent root;
            try {
                root = loader.load();

                // Criar uma nova cena e estágio para a janela de adição de consultório
                Stage adicionarConsultorioStage = new Stage();
                adicionarConsultorioStage.setTitle("Adicionar Consultório");
                adicionarConsultorioStage.setScene(new Scene(root, 460, 386));
                adicionarConsultorioStage.setResizable(false);

                // Obtém o controlador da janela de adição de consultório
                AdicionarConsultorioPageController adicionarConsultorioController = loader.getController();

                // Configurar o controlador com a empresa selecionada
                adicionarConsultorioController.setEmpresa(empresaSelecionada);

                closeStage();
                adicionarConsultorioStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Optional<Consultorio> getConsultorioSelecionado(String nomeConsultorio) {
        if (donoEmpresa != null && empresaSelecionada != null) {
            Optional<EmpresaCuidadosDentarios> empresaOptional = donoEmpresa.getEmpresaPeloNome(empresaSelecionada.getNome());

            if (empresaOptional.isPresent()) {
                EmpresaCuidadosDentarios empresa = empresaOptional.get();
                Optional<Consultorio> consultorioOptional = empresa.getConsultorios().stream()
                        .filter(consultorio -> consultorio.getNome().equals(nomeConsultorio))
                        .findFirst();

                return consultorioOptional;
            }
        }

        return Optional.empty();
    }



    public void setDonoEmpresa(DonoEmpresa donoEmpresa) {
        this.donoEmpresa = donoEmpresa;
    }

    public void setEmpresaLabel(String nomeEmpresa) {
        empresaLabel.setText("Empresa - " + nomeEmpresa);


    }

    @FXML
    public void loadConsultorios() {
        consultoriosSelect.getItems().clear();

        Set<Consultorio> consultorios = empresaSelecionada.getConsultorios();

        consultoriosSelect.getItems().addAll(consultorios.stream().map(Consultorio::getNome).collect(Collectors.toList()));


    }


    @FXML
    private void onAdicionarFuncionarioButtonClick(ActionEvent event) {
        if (empresaSelecionada != null && consultoriosSelect.getValue() != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdicionarFuncionarioPage.fxml"));
            Parent root;
            try {
                root = loader.load();

                Stage adicionarFuncionarioStage = new Stage();
                adicionarFuncionarioStage.setTitle("Adicionar Funcionário");
                adicionarFuncionarioStage.setScene(new Scene(root, 414, 415));
                adicionarFuncionarioStage.setResizable(false);

                AdicionarFuncionarioPageController adicionarFuncionarioController = loader.getController();

                adicionarFuncionarioController.setDonoEmpresa(donoEmpresa);
                adicionarFuncionarioController.setConsultorioSelecionado(consultoriosSelect.getValue());
                adicionarFuncionarioController.setNomeEmpresa(empresaSelecionada.getNome());

                adicionarFuncionarioStage.show();
                closeStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeStage() {
        Stage stage = (Stage) addConsultorio.getScene().getWindow();
        stage.close();
    }
}
