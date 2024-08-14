package org;


import org.core.Core;
import java.util.logging.Logger;


public class CoDraw {
    static int ROWS = 50;
    static int COLUMNS = 50;
    static int BLOCK_SIZE = 16;
    static int WIDTH = 1330;
    static int HEIGHT = 900;

    public static Core core;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("global");
        logger.info("coDraw v-1.1.2");
        core = new Core(ROWS, COLUMNS, BLOCK_SIZE, WIDTH, HEIGHT);
        core.start();

    }




}
