/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

/**
 *
 * @author admin
 */
public class MyTable extends JTable {

    public void changeSelection(
            int row, int column, boolean toggle, boolean extend) {
        super.changeSelection(row, column, toggle, extend);

        if (editCellAt(row, column)) {
            Component editor = getEditorComponent();
            editor.requestFocusInWindow();
            ((JTextComponent) editor).selectAll();
        }
    }
}
