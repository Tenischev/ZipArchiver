package ru.ifmo.ctddev.tenischev.java.archiver;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by kris13 on 28.10.15.
 */
public class MainGUI extends JFrame{
    private JButton addFile;
    private JButton saveAs;
    private JLabel destPath;
    private JSlider compress;
    private JList listFiles;
    private JPanel rootPanel;
    private JButton create;
    private JButton removeFile;
    private final JFileChooser fileChooserAdd = new JFileChooser();
    private final JFileChooser fileChooserSave = new JFileChooser();
    private final StringDefaultListModel listModel = new StringDefaultListModel();

    public MainGUI() {
        super("Tenischev Semen archiver");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        destPath.setText(Archive.DEFAULT_DEST);

        listFiles.setModel(listModel);

        fileChooserAdd.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooserSave.setCurrentDirectory((new File(Archive.DEFAULT_DEST)).getParentFile());

        addFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooserAdd.showOpenDialog(addFile);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooserAdd.getSelectedFile();
                    listModel.addElement(file.getAbsolutePath());
                    listFiles.ensureIndexIsVisible(listModel.size());
                }
            }
        });

        removeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] ind = listFiles.getSelectedIndices();
                Arrays.sort(ind);
                for (int i = ind.length - 1; i >= 0; i--)
                listModel.removeElementAt(ind[i]);
            }
        });

        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooserSave.showSaveDialog(saveAs);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooserSave.getSelectedFile();
                    try {
                        destPath.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        destPath.setText(file.getAbsolutePath());
                    }
                }
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                (new Thread(new Archive(listModel.toArray(), destPath.getText(), compress.getValue(), true), "ArchiveThread")).start();
            }
        });

        setVisible(true);
    }

    private class StringDefaultListModel extends DefaultListModel<String> {
        @Override
        public String[] toArray() {
            String[] rv = new String[this.size()];
            for (int i = 0; i < this.size(); i++) {
                rv[i] = this.getElementAt(i);
            }
            return rv;
        }
    }
}
