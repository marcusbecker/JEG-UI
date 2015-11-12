/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.external;

import br.com.mvbos.jegui.App;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 *
 * @author Marcus Becker
 */
public class MyObjectInputStream extends ObjectInputStream {

    protected MyObjectInputStream() throws IOException, SecurityException {
        super();
    }

    public MyObjectInputStream(FileInputStream in) throws IOException, SecurityException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

        for (Class c : App.jarUtil.getElements()) {
            if (c.getName().equals(desc.getName())) {
                return c;
            }
        }

        return super.resolveClass(desc);
    }

}
