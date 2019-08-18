package yummyloop.example.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.common.reflect.TypeToken;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    private final Path path;
    private List<Object> list = new ArrayList<Object>();
    private List<Class<? extends Object>> classList = new ArrayList<Class<? extends Object>>();
    
    public Config(String file){
        this.path = FabricLoader.getInstance().getConfigDirectory().toPath().resolve(file);
        this.path.toFile().getParentFile().mkdirs();
    }

    private boolean save(List<Object> list){
        try {
            BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
            writer.write(this.toJson(list));
            writer.close();
        } catch (IOException e) {
            //System.out.println("Could not create file");
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

    private String toJson(List<Object> list){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(list);
    }

    public String toJson(Object obj){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    private <T> T fromJson (String string, Class<T> c) throws JsonSyntaxException{
        Gson gson = new Gson();
        return gson.fromJson(string, c);
    }

    private List<Object> fromJson(String json) throws JsonParseException, JsonSyntaxException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Object>>(){
            private static final long serialVersionUID = 1L;
        }.getType();
        return gson.fromJson(json, type);
    }

    public boolean load(){
        final boolean debug = false;
        String fileText = "";
        String line;
        List<Object> tempList = new ArrayList<Object>();
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
            if (debug) System.out.println("Could not read file");
            return false;
        }

        // Try to parse file to json
        try {
            tempList=this.fromJson(fileText);
        } catch (JsonParseException e) {
            System.out.println(fileText);
            if (debug) System.out.println("Could not parse json");
            return false;
        }

        // Don't load if the lists have different sizes
        if (this.list.size() != tempList.size()) {
            if (debug) System.out.println("Wrong json size");
            return false;
        }

        // Re-parse objects in the list to classes
        for (int i = 0; i < this.list.size(); i++) {
            tempObj = tempList.get(i);
            try {
                this.list.set(i, fromJson(toJson(tempObj), this.classList.get(i)));
            } catch (Exception e) {
                if (debug) System.out.println("Wrong format " + i + " | " + this.classList.get(i));
                return false;
            }
        }
        return true;
    }

    public Object getObject (int index) throws IndexOutOfBoundsException{
        return this.list.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T get (int index) throws IndexOutOfBoundsException{
        return (T) this.classList.get(index).cast(this.list.get(index));
    }

    public int size(){
        return this.list.size();
    }
}

// look at : //@SerializedName("w") replace var name with w
/** Example:
 *  Config cc = new Config("a/b/Hello.json");
        cc.add("Hello");
        cc.add(new String[]{"sa","sb","sc"});
        cc.add("End");
        if (!cc.load()) {cc.save();}
        System.out.println(cc.toJson());
        System.out.println( ((String[]) cc.get(1))[0] );
*/
 