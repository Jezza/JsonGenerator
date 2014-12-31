package me.jezza;

import me.jezza.common.core.interfaces.IJsonHandler;
import me.jezza.common.core.ShutdownThread;
import me.jezza.common.core.config.ConfigHandler;
import me.jezza.common.ui.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class App {

    public static final Collection<IJsonHandler> HANDLERS = new ArrayList<>();

    public static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static final String VERSION = "0.0.01";

    private static MainWindow mainWindow;

    public static void main(String[] args) throws Exception {
        ShutdownThread.add();

        setLookAndFeel();
        mainWindow = new MainWindow();
        HANDLERS.add(mainWindow);

        try {
            ConfigHandler.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        SwingUtilities.invokeLater(mainWindow);
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
