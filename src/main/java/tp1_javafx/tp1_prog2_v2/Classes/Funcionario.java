package tp1_javafx.tp1_prog2_v2.Classes;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Funcionario extends Utilizador implements Serializable {
    private TipoFuncionario tipo;
    private Consultorio consultorio;

    public Funcionario(String nome, String num_cc, String num_fiscal, String telemovel, String morada, String localidade){
        super(nome, num_cc, num_fiscal, telemovel, morada, localidade);
        this.tipo = tipo;
        this.consultorio = null;
    }

    public TipoFuncionario getTipo() {
        return tipo;
    }

    public void setTipo(TipoFuncionario tipo) {
        this.tipo = tipo;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void associarConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public void desassociarConsultorio(Consultorio consultorio) {
        this.consultorio = null;
    }

    public List<Consulta> getConsultasAssociadas() {
        return consultorio.getConsultas().stream()
                .filter(consulta -> consulta.getFuncionario() == this)
                .collect(Collectors.toList());
    }

    public List<Consulta> getConsultasDoConsultorio() {
        return consultorio.getConsultas();
    }

    @Override
    public String toString() {
        return
                "{nome='" + getNome() + '\'' +
                ", localidade='" + getLocalidade() + '\'' +
                ", tipo=" + tipo +

                '}';
    }

}
