package tp1_javafx.tp1_prog2_v2.Classes;

import java.io.Serializable;

public abstract class Utilizador implements Serializable {

    private String nome;
    private String num_cc;
    private String num_fiscal;
    private String telemovel;
    private String morada;
    private String localidade;

    public Utilizador(String nome, String num_cc, String num_fiscal, String telemovel, String morada, String localidade){
        this.nome = nome;
        this.num_cc = num_cc;
        this.num_fiscal = num_fiscal;
        this.telemovel = telemovel;
        this.morada = morada;
        this.localidade = localidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNum_cc() {
        return num_cc;
    }

    public void setNum_cc(String num_cc) {
        this.num_cc = num_cc;
    }

    public String getNum_fiscal() {
        return num_fiscal;
    }

    public void setNum_fiscal(String num_fiscal) {
        this.num_fiscal = num_fiscal;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
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
}
