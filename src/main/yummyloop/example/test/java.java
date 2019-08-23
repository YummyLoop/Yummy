package yummyloop.example.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class java {
    public static String comm = "Hello world!";

    Comment a = new Comment("Hello");
    private int aa=2;
    int bb= 3;
    @SerializedName("__Comment")
    String comment = "Hello world!";

    public java(){
        int a = 1;
    }

    class Comment {
        @SerializedName("__Comment")
        String c;
        Comment(String message){
            this.c = message;
        }
    }
}
