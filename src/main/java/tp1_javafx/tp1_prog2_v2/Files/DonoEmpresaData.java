package tp1_javafx.tp1_prog2_v2.Files;

import tp1_javafx.tp1_prog2_v2.Classes.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class DonoEmpresaData extends DataFile {
    public ArrayList<DonoEmpresa> donosEmpresa;
    public ArrayList<EmpresaCuidadosDentarios> empresas;

    public DonoEmpresaData() {
        super("donoEmpresaData.bin");
        this.donosEmpresa = new ArrayList<DonoEmpresa>();
        this.empresas = new ArrayList<>();
    }

    public List<DonoEmpresa> getDonosEmpresa() {
        return new ArrayList<>(donosEmpresa);
    }

    public List<EmpresaCuidadosDentarios> getEmpresas() {
        return new ArrayList<>(empresas);
    }


    /* Load client data from file */
    @Override
    public boolean load() {
        if(!this.exists())
            this.create();

        try {
            FileInputStream fis=new FileInputStream(this.name);
            ObjectInputStream ois=new ObjectInputStream(fis);

            this.donosEmpresa = (ArrayList<DonoEmpresa>) ois.readObject();
            System.out.println("tamanho array Donos - " + donosEmpresa.size());
            fis.close();

            return true;
        } catch (ClassNotFoundException | IOException e) {
            System.out.print(e.getMessage());
            return false;
        }
    }

    /* Save client data to file */
    @Override
    public boolean save() {
        if(!this.exists())
            this.create();
        try {
            FileOutputStream fileOut = new FileOutputStream(this.name);

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.donosEmpresa);
            out.close();
            fileOut.close();
            System.out.println("GUARDOU");
            return true;
        }catch(IOException e) {
            System.out.println("NAO GUARDOU");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /* Insert client data on file */
    public boolean insert(DonoEmpresa donoEmpresa) {
        this.donosEmpresa.add(donoEmpresa);
        return this.save();
    }

    public DonoEmpresa getDonoEmpresaByUsername(String username) {
        for (DonoEmpresa dono : donosEmpresa) {
            if (dono.getNome().equals(username)) {
                return dono;
            }
        }
        return null;
    }





    /* Update empresa data on file */
    public boolean update(EmpresaCuidadosDentarios empresaAtualizada) {
        Optional<EmpresaCuidadosDentarios> empresaExistente = empresas.stream()
                .filter(e -> e.getNome().equals(empresaAtualizada.getNome()))
                .findFirst();

        if (empresaExistente.isPresent()) {
            EmpresaCuidadosDentarios empresa = empresaExistente.get();
            empresa.setNome(empresaAtualizada.getNome());
            empresa.setMorada(empresaAtualizada.getMorada());
            empresa.setLocalidade(empresaAtualizada.getLocalidade());
            empresa.setNumeroTelefone(empresaAtualizada.getNumeroTelefone());

            save();
            return true;
        } else {
            return false;
        }
    }


    public void printDonosEmpresas() {
        for (DonoEmpresa dono : donosEmpresa) {
            System.out.println("Dono: " + dono.getNome());
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                System.out.println("  Empresa: " + empresa.getNome());
                for (Consultorio consultorio : empresa.getConsultorios()) {
                    System.out.println("    Consultório: " + consultorio.getNome());
                    for (Funcionario funcionario : consultorio.getFuncionarios()) {
                        System.out.println("      Funcionário: " + funcionario.getNome());
                    }
                    for (Consulta consulta : consultorio.getConsultas()) {
                        System.out.println("      Consulta: " + consulta + (consulta.getCliente() != null ? ", Cliente: " + consulta.getCliente().getNome() : ""));
                    }
                }
            }
        }
    }

    public void visualizarConsultasCliente(String nomeCliente) {
        for (DonoEmpresa dono : donosEmpresa) {
            for (EmpresaCuidadosDentarios empresa : dono.getEmpresas()) {
                for (Consultorio consultorio : empresa.getConsultorios()) {
                    for (Consulta consulta : consultorio.getConsultas()) {
                        // Verificar se o cliente está associado à consulta
                        if (consulta.getCliente() != null && consulta.getCliente().getNome().equals(nomeCliente)) {
                            System.out.println("Consulta para o cliente " + nomeCliente + ": " + consulta);
                        }
                    }
                }
            }
        }
    }


    public Funcionario getFuncionarioByUsername(String username) {
        for (DonoEmpresa donoEmpresa : donosEmpresa) {
            for (EmpresaCuidadosDentarios empresa : donoEmpresa.getEmpresas()) {
                for (Consultorio consultorio : empresa.getConsultorios()) {
                    for (Funcionario funcionario : consultorio.getFuncionarios()) {
                        if (funcionario.getNome().equals(username)) {
                            return funcionario;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean desativarEmpresa(String nomeEmpresa) {
        for (DonoEmpresa donoEmpresa : donosEmpresa) {
            for (EmpresaCuidadosDentarios empresa : donoEmpresa.getEmpresas()) {
                if (empresa.getNome().equals(nomeEmpresa)) {
                    empresa.setAtivo(false);
                }
                return true;
            }
            save();
        } return false;

    }

    public boolean ativarEmpresa(String nomeEmpresa) {
        for (DonoEmpresa donoEmpresa : donosEmpresa) {
            for (EmpresaCuidadosDentarios empresa : donoEmpresa.getEmpresas()) {
                if (empresa.getNome().equals(nomeEmpresa)) {
                    empresa.setAtivo(true);
                }
                return true;
            }
            save();
        } return false;

    }



}

