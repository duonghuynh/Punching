/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@XmlRootElement
public class Setting {

    private Tool cutTool; // dao cat
    private Tool textTool; // dau
    private List<Tool> faceATools = new ArrayList<Tool>();
    private List<Tool> faceBTools = new ArrayList<Tool>();
    private float thickness;

    // For PLC
    private String comSpeed;
    private String comPort;
    private String parity;
    private String dataLength;
    private String stopBit;

    // for application setting
    private String templateFolder;
    private String planFolder;

    public Tool getCutTool() {
        return cutTool;
    }

    public void setCutTool(Tool cutTool) {
        this.cutTool = cutTool;
    }

    public List<Tool> getFaceATools() {
        return faceATools;
    }

    public void setFaceATools(List<Tool> faceATools) {
        this.faceATools = faceATools;
    }

    public List<Tool> getFaceBTools() {
        return faceBTools;
    }

    public void setFaceBTools(List<Tool> faceBTools) {
        this.faceBTools = faceBTools;
    }

    public String getComSpeed() {
        return comSpeed;
    }

    public void setComSpeed(String comSpeed) {
        this.comSpeed = comSpeed;
    }

    public String getComPort() {
        return comPort;
    }

    public void setComPort(String comPort) {
        this.comPort = comPort;
    }

    public Tool getTextTool() {
        return textTool;
    }

    public void setTextTool(Tool textTool) {
        this.textTool = textTool;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getStopBit() {
        return stopBit;
    }

    public void setStopBit(String stopBit) {
        this.stopBit = stopBit;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public String getTemplateFolder() {
        return templateFolder;
    }

    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
    }

    public String getPlanFolder() {
        return planFolder;
    }

    public void setPlanFolder(String planFolder) {
        this.planFolder = planFolder;
    }

}
