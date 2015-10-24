/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.ui.table;

import br.com.mvbos.jeg.element.ElementModel;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Marcus Becker
 */
public class PropertyElementTable extends JTable {

    private Class editingClass;

    private final ImageButtonColumn imageBtnCol = new ImageButtonColumn();
    private final ColorButtonColumn colorBtnCol = new ColorButtonColumn(Color.WHITE);

    private ElementModel element;
    private final String[] columnNames = {"Type", "Value"};
    private final Object[][] data = {
        {"id", 0},
        {"name", ""},
        {"pX", 0.0f},
        {"pY", 0.0f},
        {"width", 10},
        {"height", 10},
        {"visible", Boolean.TRUE},
        {"enabled", Boolean.TRUE},
        {"image", new ImageIcon()},
        {"color", Color.GRAY}
    };

    public PropertyElementTable() {

        setModel(new AbstractTableModel() {
            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public int getRowCount() {
                return data.length;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public Object getValueAt(int row, int col) {
                return data[row][col];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {

                if (!data[row][col].equals(value)) {
                    data[row][col] = value;
                    dataToElement(element);

                    //System.out.println("old " + data[row][col] + " new " + value);
                }

                fireTableCellUpdated(row, col);
                //fireTableDataChanged();
            }
        });

        getTableHeader().setReorderingAllowed(false);

        setDefaultEditor(ImageIcon.class, imageBtnCol);
        setDefaultRenderer(ImageIcon.class, imageBtnCol);

        setDefaultEditor(Color.class, colorBtnCol);
        setDefaultRenderer(Color.class, colorBtnCol);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        editingClass = null;
        int modelColumn = convertColumnIndexToModel(column);
        if (modelColumn == 1) {
            //Class rowClass = getModel().getValueAt(row, modelColumn).getClass();
            Class rowClass = data[row][modelColumn].getClass();
            return getDefaultRenderer(rowClass);
        } else {
            return super.getCellRenderer(row, column);
        }
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        editingClass = null;
        int modelColumn = convertColumnIndexToModel(column);
        if (modelColumn == 1) {
            //editingClass = getModel().getValueAt(row, modelColumn).getClass();
            editingClass = data[row][modelColumn].getClass();
            return getDefaultEditor(editingClass);
        } else {
            return super.getCellEditor(row, column);
        }
    }

    //  This method is also invoked by the editor when the value in the editor
    //  component is saved in the TableModel. The class was saved when the
    //  editor was invoked so the proper class can be created.
    @Override
    public Class getColumnClass(int column) {
        return editingClass != null ? editingClass : super.getColumnClass(column);
    }

    public void printData() {
        for (Object[] o : data) {
            for (Object dt : o) {
                if (dt instanceof ImageIcon) {
                    ImageIcon i = (ImageIcon) dt;
                    System.out.print(" " + i.getIconWidth() + "x" + i.getIconHeight());
                } else {
                    System.out.print(" " + dt);
                }
            }

            System.out.println();
        }
    }

    private void elementToData(ElementModel el) {
        data[0][1] = el.getId();
        data[1][1] = el.getName();
        data[2][1] = el.getPx();
        data[3][1] = el.getPy();
        data[4][1] = el.getWidth();
        data[5][1] = el.getHeight();
        data[6][1] = el.isVisible();
        data[7][1] = el.isEnabled();
        data[8][1] = el.isValidImage() ? el.getImage() : new ImageIcon();
        data[9][1] = el.getColor();
    }

    private void dataToElement(ElementModel element) {
        if (element != null) {
            element.setId((int) data[0][1]);
            element.setName(data[1][1].toString());
            element.setPx(Float.valueOf(data[2][1].toString()));
            element.setPy(Float.valueOf(data[3][1].toString()));
            element.setWidth((int) data[4][1]);
            element.setHeight((int) data[5][1]);
            element.setVisible((boolean) data[6][1]);
            element.setEnabled((boolean) data[7][1]);
            element.setImage((ImageIcon) data[8][1]);
            element.setColor((Color) data[9][1]);
        }

    }

    public void setElement(ElementModel el) {

        this.element = el;
        elementToData(el);

        for (int i = 0; i < data.length; i++) {
            getModel().setValueAt(data[i][1], i, 1);
        }

        /*getModel().setValueAt(el.getId(), 0, 1);
         getModel().setValueAt(el.getPx(), 1, 1);
         getModel().setValueAt(el.getPy(), 2, 1);
         getModel().setValueAt(el.getWidth(), 3, 1);
         getModel().setValueAt(el.getHeight(), 4, 1);
         getModel().setValueAt(el.isVisible(), 5, 1);
         getModel().setValueAt(el.isEnabled(), 6, 1);
         getModel().setValueAt(el.getImage() == null ? new ImageIcon() : el.getImage(), 7, 1);
         getModel().setValueAt(el.getDefaultColor(), 8, 1);*/
    }
}
