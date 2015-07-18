/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package punching;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author admin
 */
public class HoleTableModel extends AbstractTableModel {

    private List<Hole> data;

    private String face; // A or B

    private float xmax;

    public HoleTableModel() {
        data = new ArrayList<Hole>();
        face = "A";
    }

    public HoleTableModel(List<Hole> data, String face) {
        this.data = data;
        this.face = face;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public float getXmax() {
        return xmax;
    }

    public void setXmax(float xmax) {
        this.xmax = xmax;
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
                return h.getYr();
            case 2:
                return h.getY();
            case 3:
                return h.getX();
            case 4:
                if (h.getT() != null) {
                    return h.getT().getName();
                }
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "No.";
            case 1:
                return "Y-TĐ";
            case 2:
                return "Y";
            case 3:
                return "X";
            case 4:
                return "T";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return false;
        } else if (row == 0 && col == 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex >= data.size()) {
            return;
        }

        Hole h = data.get(rowIndex);
        switch (columnIndex) {
            case 1:
                h.setYr((Float) value);
                updateFixedY();
                break;
            case 2:
                h.setY((Float) value);
                updateYR();
                break;
            case 3:
                if((Float)value > xmax){
                    JOptionPane.showMessageDialog(null, "X không được lớn hơn " + xmax);
                }else{
                    h.setX((Float) value);
                }
                break;
            case 4:
                Tool t = Environment.getToolByName((String) value, face);
                if (t != null) {
                    h.setT(Environment.getToolByName((String) value));
                }
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addRow(boolean isFixedX, float step) {
        Tool t;
        if (face.equals("A")) {
            t = Environment.getInstance().getSetting().getFaceATools().get(0);
        } else {
            t = Environment.getInstance().getSetting().getFaceBTools().get(0);
        }

        Hole h = new Hole(0, 0, t);

        if (data.size() > 0) {
            Hole lastHole = data.get(data.size() - 1);
            h = lastHole.clone();
            if (isFixedX) {
                h.setY(h.getY() + step);
            } else {
                h.setX(h.getX() + step);
            }
        }

        data.add(h);
        fireTableDataChanged();
    }

    public void removeRow(int rowIndex) {
        if (data.size() > rowIndex) {
            data.remove(rowIndex);
            fireTableDataChanged();
        }
    }

    /**
     * Update Y relatively by calculate from fixed y
     */
    public void updateYR() {
        if (data.size() <= 1) {
            return;
        }
        for (int i = 0; i < data.size() - 1; i++) {
            Hole pre = data.get(i);
            Hole me = data.get(i + 1);
            me.setYr(me.getY() - pre.getY());
        }
        fireTableDataChanged();
    }

    /**
     * Update fixed Y by calculate from relatively y
     */
    public void updateFixedY() {
        if (data.size() <= 1) {
            return;
        }
        for (int i = 0; i < data.size() - 1; i++) {
            Hole pre = data.get(i);
            Hole me = data.get(i + 1);
            me.setY(pre.getY() + me.getYr());
        }
        fireTableDataChanged();
    }
}
