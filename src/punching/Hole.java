/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
public class Hole {

    private float x;
    private float y;
    private float machineY; // Machine Y
    private Tool t;
    private String name;
    private float yr; // relative y

    public Hole() {
        this.name = "";
    }

    public Hole(float y, float x, Tool t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }

    @XmlAttribute
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    @XmlAttribute
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @XmlTransient
    public Tool getT() {
        return t;
    }

    public void setT(Tool t) {
        this.t = t;
    }

    @XmlAttribute
    public String getTool() {
        if (this.t != null) {
            return t.getName();
        } else {
            return "";
        }
    }

    public void setTool(String tool) {
        this.t = Environment.getToolByName(tool);
    }

    @XmlTransient
    public float getMachineY() {
        return machineY;
    }

    public void setMachineY(float machineY) {
        this.machineY = machineY;
    }

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Hole clone() {
        return new Hole(this.y, this.x, this.t);
    }

    @XmlTransient
    public float getYr() {
        return yr;
    }

    public void setYr(float yr) {
        this.yr = yr;
    }
    
    
}
