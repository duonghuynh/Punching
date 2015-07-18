/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author admin
 */
public class ResultTableModel extends AbstractTableModel {

    private List<Hole> data;

    public ResultTableModel() {
        data = new ArrayList<Hole>();
    }

    public ResultTableModel(List<Hole> data) {
        this.data = data;
    }

    public List<Hole> getData() {
        return data;
    }

    public void setData(List<Hole> data) {
        this.data = data;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= data.size()) {
            return null;
        }
        Hole h = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return h.getName();
            case 2:
                return h.getMachineY();
            case 3:
                return h.getX();
            case 4:
                return h.getT().getValue();
        }
        return "";
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "No.";
            case 1:
                return "Name";
            case 2:
                return "Machine Y";
            case 3:
                return "Machine X";
            case 4:
                return "T value";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int c) {
        Object obj = getValueAt(0, c);
        if (obj != null) {
            return getValueAt(0, c).getClass();
        } else {
            return null;
        }
    }
}
