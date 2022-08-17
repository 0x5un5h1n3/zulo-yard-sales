package com.ox5un5h1n3.zulo.utils;

public class Global {

    private String title = "Not Set";
    private String content = "Not Set";

    private static Global instance;

    public static Global getInstance() {
        if (instance == null)
            instance = new Global();
        return instance;
    }

    private Global() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}