/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author admin
 */
@XmlType(propOrder = {"name", "length", "faceAHoles", "faceBHoles", "hasmark", "marker"})
public class ObjectV {

    private String name;
    private float length;
    private float machineY;
    private List<Hole> faceAHoles = new ArrayList<Hole>();
    private List<Hole> faceBHoles = new ArrayList<Hole>();
    private boolean hasmark;
    private Hole marker;

    public ObjectV() {
    }

    public ObjectV(String name, float length) {
        this.name = name;
        this.length = length;
        marker = Environment.createMarker(length / 2);
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    @XmlTransient
    public float getMachineY() {
        return machineY;
    }

    public void setMachineY(float machineY) {
        this.machineY = machineY;
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

    public Hole getMarker() {
        return marker;
    }

    public void setMarker(Hole marker) {
        this.marker = marker;
    }

    public boolean isHasmark() {
        return hasmark;
    }

    public void setHasmark(boolean hasmark) {
        this.hasmark = hasmark;
    }
    
    public static ObjectV generate(Template template) {
        ObjectV obj = new ObjectV(template.getName(), template.getLength());
        for (Hole hole : template.getFaceAHoles()) {
            obj.getFaceAHoles().add(hole.clone());
        }
        for (Hole hole : template.getFaceBHoles()) {
            obj.getFaceBHoles().add(hole.clone());
        }
        return obj;
    }
}
