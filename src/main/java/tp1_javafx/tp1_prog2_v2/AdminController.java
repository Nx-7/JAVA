package tp1_javafx.tp1_prog2_v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tp1_javafx.tp1_prog2_v2.Classes.Consulta;
import tp1_javafx.tp1_prog2_v2.Classes.Consultorio;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;
import tp1_javafx.tp1_prog2_v2.Classes.EmpresaCuidadosDentarios;
import tp1_javafx.tp1_prog2_v2.Models.User;
import tp1_javafx.tp1_prog2_v2.Models.Role;
import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;
import tp1_javafx.tp1_prog2_v2.Files.UserData;

public class AdminController {

    @FXML
    private TextField novoAdminUsername;

    @FXML
    private TextField novoAdminPassword;

    @FXML
    private Button criarAdminButton;

    @FXML
    private ComboBox<String> empresaComboBox;

    @FXML
    private Button desativarEmpresaButton;

    @FXML
    private ComboBox<String> ativarEmpresaComboBox;

    @FXML
    private Button ativarEmpresaButton;

    @FXML
    private Label receitaLabel;

    @FXML
    private VBox consultoriosVBox;

    @FXML
    private ComboBox<String> todasEmpresasComboBox;

    @FXML
    private Label receitaTotalTodasEmpresasLabel;

    @FXML
    private Label receitaDescontadaLabel;

    private DonoEmpresaData donoEmpresaData;
    private UserData userData;



    @FXML
    private void initialize() {
        donoEmpresaData = new DonoEmpresaData();
        donoEmpresaData.load();

        userData = new UserData();
        userData.load();

        initializeEmpresaComboBox();
        initializeAtivarEmpresaComboBox();
        initializeTodasEmpresasComboBox();
        calcularEExibirReceitaTotalTodasEmpresas();
    }

    private void initializeEmpresaComboBox() {
        empresaComboBox.getItems().clear();

        // obtem empresas ativas
        for (DonoEmpresa dono : donoEmpresaData.getDonosEmpresa()) {
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                if (empresa.isAtivo()) {
                    empresaComboBox.getItems().add(empresa.getNome());
                }
            }
        }
    }

    private void initializeAtivarEmpresaComboBox() {
        ativarEmpresaComboBox.getItems().clear();

        // obtem empresas desativadas
        for (DonoEmpresa dono : donoEmpresaData.getDonosEmpresa()) {
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                if (!empresa.isAtivo()) {
                    ativarEmpresaComboBox.getItems().add(empresa.getNome());
                }
            }
        }
    }

    private void initializeTodasEmpresasComboBox() {
        todasEmpresasComboBox.getItems().clear();

        // obtem empresas
        for (DonoEmpresa dono : donoEmpresaData.getDonosEmpresa()) {
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                todasEmpresasComboBox.getItems().add(empresa.getNome());
            }
        }

        // regista a açãode clicar na combobx
        todasEmpresasComboBox.setOnAction(event -> {
            String empresaSelecionada = todasEmpresasComboBox.getValue();
            if (empresaSelecionada != null && !empresaSelecionada.isEmpty()) {
                calcularEExibirReceita(empresaSelecionada);
            }
        });
    }

    @FXML
    private void onCriarAdminButtonClick(ActionEvent event) {
        String username = novoAdminUsername.getText();
        String password = novoAdminPassword.getText();
        System.out.println(userData);
        userData.save();
        if (!username.isEmpty() && !password.isEmpty()) {
            User novoAdmin = new User(username, password, Role.ADMIN);
            userData.register(novoAdmin);
            userData.save();
            showAlert("Novo Admin Criado", "O novo administrador foi criado com sucesso.");
        } else {
            showAlert("Campos Vazios", "Por favor, preencha todos os campos para criar um novo administrador.");
        }
    }

    @FXML
    private void onDesativarEmpresaButtonClick(ActionEvent event) {
        String nomeEmpresa = empresaComboBox.getValue();
        System.out.println(nomeEmpresa);

        if (nomeEmpresa != null && !nomeEmpresa.isEmpty()) {
            boolean desativadaComSucesso = donoEmpresaData.desativarEmpresa(nomeEmpresa);

            if (desativadaComSucesso) {
                showAlert("Empresa Desativada", "A empresa foi desativada com sucesso.");
            } else {
                showAlert("Erro", "A empresa não foi encontrada ou já está desativada.");
            }

            initializeEmpresaComboBox();
            initializeAtivarEmpresaComboBox();
        } else {
            showAlert("Nenhuma Empresa Selecionada", "Por favor, selecione uma empresa para desativar.");
        }
        donoEmpresaData.save();
    }

    @FXML
    private void onAtivarEmpresaButtonClick(ActionEvent event) {
        String nomeEmpresa = ativarEmpresaComboBox.getValue();

        System.out.println(nomeEmpresa);

        if (nomeEmpresa != null && !nomeEmpresa.isEmpty()) {
            boolean ativadaComSucesso = donoEmpresaData.ativarEmpresa(nomeEmpresa);

            if (ativadaComSucesso) {
                showAlert("Empresa Ativada", "A empresa foi ativada com sucesso.");
            } else {
                showAlert("Erro", "A empresa não foi encontrada ou já está ativada.");
            }

            initializeEmpresaComboBox();
            initializeAtivarEmpresaComboBox();
        } else {
            showAlert("Nenhuma Empresa Selecionada", "Por favor, selecione uma empresa para ativar.");
        }
        donoEmpresaData.save();
    }

    private void calcularEExibirReceita(String nomeEmpresa) {
        float receitaTotal = 0.0f;

        for (DonoEmpresa dono : donoEmpresaData.getDonosEmpresa()) {
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                if (empresa.getNome().equals(nomeEmpresa)) {

                    receitaTotal = calcularReceitaEmpresa(empresa);
                    receitaLabel.setText("Receita da Empresa: " + receitaTotal + "€");

                    consultoriosVBox.getChildren().clear();

                    for (Consultorio consultorio : empresa.getConsultorios()) {
                        float receitaConsultorio = calcularReceitaConsultorio(consultorio);

                        Label labelConsultorio = new Label("Receita do Consultório de " + consultorio.getEspecialidade() + ": " + receitaConsultorio + "€");

                        consultoriosVBox.getChildren().add(labelConsultorio);
                    }
                }
            }
        }
    }

    private float calcularReceitaEmpresa(EmpresaCuidadosDentarios empresa) {
        float receitaTotal = 0.0f;
        for (Consultorio consultorio : empresa.getConsultorios()) {
            for (Consulta consulta : consultorio.getConsultas()) {
                if(consulta.isPago()){
                    receitaTotal += consulta.getPreco();
                }
            }
        }
        return receitaTotal;
    }

    private float calcularReceitaConsultorio(Consultorio consultorio) {
        float receitaConsultorio = 0.0f;
        for (Consulta consulta : consultorio.getConsultas()) {
            if(consulta.isPago()) {
                receitaConsultorio += consulta.getPreco();
            }
        }
        return receitaConsultorio;
    }

    private void calcularEExibirReceitaTotalTodasEmpresas() {
        float receitaTotalTodasEmpresas = calcularReceitaTotalTodasEmpresas();
        receitaTotalTodasEmpresasLabel.setText("Receita Total de Todas Empresas: " + receitaTotalTodasEmpresas + "€");

        float receitaDescontada = receitaTotalTodasEmpresas * 0.05f; // 5% de desconto
        receitaDescontadaLabel.setText("Receita da DentalCare: " + receitaDescontada + "€");
    }

    private float calcularReceitaTotalTodasEmpresas() {
        float receitaTotalTodasEmpresas = 0.0f;
        for (DonoEmpresa dono : donoEmpresaData.getDonosEmpresa()) {
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                receitaTotalTodasEmpresas += calcularReceitaEmpresa(empresa);
            }
        }
        return receitaTotalTodasEmpresas;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
