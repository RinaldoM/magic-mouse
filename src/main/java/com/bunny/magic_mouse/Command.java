package com.bunny.magic_mouse;

public class Command {
    private int x;
    private int y;
    private boolean click;
    private String description;

    public String getDescription() {
        return description;
    }

    public Command(boolean click) {
        this.click = click;
    }

    public Command(int x, int y, String description) {
        this.x = x;
        this.y = y;
        this.description = description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isClick() {
        return click;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setClick(boolean click) {
        this.click = click;
    }
}
