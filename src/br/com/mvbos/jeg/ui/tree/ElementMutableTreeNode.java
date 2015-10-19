/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jeg.ui.tree;

import br.com.mvbos.jeg.element.ElementModel;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Marcus Becker
 */
public class ElementMutableTreeNode extends DefaultMutableTreeNode {

    public ElementMutableTreeNode(ElementModel userObject) {
        super(userObject);
    }

    @Override
    public String toString() {
        if (userObject == null) {
            return "";

        } else if (userObject instanceof ElementModel) {
            ElementModel el = (ElementModel) userObject;
            return el.getName();

        } else {
            return userObject.toString();
        }
    }

}
