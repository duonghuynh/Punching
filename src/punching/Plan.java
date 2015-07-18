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
public class Plan {
    private List<ObjectV> objectV = new ArrayList<ObjectV>();

    public List<ObjectV> getObjectV() {
        return objectV;
    }

    public void setObjectV(List<ObjectV> objectV) {
        this.objectV = objectV;
    }

    
}
