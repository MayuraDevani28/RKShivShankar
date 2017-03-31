package com.shivshankar.classes;

/**
 * Created by Mayura on 10/1/2016.
 */
public class CMS {
    String name, content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public CMS(String name, String content) {
        this.name = name;
        this.content = content;
    }


    public String getContent() {
        return content;
    }

}
