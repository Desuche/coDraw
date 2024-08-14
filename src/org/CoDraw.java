package org;


import org.core.Core;

public class CoDraw {
    static int ROWS = 50;
    static int COLUMNS = 50;
    static int BLOCK_SIZE = 16;
    static int WIDTH = 1330;
    static int HEIGHT = 900;

    public static Core core;

    public static void main(String[] args) {
        core = new Core(ROWS, COLUMNS, BLOCK_SIZE, WIDTH, HEIGHT);
        core.start();

    }




}
