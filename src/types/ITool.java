/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author dpf
 */
public interface ITool {
    
    public static int CLICK_SELECTION_SIZE = 20;
    
    public ImageIcon getIcon();
    
    public MouseAdapter getMouseAdapter();
    
    public List<JPanel> getUtilityPanel();
    
    public void setCanvas(ICanvas canvas);
    
    public String getToolName();
    
    public Cursor getCursor();
    
    public void onClose();
    
    public void reOpen(Object... args);
    
}
