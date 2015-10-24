/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jeg.ui;

import javax.swing.JTextField;

/**
 *
 * @author Marcus Becker
 */
class Util {

    static int getInt(JTextField tf) {
        try {
            return Integer.parseInt(tf.getText());
        } catch (NumberFormatException e) {

        }

        return -1;
    }

}
