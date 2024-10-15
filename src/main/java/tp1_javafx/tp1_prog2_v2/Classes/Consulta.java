package tp1_javafx.tp1_prog2_v2.Classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Consulta implements Serializable {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private long id;
    private Funcionario funcionario;
    private LocalDate data;
    private LocalTime hora;
    private Cliente cliente;
    private EstadoConsulta estado;
    private float preco;
    private boolean pago;
    private String descricao;
    private Map<String, Float> produtosServicosComplementares;

    public Consulta(Funcionario funcionario, LocalDate data, LocalTime hora, Cliente cliente, float preco) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.funcionario = funcionario;
        this.data = data;
        this.hora = hora;
        this.cliente = cliente;
        this.estado = EstadoConsulta.PENDENTE;
        this.preco = preco;
        this.pago = false;
        this.produtosServicosComplementares = new HashMap<>();
    }

    public long getId(){
        return id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoConsulta getEstado() {
        return estado;
    }

    public void marcarComoConfirmada() {
        this.estado = EstadoConsulta.CONFIRMADA;
    }

    public void marcarComoAnulada() {
        this.estado = EstadoConsulta.ANULADA;
    }

    public void marcarComoEfetuada(){
        this.estado = EstadoConsulta.EFETUADA;
    }

    public void setEstado(EstadoConsulta estado) {
        this.estado = estado;
    }

    public boolean isPago() {
        return pago;
    }

    public void marcarComoPago() {
        this.pago = true;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao != null ? descricao : "Nenhuma descricao registado.";
    }

    public Map<String, Float> getProdutosServicosComplementares() {
        return produtosServicosComplementares;
    }

    public void adicionarProdutoServicoComplementar(String nome, Float preco) {
        produtosServicosComplementares.put(nome, preco);
    }



    @Override
    public String toString() {
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String estadoConsulta = "";

        switch (estado) {
            case PENDENTE:
                estadoConsulta = "Pendente";
                break;
            case CONFIRMADA:
                estadoConsulta = "Confirmada";
                break;
            case ANULADA:
                estadoConsulta = "Anulada";
                break;
            case EFETUADA:
                estadoConsulta = "Efetuada";
                break;
        }

        String pagamentoStatus = pago ? "Sim" : "Não";

        return String.format("Consulta agendada para %s às %s\n" +
                        "Funcionário: %s\n" +
                        "Cliente: %s\n" +
                        "Estado: %s\n" +
                        "Preço: %.2f\n" +
                        "Pago: %s",
                data.format(dataFormatter), hora.format(horaFormatter),
                funcionario.getNome(), cliente.getNome(), estadoConsulta, preco, pagamentoStatus);
    }





    public String exibirDetalhes() {
        return "Consulta agendada para " + data + " às " + hora + " com " + funcionario.getNome();
    }

    public String getNomeConsultorio() {
        if (funcionario != null && funcionario.getConsultorio() != null) {
            return funcionario.getConsultorio().getNome();
        } else {
            return null; // Ou uma string indicando que o consultório não está disponível
        }
    }


}
