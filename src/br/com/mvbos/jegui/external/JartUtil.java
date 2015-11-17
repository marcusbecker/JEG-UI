/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.external;

import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.scene.IScene;
import br.com.mvbos.jegui.prev.DefaultScene;
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

    private final List<Class<? extends IScene>> scenes;
    private final List<Class<? extends ElementModel>> elements;

    public JartUtil() {
        scenes = new ArrayList<>(10);
        elements = new ArrayList<>(10);

        //elements.add((ElementModel) createInstance(ElementModel.class));
        //scenes.add((IScene) createInstance(MyScece.class));
        scenes.add(DefaultScene.class);
        elements.add(ElementModel.class);
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

    public void populeClassNames(String path, List<String> classNames) {

        try {
            File myJar = new File(path);
            URL[] urls = new URL[1];
            urls[0] = myJar.toURI().toURL();

            URLClassLoader child = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

            for (String cName : classNames) {
                Class classToLoad = Class.forName(cName, true, child);
                //System.out.println("classToLoad " + classToLoad.getName());

                boolean isElement = false;

                try {
                    classToLoad.asSubclass(ElementModel.class);
                    elements.add(classToLoad);
                    isElement = true;
                } catch (Exception e) {

                }

                if (!isElement) {
                    try {
                        classToLoad.asSubclass(IScene.class);
                        scenes.add(classToLoad);
                    } catch (Exception e) {
                    }
                }
            }

        } catch (MalformedURLException | ClassNotFoundException e) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<Class> getClassNames(String path, List<String> classNames) {

        List<Class> lstClasses = new ArrayList<>(classNames.size());

        try {
            File myJar = new File(path);
            URL[] urls = new URL[1];
            urls[0] = myJar.toURI().toURL();

            URLClassLoader child = new URLClassLoader(urls, FileUtil.class.getClassLoader());

            for (String cName : classNames) {
                Class classToLoad = Class.forName(cName, true, child);

                try {
                    classToLoad.asSubclass(ElementModel.class);
                    lstClasses.add(classToLoad);

                    continue;
                } catch (Exception e) {
                }

                try {
                    classToLoad.asSubclass(IScene.class);
                    lstClasses.add(classToLoad);
                } catch (Exception e) {
                }

            }

        } catch (MalformedURLException | ClassNotFoundException e) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, e);
        }

        return lstClasses;
    }

    public void instanceClassNames(String path, List<String> classNames) {

        try {
            File myJar = new File(path);
            URL[] urls = new URL[1];
            urls[0] = myJar.toURI().toURL();

            URLClassLoader child = new URLClassLoader(urls, FileUtil.class.getClassLoader());

            for (String cName : classNames) {
                Class classToLoad = Class.forName(cName, true, child);

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
                    //Prevent class loader error.
                    Object instance = classToLoad.newInstance();
                    System.out.println(instance);

                    //setMethods(classToLoad, instance);
                    /*if (instance instanceof ElementModel) {
                     _elements.add((ElementModel) instance);

                     } else if (instance instanceof IScene) {
                     IScene sc = (IScene) instance;
                     _scenes.add(sc);
                     }*/
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

    private boolean isSub(Class classToLoad, Class aClass) {

        try {
            classToLoad.asSubclass(aClass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public IScene getScene(int selectedIndex) {
        try {
            return scenes.get(selectedIndex).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public ElementModel getElement(int selectedIndex) {
        try {
            return elements.get(selectedIndex).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JartUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
