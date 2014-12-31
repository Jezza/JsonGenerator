package me.jezza.common.ui;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public abstract class HoverFrame extends JFrame {

    private JFrame parent;
    private int xOffset, yOffset;
    private boolean edgedX, edgedY;

    protected GridBagConstraints constraints;

    public HoverFrame(JFrame parent, int xOffset, int yOffset) {
        this.parent = parent;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        setUndecorated(true);
        constraints = new GridBagConstraints();
        getContentPane().setLayout(new GridBagLayout());
        setType(Type.UTILITY);
        setLocationRelativeTo(parent);

        parent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updateHover(false);
            }
        });
        initComponents();
    }

    public HoverFrame(JFrame parent, AbstractButton button, int xOffset, int yOffset) {
        this(parent, xOffset, yOffset);
        setToggleComponent(button);
    }

    public void setToggleComponent(AbstractButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHover(true);
            }
        });
    }

    public void setEdgedX() {
        edgedX = !edgedX;
    }

    public void setEdgedY() {
        edgedY = !edgedY;
    }

    public abstract void initComponents();

    public void updateHover(boolean toggle) {
        int edgeX = edgedX ? parent.getWidth() : 0;
        int edgeY = edgedY ? parent.getHeight() : 0;

        setLocation(parent.getX() + edgeX + xOffset + 5, parent.getY() + edgeY + yOffset + 5);
        if (toggle)
            setVisible(!isVisible());
    }
}