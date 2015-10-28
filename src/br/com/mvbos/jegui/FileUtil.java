/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui;

import br.com.mvbos.jeg.element.ElementModel;
import static br.com.mvbos.jegui.Constants.BACKGROUND;
import static br.com.mvbos.jegui.Constants.FOREGROUND;
import static br.com.mvbos.jegui.Constants.STAGE;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Becker
 */
class FileUtil {

    private static final String LIBRARY_ELEMENTS = "library.temp";
    private static final String SCENE_ELEMENTS = "list.temp";

    static void saveList(Map<String, List<ElementModel>> sceneElements, List<ElementModel> libElements) {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SCENE_ELEMENTS))) {
            out.writeObject(sceneElements);
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(LIBRARY_ELEMENTS))) {
            out.writeObject(libElements);
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static Map<String, List<ElementModel>> loadList() {
        File f = new File(SCENE_ELEMENTS);

        if (f.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
                return (Map<String, List<ElementModel>>) in.readObject();
            } catch (Exception ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Map<String, List<ElementModel>> treeMap = new LinkedHashMap<>(3);
        treeMap.put(BACKGROUND, new ArrayList<ElementModel>(5));
        treeMap.put(STAGE, new ArrayList<ElementModel>(40));
        treeMap.put(FOREGROUND, new ArrayList<ElementModel>(20));

        return treeMap;
    }

    static List<ElementModel> loadLib() {
        File f = new File(LIBRARY_ELEMENTS);

        if (f.exists()) {

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
                return (List<ElementModel>) in.readObject();
            } catch (Exception ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return new ArrayList<>(100);
    }

}
