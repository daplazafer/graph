/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import javax.swing.JPanel;

/**
 *
 * @author dpf
 */
public interface IGraph {
    
    public JPanel getToolsPanel();
    
    public JPanel getUtiliyPanel();
    
    public JPanel getCanvasPanel();
    
    public ICanvas getCanvas();
   
    public void addToToolbar(ITool tool);
    
}
