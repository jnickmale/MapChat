package edu.temple.mapchat;

/**
 * Created by nmale_000 on 4/6/2018.
 */

public class MyMessage {
    private String message;
    private String from;

    public MyMessage(String f, String mes){
        from = f;
        message = mes;
    }

    @Override
    public String toString(){
        return from + ": " + message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage(){
        return  message;
    }

}
