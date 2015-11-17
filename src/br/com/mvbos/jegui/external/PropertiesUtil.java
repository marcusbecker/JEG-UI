/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.jegui.external;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Becker
 */
public class PropertiesUtil {

    public static Properties prop = new Properties();

    private static final File dir = new File("config");

    static {
        load();
    }

    public static void save() {

        try {

            if (!dir.exists()) {
                dir.mkdir();
            }

            prop.store(new FileOutputStream(new File(dir, "config.properties")), "jeg-ui");
        } catch (IOException ex) {
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void load() {
        try {

            if (!dir.exists()) {
                dir.mkdir();
                return;
            }

            prop.load(new FileInputStream(new File(dir, "config.properties")));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
