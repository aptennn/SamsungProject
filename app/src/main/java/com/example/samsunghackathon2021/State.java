package com.example.samsunghackathon2021;

public class State {
    private String name;
    private String text;
    private String mode;
    private int ID;

    public State(int ID, String name, String text, String mode){
        this.ID=ID;
        this.name=name;
        this.text=text;
        this.mode=mode;

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }



}
