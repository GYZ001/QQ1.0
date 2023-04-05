/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package component;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import bean.Qquser;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author jzyqd_
 */
public class ClinetImgCell extends JLabel implements ListCellRenderer {

    private ImageIcon img;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Qquser obj = (Qquser)value;
        this.img = new ImageIcon(obj.getPlace1());
        if (isSelected) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
        }
        this.setText(obj.getName());
        this.setIcon(this.img);
        this.setEnabled(list.isEnabled());
        this.setFont(list.getFont());
        this.setOpaque(true);
        return this;
    }
}