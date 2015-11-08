/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.external;

import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.scene.IScene;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Marcus Becker
 */
public class JartUtil {

    private final List<IScene> scenes;
    private final List<ElementModel> elements;

    public JartUtil() {
        scenes = new ArrayList<>(10);
        elements = new ArrayList<>(10);

        elements.add((ElementModel) createInstance(ElementModel.class));
    }

    private static Object createInstance(Class c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<String> listClassNames(String path) {

        List<String> classNames = new ArrayList<>(10);

        try {

            ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
            ZipEntry entry = zip.getNextEntry();

            while (entry != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.'); // including ".class"
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }

                entry = zip.getNextEntry();
            }

        } catch (Exception e) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, e);
        }

        return classNames;
    }

    public void instanceClassNames(String path, List<String> classNames) {

        try {
            File myJar = new File(path);
            URL[] urls = new URL[1];
            urls[0] = myJar.toURI().toURL();

            URLClassLoader child = new URLClassLoader(urls, this.getClass().getClassLoader());

            for (String cName : classNames) {
                Class classToLoad = Class.forName(cName, true, child);

                /*if (!isSub(classToLoad, ElementModel.class) || !isSub(classToLoad, IScene.class)) {
                 continue;
                 }*/
                boolean fail = false;

                try {
                    classToLoad.asSubclass(ElementModel.class);
                } catch (Exception e) {
                    fail = true;
                    //e.printStackTrace();
                }

                if (fail) {
                    try {
                        classToLoad.asSubclass(IScene.class);
                        fail = false;
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }

                if (fail) {
                    continue;
                }

                /* for(Class c : classToLoad.getClasses())
                 System.out.println("classToLoad " + (c));*/
                try {
                    Object instance = classToLoad.newInstance();

                    //setMethods(classToLoad, instance);
                    if (instance instanceof ElementModel) {
                        elements.add((ElementModel) instance);

                    } else if (instance instanceof IScene) {
                        IScene sc = (IScene) instance;
                        sc.startScene();
                        scenes.add(sc);
                    }

                } catch (InstantiationException | UnsupportedOperationException e) {

                }

                //Object result = method.invoke(instance);
            }

        } catch (MalformedURLException | ClassNotFoundException | IllegalAccessException e) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public ElementModel copy(ElementModel org) {

        ElementModel copy = null;

        try {
            final Class<? extends ElementModel> cpClass = org.getClass();
            copy = cpClass.newInstance();

            if (cpClass.getSuperclass() == ElementModel.class) {
                copy(cpClass, copy, org, cpClass.getSuperclass().getDeclaredFields());
            }

            copy(cpClass, copy, org, cpClass.getDeclaredFields());

            //copy.setName("Novo elemento");
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException e) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, e);
        }

        return copy;
    }

    public void copy(final Class<? extends ElementModel> cpClass, Object copy, ElementModel org, Field[] fields) throws IllegalAccessException, IllegalArgumentException, SecurityException {
        for (Field f : fields) {

            if (Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            f.setAccessible(true);
            f.set(copy, f.get(org));
        }
    }

    public List<IScene> getScenes() {
        return scenes;
    }

    public List<ElementModel> getElements() {
        return elements;
    }

    private boolean isSub(Class classToLoad, Class aClass) {

        try {
            classToLoad.asSubclass(aClass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
