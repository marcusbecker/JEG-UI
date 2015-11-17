/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.external;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Marcus Becker
 */
public class MyObjectInputStream extends ObjectInputStream {

    private final Collection<Class> classes;

    protected MyObjectInputStream() throws IOException, SecurityException {
        super();
        classes = Collections.EMPTY_SET;
    }

    public MyObjectInputStream(FileInputStream in) throws IOException, SecurityException {
        super(in);
        classes = Collections.EMPTY_SET;
    }

    public MyObjectInputStream(Collection classes, FileInputStream in) throws IOException, SecurityException {
        super(in);
        this.classes = classes;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

        for (Class c : classes) {
            if (c.getName().equals(desc.getName())) {
                return c;
            }
        }

        return super.resolveClass(desc);
    }

}
