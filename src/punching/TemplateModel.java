/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author admin
 */
public class TemplateModel extends DefaultComboBoxModel<Template> {
    public TemplateModel(Template[] items) {
        super(items);
    }
 
    @Override
    public Template getSelectedItem() {
        Template selectedTemplate = (Template) super.getSelectedItem();
        return selectedTemplate;
    }
}
