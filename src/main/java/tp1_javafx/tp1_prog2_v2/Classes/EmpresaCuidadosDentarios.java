package tp1_javafx.tp1_prog2_v2.Classes;

import java.io.Serializable;
import java.util.*;

public class EmpresaCuidadosDentarios implements Serializable {
    private String nome;
    private String morada;
    private String localidade;
    private String numeroTelefone;
    private Set<Consultorio> consultorios;
    private static Map<String, EmpresaCuidadosDentarios> mapaLocalidadeEmpresa;
    private static Map<EmpresaCuidadosDentarios, List<Consultorio>> mapaEmpresaPraConsultorios;
    private static Map<DonoEmpresa, List<EmpresaCuidadosDentarios>> mapaDonoEmpresa;
    private boolean ativo = true;

    public EmpresaCuidadosDentarios(String nome, String morada, String localidade, String numeroTelefone){
        this.nome = nome;
        this.morada = morada;
        this.localidade = localidade;
        this.numeroTelefone = numeroTelefone;
        this.consultorios = new HashSet<>();
        this.mapaLocalidadeEmpresa = new HashMap<>();
        this.mapaEmpresaPraConsultorios = new HashMap<>();
        this.mapaDonoEmpresa = new HashMap<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
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

    public Set<Consultorio> getConsultorios() {
        return consultorios;
    }

    public void setConsultorios(Set<Consultorio> consultorios) {
        this.consultorios = consultorios;
    }

    public static Map<String, EmpresaCuidadosDentarios> getMapaLocalidadeEmpresa() {
        return mapaLocalidadeEmpresa;
    }

    public static void setMapaLocalidadeEmpresa(Map<String, EmpresaCuidadosDentarios> mapaLocalidadeEmpresa) {
        EmpresaCuidadosDentarios.mapaLocalidadeEmpresa = mapaLocalidadeEmpresa;
    }

    public static Map<EmpresaCuidadosDentarios, List<Consultorio>> getMapaEmpresaPraConsultorios() {
        return mapaEmpresaPraConsultorios;
    }

    public static void setMapaEmpresaPraConsultorios(Map<EmpresaCuidadosDentarios, List<Consultorio>> mapaEmpresaPraConsultorios) {
        EmpresaCuidadosDentarios.mapaEmpresaPraConsultorios = mapaEmpresaPraConsultorios;
    }

    public Map<DonoEmpresa, List<EmpresaCuidadosDentarios>> getMapaDonoEmpresa() {
        return mapaDonoEmpresa;
    }

    public void setMapaDonoEmpresa(Map<DonoEmpresa, List<EmpresaCuidadosDentarios>> mapaDonoEmpresa) {
        this.mapaDonoEmpresa = mapaDonoEmpresa;
    }

    public void adicionarAoMapaDonoEmpresa(DonoEmpresa donoEmpresa) {
        mapaDonoEmpresa.computeIfAbsent(donoEmpresa, k -> new ArrayList<>()).add(this);
        System.out.println("Empresa " + this.nome + " adicionada ao mapa para o DonoEmpresa " + donoEmpresa.getNome());

    }

    public void removerDoMapaDonoEmpresa(DonoEmpresa donoEmpresa) {
        mapaDonoEmpresa.computeIfPresent(donoEmpresa, (k, v) -> {
            v.remove(this);
            return v;
        });
    }


    public boolean isAtivo(){
        return ativo;
    }

    public void setAtivo(boolean ativo){
        this.ativo = ativo;
    }



    @Override
    public String toString() {
        return "EmpresaCuidadosDentarios{" +
                "nome='" + nome + '\'' +
                ", localidade='" + localidade + '\'' +
                ", ativo=" + ativo +
                '}';
    }





    public Consultorio getConsultorioPeloNome(String nomeConsultorio) {
        return consultorios.stream()
                .filter(consultorio -> consultorio.getNome().equals(nomeConsultorio))
                .findFirst()
                .orElse(null);
    }

    public Consultorio getConsultorioPelaEspecialidade(String especialidadeConsultorio) {
        return consultorios.stream()
                .filter(consultorio -> consultorio.getEspecialidade().equals(especialidadeConsultorio))
                .findFirst()
                .orElse(null);
    }

    public String getNomeEmpresaAssociada(Consultorio consultorio) {
        return mapaLocalidadeEmpresa.entrySet().stream()
                .filter(entry -> entry.getValue().equals(consultorio))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
