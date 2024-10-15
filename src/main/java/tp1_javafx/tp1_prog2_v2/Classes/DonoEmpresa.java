package tp1_javafx.tp1_prog2_v2.Classes;

import tp1_javafx.tp1_prog2_v2.Files.DonoEmpresaData;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DonoEmpresa extends Utilizador implements Serializable {
    private List<EmpresaCuidadosDentarios> empresas;
    private boolean ativo = true;


    public DonoEmpresa(String nome, String num_cc, String num_fiscal, String telemovel, String morada, String localidade) {
        super(nome, num_cc, num_fiscal, telemovel, morada, localidade);
        this.empresas = new ArrayList<>();

    }

    public List<EmpresaCuidadosDentarios> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<EmpresaCuidadosDentarios> empresas) {
        this.empresas = empresas;
    }

    public void adicionarEmpresa(EmpresaCuidadosDentarios novaEmpresa) {
        empresas.add(novaEmpresa);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }



    public String obterNome() {
        return getNome();
    }

    public List<String> getNomesEmpresas() {
        return empresas.stream()
                .map(EmpresaCuidadosDentarios::getNome)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "DonoEmpresa{" +
                "nome='" + getNome() + '\'' +
                ", empresas=" + empresas +
                '}';
    }


    public Optional<EmpresaCuidadosDentarios> getEmpresaPeloNome(String nomeEmpresa) {
        // stream e filter pra encontrar a empresa
        return empresas.stream()
                .filter(empresa -> empresa.getNome().equals(nomeEmpresa))
                .findFirst();
    }

    public void atualizarEmpresa(EmpresaCuidadosDentarios empresaAtualizada, DonoEmpresaData empresasData) {
        // econtra empresas no donoempresa
        Optional<EmpresaCuidadosDentarios> empresaExistente = getEmpresaPeloNome(empresaAtualizada.getNome());

        empresaExistente.ifPresent(empresa -> {
            empresas.remove(empresa);

            empresa.setNome(empresaAtualizada.getNome());
            empresa.setMorada(empresaAtualizada.getMorada());
            empresa.setLocalidade(empresaAtualizada.getLocalidade());
            empresa.setNumeroTelefone(empresaAtualizada.getNumeroTelefone());

            // adiciona a lista
            empresas.add(empresaAtualizada);

        });
    }

    public boolean updateConsultorios(String nomeEmpresa, Set<Consultorio> consultorios) {
        Optional<EmpresaCuidadosDentarios> empresaExistente = getEmpresas().stream()
                .filter(e -> e.getNome().equals(nomeEmpresa))
                .findFirst();

        if (empresaExistente.isPresent()) {
            EmpresaCuidadosDentarios empresa = empresaExistente.get();

            empresa.setConsultorios(consultorios);


            return true;
        } else {
            return false;
        }
    }

    public boolean associarFuncionariosAoConsultorio(String nomeEmpresa, String nomeConsultorio, Funcionario funcionario) {
        Optional<EmpresaCuidadosDentarios> empresaExistente = empresas.stream()
                .filter(e -> e.getNome().equals(nomeEmpresa))
                .findFirst();

        if (empresaExistente.isPresent()) {
            Optional<Consultorio> consultorioExistente = empresaExistente.get().getConsultorios().stream()
                    .filter(c -> c.getNome().equals(nomeConsultorio))
                    .findFirst();

            consultorioExistente.ifPresent(consultorio -> {
                consultorio.associarFuncionario(funcionario);

            });

            return consultorioExistente.isPresent();
        }

        return false;
    }

    public boolean atualizarEstadoConsulta(Long id, String nomeEmpresa, String nomeConsultorio, EstadoConsulta novoEstado) {
        Optional<EmpresaCuidadosDentarios> empresaExistente = empresas.stream()
                .filter(e -> e.getNome().equals(nomeEmpresa))
                .findFirst();

        if (empresaExistente.isPresent()) {
            Optional<Consultorio> consultorioExistente = empresaExistente.get().getConsultorios().stream()
                    .filter(c -> c.getNome().equals(nomeConsultorio))
                    .findFirst();
            System.out.println("AQIO" + consultorioExistente);

            consultorioExistente.ifPresent(consultorio -> {
                // encontra a consulta pelo id
                Optional<Consulta> consultaExistente = consultorio.getConsultas().stream()
                        .filter(consulta ->
                                consulta.getId() == id)
                        .findFirst();

                consultaExistente.ifPresent(consulta -> {
                    // atualiza o estado da consulta
                    consulta.setEstado(novoEstado);
                });
                System.out.println(consultorioExistente);
                System.out.println(empresaExistente);
            });

            return consultorioExistente.isPresent();
        }

        return false;
    }
}
