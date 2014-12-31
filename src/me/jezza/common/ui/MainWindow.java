package me.jezza.common.ui;

import com.google.gson.JsonObject;
import me.jezza.App;
import me.jezza.common.core.JsonGenerator;
import me.jezza.common.core.config.ConfigHandler;
import me.jezza.common.core.interfaces.IJsonData;
import me.jezza.common.core.interfaces.IJsonHandlerWindow;
import me.jezza.common.core.json.BlockJson;
import me.jezza.common.core.json.ItemJson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.HORIZONTAL;

public class MainWindow extends MainWindowAbstract implements IJsonHandlerWindow {

    private JLabel labelJsonLabel;
    private JButton buttonJsonGen;
    private JTextField fieldJsonName;

    private JTextField fieldModID;

    private DisplayState displayState = DisplayState.NONE;

    public MainWindow() {
        mainFrame.setResizable(false);
        mainFrame.setTitle("Json Generator - v" + App.VERSION);
        setGridDimensions(5, 5);
    }

    @Override
    protected void createUI() {
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets.set(3, 14, 3, 14);
        add(new JLabel("Mod ID:"));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets.set(3, 6, 3, 6);
        labelJsonLabel = new JLabel();
        add(labelJsonLabel);
        labelJsonLabel.setVisible(false);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.ipadx = 20;
        constraints.gridwidth = 2;
        constraints.fill = HORIZONTAL;
        fieldModID = new JTextField("Enter your mod ID");
        fieldModID.getDocument().addDocumentListener(new TextFieldListener());
        fieldModID.setHorizontalAlignment(JTextField.CENTER);
        add(fieldModID);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.ipadx = 20;
        constraints.gridwidth = 2;
        constraints.fill = HORIZONTAL;
        fieldJsonName = new JTextField();
        fieldJsonName.getDocument().addDocumentListener(new TextFieldListener());
        fieldJsonName.setVisible(false);
        fieldJsonName.setHorizontalAlignment(JTextField.CENTER);
        add(fieldJsonName);

        constraints.fill = HORIZONTAL;
        constraints.gridx = 4;
        constraints.gridy = 0;
        JButton jsonItem = new JButton("Generate Item Json");
        jsonItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showItemJsonGenerator();
            }
        });
        add(jsonItem, false);

        constraints.fill = HORIZONTAL;
        constraints.gridx = 4;
        constraints.gridy = 1;
        JButton jsonBlock = new JButton("Generate Block Json");
        jsonBlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBlockJsonGenerator();
            }
        });
        add(jsonBlock, false);

        constraints.gridx = 4;
        constraints.gridy = 2;
        JButton save = new JButton("Save Config");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModID();
            }
        });
        add(save);

        constraints.fill = HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        buttonJsonGen = new JButton("Generate!");
        buttonJsonGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateJsonFile();
            }
        });
        buttonJsonGen.setVisible(false);
        add(buttonJsonGen);
    }

    public void generateJsonFile() {
        App.LOG.info("Generating JSON file.");
        App.LOG.info("Type: " + displayState);

        String name = fieldJsonName.getText();

        IJsonData jsonData = null;
        if (displayState.isItemGenerator())
            jsonData = new ItemJson(getModID(), name);
        else if (displayState.isBlockGenerator())
            jsonData = new BlockJson(getModID(), name);

        if (jsonData == null)
            return;

        JsonGenerator.generateJson(getModID(), jsonData);
    }

    public void pack() {
        mainFrame.pack();
    }

    private String getModID() {
        return fieldModID.getText();
    }

    private void showItemJsonGenerator() {
        if (displayState == DisplayState.ITEM_GENERATOR) {
            hideJsonGenerator();
            return;
        }
        App.LOG.info("Setting to ITEM_GENERATOR");
        displayState = DisplayState.ITEM_GENERATOR;

        labelJsonLabel.setText("Item name");
        labelJsonLabel.setVisible(true);
        buttonJsonGen.setVisible(true);
        fieldJsonName.setVisible(true);
        pack();
    }

    private void showBlockJsonGenerator() {
        if (displayState == DisplayState.BLOCK_GENERATOR) {
            hideJsonGenerator();
            return;
        }
        App.LOG.info("Setting to BLOCK_GENERATOR");
        displayState = DisplayState.BLOCK_GENERATOR;

        labelJsonLabel.setText("Block name");
        labelJsonLabel.setVisible(true);
        buttonJsonGen.setVisible(true);
        fieldJsonName.setVisible(true);

        pack();
    }

    private void hideJsonGenerator() {
        if (displayState == DisplayState.NONE)
            return;
        App.LOG.info("Setting to NONE");
        displayState = DisplayState.NONE;

        labelJsonLabel.setVisible(false);
        fieldJsonName.setVisible(false);
        buttonJsonGen.setVisible(false);
        pack();
    }

    private void setModID() {
        if (!getModID().equals("Enter your mod ID")) {
            ConfigHandler.save();
            pack();
        }
    }

    @Override
    public void toJsonObject(JsonObject jsonObject) {
        String modID = fieldModID.getText();
        if (!modID.isEmpty() && !modID.equals("Enter your mod ID"))
            jsonObject.addProperty("modID", modID);
        jsonObject.addProperty("displayState", displayState.name());
    }

    @Override
    public void fromJsonObject(JsonObject jsonObject) {
        if (jsonObject.has("modID")) {
            String modID = jsonObject.get("modID").getAsString();
            fieldModID.setText(modID);
            fieldModID.setCaretPosition(modID.length());
        }
        if (jsonObject.has("displayState")) {
            String displayString = jsonObject.get("displayState").getAsString();
            switch (DisplayState.valueOf(displayString)) {
                case NONE:
                    hideJsonGenerator();
                    break;
                case ITEM_GENERATOR:
                    showItemJsonGenerator();
                    break;
                case BLOCK_GENERATOR:
                    showBlockJsonGenerator();
                    break;
            }
        }
    }

    @Override
    public IJsonHandlerWindow getMainInstance() {
        return App.getMainWindow();
    }

    class TextFieldListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            pack();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            pack();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            pack();
        }
    }
}

enum DisplayState {
    NONE, ITEM_GENERATOR, BLOCK_GENERATOR;

    public boolean isNone() {
        return this == NONE;
    }

    public boolean isItemGenerator() {
        return this == ITEM_GENERATOR;
    }

    public boolean isBlockGenerator() {
        return this == BLOCK_GENERATOR;
    }
}
