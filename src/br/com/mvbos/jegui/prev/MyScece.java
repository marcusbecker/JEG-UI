/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.prev;

import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.element.SelectorElement;
import br.com.mvbos.jeg.engine.GraphicTool;
import br.com.mvbos.jeg.scene.IScene;
import br.com.mvbos.jeg.scene.ISelectorScene;
import br.com.mvbos.jeg.window.Camera;
import br.com.mvbos.jeg.window.IMemory;
import java.awt.Graphics2D;

/**
 *
 * @author Marcus Becker
 */
public class MyScece implements IScene, ISelectorScene {

    private static int ct;
    private IMemory[] memo;
    private SelectorElement selector;

    public MyScece() {
    }

    public MyScece(IMemory[] memo) {
        this.memo = memo;
    }

    @Override
    public void updateScene() {

        for (IMemory m : memo) {
            for (ct = 0; ct < m.getElementCount(); ct++) {
                ElementModel el = m.getByElement(ct);

                if (GraphicTool.g().bcollide(el, selector)) {
                    el.setVisible(false);
                } else {
                    //el.setVisible(true);
                }

                el.update();
            }
        }
        if (selector != null) {
            selector.setEnabled(false);
            selector = null;
        }
    }

    @Override
    public void drawScene(Graphics2D g2d) {
        for (IMemory m : memo) {
            for (ct = 0; ct < m.getElementCount(); ct++) {
                ElementModel el = m.getByElement(ct);
                //el.drawMe(g2d);
                Camera.c().draw(g2d, el);
            }
        }
    }

    @Override
    public void startSelection(SelectorElement selectorElement) {
        //this.selector = selectorElement;
    }

    @Override
    public void endSelection(SelectorElement selectorElement) {
        this.selector = selectorElement;
    }

    @Override
    public boolean startScene() {
        return true;
    }

    @Override
    public IMemory[] getElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setElements(IMemory[] memory) {
        this.memo = memory;
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeSceneEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void focusWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void lostFocusWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void closeWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resizeWindow(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
