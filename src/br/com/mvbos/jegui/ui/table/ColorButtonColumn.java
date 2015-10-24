/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author MarcusS
 */
public class ColorButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {

    private final JButton button;

    private final JDialog dialog;
    private final JColorChooser colorChooser;

    private Color colorSelected = Color.ORANGE;

    public ColorButtonColumn() {
        this(null);
    }

    public ColorButtonColumn(Color color) {
        this.colorSelected = color;

        button = new JButton();

        if (colorSelected != null) {
            button.setBackground(colorSelected);
        }

        button.setToolTipText("Change color");
        button.setBorderPainted(false);
        button.addActionListener(this);

        colorChooser = new JColorChooser();
        ActionListener ac = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorSelected = colorChooser.getColor();
            }
        };

        dialog = JColorChooser.createDialog(button, "Choose the color", true, colorChooser, ac, null);
    }

    @Override
    public Object getCellEditorValue() {
        return colorSelected;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //System.out.printf("JTable color , value %s, isSelected %s, hasFocus %s, row %s, column %s\n", value, isSelected, hasFocus, row, column);

        button.setBackground((Color) value);

        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        colorSelected = (Color) value;
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        button.setBackground(colorSelected);
        colorChooser.setColor(colorSelected);
        dialog.setVisible(true);

        fireEditingStopped();

    }

    public void setValue(Color color) {
        colorSelected = color;
    }

}
