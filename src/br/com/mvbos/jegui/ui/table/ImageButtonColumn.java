/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.ui.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MarcusS
 */
public class ImageButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {

    private final JButton button;
    private final JFileChooser fc;

    private ImageIcon icon = new ImageIcon();

    private static final String DEFAULT_LABEL = "...";

    public ImageButtonColumn() {
        button = new JButton(DEFAULT_LABEL);
        button.setToolTipText("Change image");
        button.setBorderPainted(false);
        button.addActionListener(this);

        fc = new JFileChooser("");
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isFile() && (f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg"));
            }

            @Override
            public String getDescription() {
                return "JPG and PNG Images";
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return icon;
    }

    //Rendering a table cell when it is not in focus
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        ImageIcon data = (ImageIcon) value;
        //System.out.printf("JTable image, value %s, isSelected %s, hasFocus %s, row %s, column %s\n", value, isSelected, hasFocus, row, column);

        if (data.getDescription() != null) {
            File temp = new File(data.getDescription());
            button.setText(temp.getName());
        } else {
            button.setText(DEFAULT_LABEL);
        }

        return button;
    }

    //Handling edits while the cell is in focus
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ImageIcon data = (ImageIcon) value;
        if (data != null) {
            icon = data;
        }

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (icon.getDescription() != null) {
            File lastDir = new File(icon.getDescription());
            fc.setCurrentDirectory(lastDir);
        }

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            icon = new ImageIcon(fc.getSelectedFile().getAbsolutePath());
        }

        //Make the renderer reappear.
        fireEditingStopped();
    }

    public void setValue(ImageIcon icon) {
        this.icon = icon;
    }

}
