package yummyloop.example.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import yummyloop.example.util.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigFile {
    private final Path path;
    private final List<Object> list = new ArrayList<>();
    private final List<Class<?>> classList = new ArrayList<>();
    private final Logger logger = new Logger(this.getClass().toString(), "OFF");

    public ConfigFile(String file){
        this.path = FabricLoader.getInstance().getConfigDirectory().toPath().resolve(file);
        this.path.toFile().getParentFile().mkdirs();
    }

    private boolean save(List<Object> list){
        try {
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            String formattedOutput = (this.toJson(list));
                   // .replaceAll("\"__Comment\": \"(.*)\"","/*$1*/");
            writer.write(formattedOutput);
            writer.close();
        } catch (IOException e) {
            logger.warn("Could not create file");
            return false;
        }
        return true;
    }

    public boolean save(){
        return this.save(this.list);
    }

    public void add(Object obj){
        this.list.add(obj);
        this.classList.add(obj.getClass());
    }

    public String toJson(){
        return this.toJson(this.list);
    }

    public String toJson(Object obj){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    private <T> T fromJson (String string, Class<T> c) throws JsonSyntaxException{
        Gson gson = new GsonBuilder().setLenient().create();
        return gson.fromJson(string, c);
        //return gson.fromJson(string.replaceAll("/\\*(.*)\\*/","\"__Comment\": \"$1\""), c);
    }

    private List fromJson(String json) throws JsonParseException {
        return this.fromJson(json, this.list.getClass() );
    }

    public boolean load(){
        String fileText = "";
        String line;
        List tempList;
        Object tempObj;
        // Try to read file
        try {
            // Open file in readmode
            RandomAccessFile file = new RandomAccessFile(this.path.toString(), "r");
            while ((line = file.readLine()) != null){
                fileText=fileText.concat(line);
            }
            file.close();
        } catch (IOException e) {
            logger.warn("Could not read file");
            return false;
        }

        // Try to parse file to json
        try {
            tempList= this.fromJson(fileText);
        } catch (JsonParseException e) {
            //System.out.println(fileText);
            logger.warn("Could not parse json");
            return false;
        }

        // If did not read anything (file is empty)
        if (tempList == null) return false;

        // Don't load if the lists have different sizes
        if (this.list.size() != tempList.size()) {
            logger.warn("Wrong json size");
            return false;
        }

        // Re-parse objects in the list to classes
        for (int i = 0; i < this.list.size(); i++) {
            tempObj = tempList.get(i);
            try {
                this.list.set(i, fromJson(toJson(tempObj), this.classList.get(i)));
            } catch (Exception e) {
                logger.warn("Wrong format " + i + " | " + this.classList.get(i));
                return false;
            }
        }
        return true;
    }

    public Object getObject (int index) throws IndexOutOfBoundsException{
        return this.list.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T get (int index) throws IndexOutOfBoundsException, ClassCastException{
        return (T) this.classList.get(index).cast(this.list.get(index));
    }

    public int size(){
        return this.list.size();
    }
}

// look at : //@SerializedName("w") replace var name with w
/* Example:
 *  Config cc = new Config("a/b/Hello.json");
        cc.add("Hello");
        cc.add(new String[]{"sa","sb","sc"});
        cc.add("End");
        if (!cc.load()) {cc.save();}
        System.out.println(cc.toJson());
        System.out.println( ((String[]) cc.get(1))[0] );
*/
