/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.ui.tree;

import br.com.mvbos.jeg.element.ElementModel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Marcus Becker
 */
public class TreeElement extends JTree {

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        Object obj = null;

        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode def = (DefaultMutableTreeNode) value;
            obj = def.getUserObject();
        }

        if (obj != null && obj instanceof ElementModel) {
            ElementModel el = (ElementModel) obj;
            return el.getName();
        }

        return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
    }

}
