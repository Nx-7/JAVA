package tp1_javafx.tp1_prog2_v2.Files;

import tp1_javafx.tp1_prog2_v2.Classes.Cliente;
import tp1_javafx.tp1_prog2_v2.Classes.Consulta;
import tp1_javafx.tp1_prog2_v2.Classes.DonoEmpresa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ClienteData extends DataFile {
    public ArrayList<Cliente> clientes;

    public ClienteData() {
        super("clienteData.bin");
        this.clientes = new ArrayList<Cliente>();
    }

    /* Load client data from file */
    @Override
    public boolean load() {
        if(!this.exists())
            this.create();

        try (FileInputStream fis = new FileInputStream(this.name);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            this.clientes = (ArrayList<Cliente>) ois.readObject();
            return true;

        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /* Save client data to file */
    @Override
    public boolean save() {
        if(!this.exists())
            this.create();
        try (FileOutputStream fileOut = new FileOutputStream(this.name);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(this.clientes);
            return true;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /* Insert client data on file */
    public boolean insert(Cliente cliente) {
        this.clientes.add(cliente);
        return this.save();
    }

    public Cliente getClienteByUserName(String username) {
        for (Cliente cliente : clientes) {
            if (cliente.getNome().equals(username)) {
                return cliente;
            }
        }
        return null;
    }








}

