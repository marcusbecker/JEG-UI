/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui;

import br.com.mvbos.jeg.element.ElementModel;
import br.com.mvbos.jeg.scene.IScene;
import static br.com.mvbos.jegui.Constants.BACKGROUND;
import static br.com.mvbos.jegui.Constants.FOREGROUND;
import static br.com.mvbos.jegui.Constants.STAGE;
import br.com.mvbos.jegui.prev.DefaultScene;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Becker
 */
public class Project implements Serializable {

    public static final List<Class<? extends IScene>> scenes;
    public static final List<Class<? extends ElementModel>> elements;

    static {
        scenes = new ArrayList<>(5);
        elements = new ArrayList<>(5);

        scenes.add(DefaultScene.class);
        elements.add(ElementModel.class);
    }

    private String title;

    private Set<String> importFiles = new LinkedHashSet<>();
    private List<ElementModel> libElements = new ArrayList<>(100);
    private Map<String, List<ElementModel>> sceneElements;

    public Project() {
        this("");
    }

    public Project(String title) {
        this.title = title;

        sceneElements = new LinkedHashMap<>(3);
        sceneElements.put(BACKGROUND, new ArrayList<ElementModel>(5));
        sceneElements.put(STAGE, new ArrayList<ElementModel>(40));
        sceneElements.put(FOREGROUND, new ArrayList<ElementModel>(20));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ElementModel> getLibElements() {
        return libElements;
    }

    public void setLibElements(List<ElementModel> libElements) {
        this.libElements = libElements;
    }

    public Map<String, List<ElementModel>> getSceneElements() {
        return sceneElements;
    }

    public void setSceneElements(Map<String, List<ElementModel>> sceneElements) {
        this.sceneElements = sceneElements;
    }

    public Set<String> getImportFiles() {
        return importFiles;
    }

    public void setImportFiles(Set<String> importFiles) {
        this.importFiles = importFiles;
    }

    public static IScene getScene(int selectedIndex) {
        try {
            return scenes.get(selectedIndex).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static ElementModel getElement(int selectedIndex) {
        try {
            return elements.get(selectedIndex).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Project{" + "title=" + title + ", libElements=" + libElements + ", sceneElements=" + sceneElements + ", importFiles=" + importFiles + '}';
    }

}
