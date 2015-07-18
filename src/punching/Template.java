/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@XmlRootElement
public class Template {

    private String fileName;
    private String name;
    private float length;
    private List<Hole> faceAHoles = new ArrayList<Hole>();
    private List<Hole> faceBHoles = new ArrayList<Hole>();
    private float xmax;

    public Template() {
    }

    public Template(String name, float length, float xmax) {
        this.name = name;
        this.length = length;
        this.xmax = xmax;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    @XmlElement(name = "hole")
    @XmlElementWrapper(name = "FaceA")
    public List<Hole> getFaceAHoles() {
        return faceAHoles;
    }

    public void setFaceAHoles(List<Hole> flatAHoles) {
        this.faceAHoles = flatAHoles;
    }

    @XmlElement(name = "hole")
    @XmlElementWrapper(name = "FaceB")
    public List<Hole> getFaceBHoles() {
        return faceBHoles;
    }

    public void setFaceBHoles(List<Hole> faceBHoles) {
        this.faceBHoles = faceBHoles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getXmax() {
        return xmax;
    }

    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    @XmlTransient
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Template clone() {
        Template t = new Template();
        t.fileName = this.fileName;
        t.length = this.length;
        t.xmax = this.xmax;
        t.name = this.name;
        for (Hole h : faceAHoles) {
            t.faceAHoles.add(h.clone());
        }
        for (Hole h : faceBHoles) {
            t.faceBHoles.add(h.clone());
        }
        return t;
    }
}
