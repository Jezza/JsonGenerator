package me.jezza.common.core;

import me.jezza.common.core.config.ConfigHandler;

public class ShutdownThread extends Thread {

    public ShutdownThread() {
        super("Shutdown-Thread");
    }

    @Override
    public void run() {
        ConfigHandler.save();
    }

    public static void add() {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
    }

}
