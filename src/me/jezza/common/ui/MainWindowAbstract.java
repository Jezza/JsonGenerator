package me.jezza.common.ui;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import static java.awt.GridBagConstraints.*;

public abstract class MainWindowAbstract implements Runnable {

    protected JFrame mainFrame;
    protected GridBagConstraints constraints;

    public MainWindowAbstract() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        constraints = new GridBagConstraints();
        mainFrame.getContentPane().setLayout(new GridBagLayout());
        createUI();
    }

    protected void setGridDimensions(int width, int height) {
        constraints.gridwidth = width;
        constraints.gridheight = height;
    }

    protected void add(JComponent component) {
        add(component, true);
    }

    protected void add(JComponent component, boolean reset) {
        mainFrame.getContentPane().add(component, constraints);
        if (reset)
            resetConstraints();
    }

    protected void resetConstraints() {
        constraints.gridx = RELATIVE;
        constraints.gridy = RELATIVE;

        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.anchor = CENTER;
        constraints.fill = NONE;

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;
    }

    @Override
    public void run() {
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    protected abstract void createUI();

}
