package tp1_javafx.tp1_prog2_v2.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Consultorio implements Serializable {
    private String nome;
    private String endereco;
    private String localidade;
    private String numeroTelefone;
    private String especialidade;
    private List<Funcionario> funcionarios;
    private Map<String, Float> precosConsulta;
    private List<String> produtosServicosAssociados;
    private List<Consulta> consultas;
    private static Map<EmpresaCuidadosDentarios, Consultorio> mapaEmpresaPraConsultorio;

    public Consultorio(String nome, String endereco, String localidade, String numeroTelefone, String especialidade){
        this.nome = nome;
        this.endereco = endereco;
        this.localidade = localidade;
        this.numeroTelefone = numeroTelefone;
        this.especialidade = especialidade;
        this.funcionarios = new ArrayList<>();
        this.precosConsulta = new HashMap<>();
        this.produtosServicosAssociados = new ArrayList<>();
        this.consultas = new ArrayList<>();
        this.mapaEmpresaPraConsultorio = new HashMap<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Map<String, Float> getPrecosConsulta() {
        return precosConsulta;
    }

    public void setPrecosConsulta(Map<String, Float> precosConsulta) {
        this.precosConsulta = precosConsulta;
    }

    public List<String> getProdutosServicosAssociados() {
        return produtosServicosAssociados;
    }

    public void setProdutosServicosAssociados(List<String> produtosServicosAssociados) {
        this.produtosServicosAssociados = produtosServicosAssociados;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setMarcacoesConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public static Map<EmpresaCuidadosDentarios, Consultorio> getMapaEmpresaPraConsultorio() {
        return mapaEmpresaPraConsultorio;
    }

    public static void setMapaEmpresaPraConsultorio(Map<EmpresaCuidadosDentarios, Consultorio> mapaEmpresaPraConsultorio) {
        Consultorio.mapaEmpresaPraConsultorio = mapaEmpresaPraConsultorio;
    }

    public void setPrecoConsulta(String tipoConsulta, float preco) {
        precosConsulta.put(tipoConsulta, preco);
    }

    public float getPrecoConsulta(String tipoConsulta) {
        return precosConsulta.getOrDefault(tipoConsulta, 0.0f);
    }



    @Override
    public String toString() {
        return "Consultorios{" +
                "nome='" + nome + '\'' +
                ", localidade='" + localidade + '\'' +
                ", funcionario=" + funcionarios +
                ", consultas=" + consultas +
                ", preco=" + precosConsulta +

                '}';
    }

    public void associarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
        System.out.println(funcionarios);
        funcionario.associarConsultorio(this);
    }

    public void desassociarFuncionario(Funcionario funcionario) {
        funcionarios.remove(funcionario);
        funcionario.desassociarConsultorio(this);
    }

    public void agendarConsulta(Consulta consulta) {
        consultas.add(consulta);

    }

    public Funcionario getFuncionarioPeloNome(String nomeFuncionario) {
        return funcionarios.stream()
                .filter(funcionario -> funcionario.getNome().equals(nomeFuncionario))
                .findFirst()
                .orElse(null);
    }

    public EmpresaCuidadosDentarios getEmpresaAssociada() {
        return mapaEmpresaPraConsultorio.entrySet().stream()
                .filter(entry -> entry.getValue().equals(this))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


}
