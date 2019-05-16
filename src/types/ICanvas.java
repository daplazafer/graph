/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.util.List;
/**
 *
 * @author dpf
 */
public interface ICanvas {
    
    public static final Color DEFAULT_DRAW_COLOR = Color.BLACK;
    public static final Color DEFAULT_BG_COLOR = Color.WHITE;
    
    public void setMouseAdapter(MouseAdapter mouseAdapter);
    
    public void reDraw();
    
    public void addShape(CanvasShape shape);
    
    public void removeShape(CanvasShape shape) ;
    
    public void unDo();
    
    public void reDo();
    
    public Color getBackgroundColor();
    
    public void openTool(ITool tool, CanvasShape targetShape);
    
    public void addTool(ITool tool);
    
    public List<CanvasShape> getShapes();
    
    public void setGraph(IGraph graph);
    
}
