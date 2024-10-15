package tp1_javafx.tp1_prog2_v2.Classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Utilizador implements Serializable {

    public Cliente(String nome, String num_cc, String num_fiscal, String telemovel, String morada, String localidade) {
        super(nome, num_cc, num_fiscal, telemovel, morada, localidade);

    }


    @Override
    public String toString() {
        return "cliente{" +
                "nome=" + getNome() +
                '}';
    }


}
