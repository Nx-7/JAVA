package tp1_javafx.tp1_prog2_v2.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class DataFile {
    protected File file;
    protected String name;
    protected FileWriter fileWriter;
    /* File Construtor */
    public DataFile(String name){
        this.file = new File(name);
        this.name = name;
    }

    public abstract boolean load();
    public abstract boolean save();

    /* Generic method boolean to see if file exists */

    public boolean exists(){
        return this.file.exists();
    }

    /* Generic method boolean to create file */

    public boolean create(){
        try{
            if(file.exists()){
                return this.file.exists();
            } else {
                return this.file.createNewFile();
            }
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    /* Generic method boolean to write text on file */
    public boolean println(String text){
        if(!this.exists()) this.create();
        try{
            this.fileWriter = new FileWriter(file);
            this.fileWriter.write(text + "\n");
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
                return true;
            } catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }
    }

}
