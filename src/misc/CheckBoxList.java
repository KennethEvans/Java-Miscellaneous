package misc;

/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * CheckBoxList.java
 * 
 * Created on October 5, 2006, 9:53 AM
 *
 * Modified June 29, 2014 for generic object warnings
 * 
 */

/**
 *
 * @author gtoffoli
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class CheckBoxList extends JList<Object>
{
    private static final long serialVersionUID = 1L;

    public CheckBoxList() {
        super();

        setModel(new DefaultListModel<Object>());
        setCellRenderer(new CheckboxCellRenderer());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());

                if(index != -1) {
                    Object obj = getModel().getElementAt(index);
                    if(obj instanceof JCheckBox) {
                        JCheckBox checkbox = (JCheckBox)obj;

                        checkbox.setSelected(!checkbox.isSelected());
                        repaint();
                    }
                }
            }
        }

        );
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public int[] getCheckedIdexes() {
        java.util.List<Integer> list = new java.util.ArrayList<Integer>();
        DefaultListModel<Object> dlm = (DefaultListModel<Object>)getModel();
        for(int i = 0; i < dlm.size(); ++i) {
            Object obj = getModel().getElementAt(i);
            if(obj instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox)obj;
                if(checkbox.isSelected()) {
                    list.add(Integer.valueOf(i));
                }
            }
        }

        int[] indexes = new int[list.size()];

        for(int i = 0; i < list.size(); ++i) {
            indexes[i] = list.get(i).intValue();
        }

        return indexes;
    }

    public java.util.List<JCheckBox> getCheckedItems() {
        java.util.List<JCheckBox> list = new java.util.ArrayList<JCheckBox>();
        DefaultListModel<Object> dlm = (DefaultListModel<Object>)getModel();
        for(int i = 0; i < dlm.size(); ++i) {
            Object obj = getModel().getElementAt(i);
            if(obj instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox)obj;
                if(checkbox.isSelected()) {
                    list.add(checkbox);
                }
            }
        }
        return list;
    }
}

