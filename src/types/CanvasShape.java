/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dpf
 */
public final class CanvasShape {
    
    public static int DASHED_LINE = -789172;

    private Shape shape;
    private Color color;
    private BasicStroke size;
    private List<CanvasShape> children;
    private ITool tool;
    private List<Object> items;
    
    public CanvasShape(ITool tool, Shape shapeList, Color color, int size, Object... items){
        this(tool, shapeList, color, size, null, items);
    }
    
    public CanvasShape(ITool tool, Shape shapeList, Color color, int size, List<CanvasShape> children, Object... items) {
        this.tool = tool;
        this.shape = shapeList;
        this.color = color;
        if(size == DASHED_LINE){
            this.size = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        }else{
            this.size = new BasicStroke(size);
        }
        this.children = new LinkedList<>();
        if(children != null){
            children.stream().forEach((cs) -> {
                this.children.add(cs);
            });    
        }
        this.items = new LinkedList<>();
        if(items != null){
            this.items.addAll(Arrays.asList(items));
        }
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public BasicStroke getSize() {
        return size;
    }

    public List<CanvasShape> getChildren() {
        return children;
    }
    
    public ITool getTool() {
        return tool;
    }

    public List<Object> getItems() {
        return items;
    }
    
}
