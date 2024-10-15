package tp1_javafx.tp1_prog2_v2.Files;


import tp1_javafx.tp1_prog2_v2.Models.User;
import tp1_javafx.tp1_prog2_v2.Utils.PasswordEncrypter;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserData extends DataFile  {

    public ArrayList<User> users;

    public UserData() {
        super("userData.bin");

        this.users = new ArrayList<User>();
    }

    /* Load user data from file */

    @Override
    public boolean load() {
        if(!this.exists())
            this.create();

        try {
            FileInputStream fis=new FileInputStream(this.name);
            ObjectInputStream ois=new ObjectInputStream(fis);

            this.users = (ArrayList<User>) ois.readObject();
            fis.close();

            return true;
        } catch (ClassNotFoundException | IOException e) {
            System.out.print(e.getMessage());
            return false;
        }
    }

    /* Save user data to file */
    @Override
    public boolean save() {
        if(!this.exists())
            this.create();

        try {
            FileOutputStream fileOut = new FileOutputStream(this.name);

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.users);
            out.close();
            fileOut.close();
            return true;
        }catch(IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /* check user data on file to login */

    public User login(String username, String password){
        for (User user : this.users) {
            if(Objects.equals(user.username, username) && Objects.equals(user.password, PasswordEncrypter.encrypt(password)))
                return user;
        }
        return null;
    }
    /* register/verify user data on file */

    public boolean register(User user) {
        user.password = PasswordEncrypter.encrypt(user.password);

        for(User u: this.users) {
            if(Objects.equals(u.username, user.username)) return false;
        }

        this.users.add(user);
        return this.save();

    }

    public boolean delete(User user) {

        for(int i = 0; i < this.users.size(); i++) {
            if(this.users.get(i).username.equals(user.username)) {
                this.users.remove(i);
                return this.save();
            }
        }

        return false;
    }

}

