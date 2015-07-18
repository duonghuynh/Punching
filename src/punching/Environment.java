/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author admin
 */
public final class Environment {

    public static String TEMPLATE_FOLDER = System.getProperty("user.dir") + File.separator + "templates" + File.separator;
    private static final String SETTING_FILE = "settings.xml";

    private Setting setting;

    private String currentFile;

    private List<ObjectV> listObject = new ArrayList<ObjectV>();

    private static List<Template> listTemplate = new ArrayList<Template>();

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    private Environment() {
        loadSetting();
    }

    public static Environment getInstance() {
        return EnvironmentHolder.INSTANCE;
    }

    private static class EnvironmentHolder {

        private static final Environment INSTANCE = new Environment();
    }

    public List<ObjectV> getListObject() {
        return listObject;
    }

    public void setListObject(List<ObjectV> listObject) {
        this.listObject = listObject;
    }

    public static List<Template> getListTemplate() {
        return listTemplate;
    }

    public static void setListTemplate(List<Template> listTemplate) {
        Environment.listTemplate = listTemplate;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    public void loadSetting() {
        String settingFile = System.getProperty("user.dir") + File.separator + SETTING_FILE;
        if (new File(settingFile).exists()) {
            try {
                JAXBContext jc = JAXBContext.newInstance(Setting.class);
                Unmarshaller u = jc.createUnmarshaller();
                FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + File.separator + SETTING_FILE);
                setting = (Setting) u.unmarshal(inputStream);
            } catch (JAXBException ex) {
                Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            loadDefaultSetting();
        }

    }

    public void loadDefaultSetting() {
        setting = new Setting();
        setting.setCutTool(new Tool("Yc", "", 3500));
        setting.getFaceATools().add(new Tool("1", "A", 3000));
        setting.getFaceATools().add(new Tool("2", "A", 2500));
        setting.getFaceATools().add(new Tool("3", "A", 2000));

        setting.getFaceBTools().add(new Tool("1", "B", 1500));
        setting.getFaceBTools().add(new Tool("2", "B", 1000));
        setting.getFaceBTools().add(new Tool("3", "B", 500));

        setting.setComSpeed("9600");
        setting.setComPort("COM3");

        saveSetting();
    }

    public void saveSetting() {
        try {
            JAXBContext context = JAXBContext.newInstance(Setting.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try {
                FileOutputStream fo = new FileOutputStream(System.getProperty("user.dir") + File.separator + SETTING_FILE);
                m.marshal(setting, fo);
                try {
                    fo.close();
                } catch (IOException ex) {
                    Logger.getLogger(TemplateFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TemplateFrame.class.getName()).log(Level.SEVERE, null, ex);

            }
        } catch (JAXBException ex) {
            Logger.getLogger(TemplateFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reloadTemplate() {
        listTemplate.clear();
        String templateFolder = getInstance().getSetting().getTemplateFolder();
        if (templateFolder == null || templateFolder.length() == 0) {
            templateFolder = TEMPLATE_FOLDER;
            getInstance().getSetting().setTemplateFolder(templateFolder);
            getInstance().saveSetting();
        }
        File folder = new File(templateFolder);
        if (folder.exists() && folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                Template template = loadTemplate(file.getPath());
                if (template != null) {
                    listTemplate.add(template);
                }
            }
        }
    }

    /**
     * Sample data
     *
     * @param name
     * @param face
     * @return
     */
    public static Tool getToolByName(String name, String face) {
        // Find in A list
        if (face.equals("A")) {
            for (Tool tool : getInstance().getSetting().getFaceATools()) {
                if (tool.getName().equals(name)) {
                    return tool;
                }
            }
        }

        // Find in B list
        if (face.equals("B")) {
            for (Tool tool : getInstance().getSetting().getFaceBTools()) {
                if (tool.getName().equals(name)) {
                    return tool;
                }
            }
        }
        return null;
    }

    public static Tool getToolByName(String name) {
        Tool tool = getToolByName(name, "A");
        if (tool != null) {
            return tool;
        } else {
            return getToolByName(name, "B");
        }
    }

    public static Template loadTemplate(String path) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Template.class);
            Unmarshaller u = jc.createUnmarshaller();
            FileInputStream inputStream = new FileInputStream(path);
            Template doc = (Template) u.unmarshal(inputStream);
            doc.setFileName(path);

            // correct tool by face A and B
            for (Hole h : doc.getFaceAHoles()) {
                h.setT(getToolByName(h.getT().getName(), "A"));
            }
            for (Hole h : doc.getFaceBHoles()) {
                if (h.getT() != null) {
                    h.setT(getToolByName(h.getT().getName(), "B"));
                }
            }

            return doc;
        } catch (JAXBException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveTemplate(String path, Template template) {
        try {
            JAXBContext context = JAXBContext.newInstance(Template.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            FileOutputStream fo = new FileOutputStream(path);
            m.marshal(template, fo);

        } catch (JAXBException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Hole createMarker(float y) {
        Hole marker = new Hole(y, 0, getInstance().setting.getTextTool());
        marker.setName("Marker");
        return marker;
    }

}
