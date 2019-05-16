/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import lib.NoDuplicatesList;
import types.ICanvas;
import types.IGraph;
import types.ITool;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import types.CanvasShape;

/**
 *
 * @author dpf
 */
public class Canvas extends JPanel implements ICanvas {

    private final Stack<CanvasShape> shapes;
    private final Stack<CanvasShape> shapesStack;
    private final List<ITool> availableTools;
    private boolean antialiasing;

    private MouseAdapter ma = null;
    private ITool usingTool;
    private CanvasShape targetShape;
    private IGraph graph;

    public Canvas() {
        this(true);
    }

    public Canvas(boolean antialiasing) {
        this.setBackground(DEFAULT_BG_COLOR);
        this.antialiasing = antialiasing;
        this.usingTool = null;
        this.shapes = new Stack<>();
        this.shapesStack = new Stack<>();
        this.availableTools = new NoDuplicatesList<>();
    }

    public boolean antialiasing() {
        return antialiasing == true;
    }

    public void setAntialiasing(boolean antialiasing) {
        this.antialiasing = antialiasing;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (antialiasing) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        shapes.stream().forEach((CanvasShape shape) -> {
            drawShape(g2d, shape);
        });
    }

    private void drawShape(Graphics2D g2d, CanvasShape shape) {
        g2d.setPaint(shape.getColor());
        g2d.setStroke(shape.getSize());
        g2d.draw(shape.getShape());
        if (shape == targetShape) {
            shape.getChildren().stream().forEach((child) -> {
                drawShape(g2d, child);
            });
        }
    }

    @Override
    public void addShape(CanvasShape shape) {
        shapes.add(shape);
        shapesStack.clear();
        targetShape = shape;
    }

    @Override
    public void reDraw() {
        repaint();
    }

    @Override
    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        if (ma != null) {
            this.removeMouseListener(ma);
            this.removeMouseMotionListener(ma);
        }
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        ma = mouseAdapter;
    }

    @Override
    public void unDo() {
        if (!shapes.isEmpty()) {
            shapesStack.add(shapes.pop());
            targetShape = null;
            usingTool.onClose();
            reDraw();
        }
    }

    @Override
    public void reDo() {
        if (!shapesStack.isEmpty()) {
            shapes.add(shapesStack.pop());
            targetShape = null;
            usingTool.onClose();
            reDraw();
        }
    }

    @Override
    public void removeShape(CanvasShape shape) {
        shapes.remove(shape);
    }

    @Override
    public Color getBackgroundColor() {
        return DEFAULT_BG_COLOR;
    }

    @Override
    public void openTool(ITool tool, CanvasShape targetShape) { 
        if (usingTool != null) {
            usingTool.onClose();
        }
        usingTool = tool;
        setMouseAdapter(usingTool.getMouseAdapter());
        if (usingTool.getCursor() != null) {
            setCursor(usingTool.getCursor());
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }        
        graph.getUtiliyPanel().removeAll();
        if (usingTool.getUtilityPanel() != null) {
            usingTool.getUtilityPanel().stream().forEach((up) -> {
                graph.getUtiliyPanel().add(up);
            });
        }
        this.targetShape = targetShape;
        if(this.targetShape != null){
            tool.reOpen(targetShape.getItems().toArray());
            removeShape(targetShape);
        }
        ((JFrame) graph).revalidate();
        ((JFrame) graph).repaint();
    }

    @Override
    public void addTool(ITool tool) {
        availableTools.add(tool);
        tool.setCanvas(this);
    }

    @Override
    public List<CanvasShape> getShapes() {
        return shapes;
    }

    @Override
    public void setGraph(IGraph graph) {
        this.graph = graph;
    }

}
