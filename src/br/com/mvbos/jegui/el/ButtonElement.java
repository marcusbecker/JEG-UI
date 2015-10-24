/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.el;

import br.com.mvbos.jeg.element.ElementModel;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Marcus Becker
 */
public class ButtonElement extends ElementModel {

    private boolean focus;

    public ButtonElement(int width, int height, String name) {
        super(width, height, name);
    }

    @Override
    public void drawMe(Graphics2D g) {
        g.setColor(Color.YELLOW);

        if (focus) {
            g.fillOval(getPx(), getPy(), getWidth(), getWidth());
        } else {
            g.drawOval(getPx(), getPy(), getWidth(), getWidth());
        }

    }

    public void setFocus(boolean b) {
        this.focus = b;
    }

}
