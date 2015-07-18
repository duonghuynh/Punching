/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

/**
 *
 * @author admin
 */
public class Tool {

    private String face; // A or B or empty for cut tool
    private String name; // 1, 2, 3, Yc...
    private float ypos;
    private int value; // value to write to PLC

    public Tool() {

    }

    public Tool(String name, String face, float ypos) {
        this.name = name;
        this.face = face;
        this.ypos = ypos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getYpos() {
        return ypos;
    }

    public void setYpos(float ypos) {
        this.ypos = ypos;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
