package ru.javalab.socketsapp.protocol;

public class Pagination {
    private int page;
    private String command;

    public Pagination(String command, int page){
        this.command = command;
        this.page = page;
    }

    public Pagination(){

    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getPage() {
        return page;
    }

    public String getCommand() {
        return command;
    }
}
