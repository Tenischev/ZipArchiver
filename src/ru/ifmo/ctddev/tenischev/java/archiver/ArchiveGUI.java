package ru.ifmo.ctddev.tenischev.java.archiver;

import javax.swing.*;
import java.awt.event.*;

public class ArchiveGUI extends JDialog {
    private final Thread archive;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList statusList;
    private JLabel mainStatus;

    private final DefaultListModel listModel = new DefaultListModel();

    public ArchiveGUI(Thread archive) {
        this.archive = archive;
        setContentPane(contentPane);
        pack();
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        buttonOK.setEnabled(false);

        statusList.setModel(listModel);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        archive.interrupt();
        dispose();
    }

    public static ArchiveGUI create(Thread archive) {
        RunGUI runGUI = new RunGUI(archive);
        ArchiveGUI archiveGUI = runGUI.archiveGUI;
        new Thread(runGUI, "ArchiveGuiThread").start();
        return archiveGUI;
    }

    public void addMessage(String str) {
        listModel.addElement(str);
        statusList.ensureIndexIsVisible(listModel.size());
    }

    public void verifyStatus(String str) {
        if ("Archive complete!".equals(str)) {
            buttonOK.setEnabled(true);
            mainStatus.setText(str);
        }
    }

    private static class RunGUI implements Runnable {
        private final ArchiveGUI archiveGUI;

        public RunGUI(Thread thread){
            archiveGUI = new ArchiveGUI(thread);
        }

        @Override
        public void run() {
            archiveGUI.setVisible(true);
        }
    }
}
