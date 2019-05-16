/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.selection;

import graph.App;
import java.awt.Color;
import types.ICanvas;
import types.ITool;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import types.CanvasShape;

/**
 *
 * @author dpf
 */
public class Selection implements ITool {

    private static final String NAME = "Selection";
    private static final String ICON = "selection.png";
    private static final Cursor CURSOR = null;
    private ICanvas canvas = null;

    public int selection_rectangle_size;
    private static final Color SELECTION_COLOR = Color.BLACK;

    private CanvasShape selectionRectangle;
    private Rectangle2D selectionShape = null;
    private int x, y;

    @Override
    public ImageIcon getIcon() {
        try {
            return new ImageIcon(ImageIO.read(getClass().getResource(ICON)));
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        MouseAdapter mouseAdapter = null;
        if (canvas != null) {
            mouseAdapter = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == 1) {
                        x = e.getX();
                        y = e.getY();
                        selectionShape = new Rectangle2D.Float(x, y, 0, 0);
                        selectionRectangle = new CanvasShape(Selection.this, selectionShape, SELECTION_COLOR, selection_rectangle_size);
                        canvas.addShape(selectionRectangle);
                        canvas.reDraw();
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == 0 && selectionRectangle != null) {
                        selectionShape.setRect(x, y, e.getX() - x, e.getY() - y);
                        canvas.reDraw();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == 1) {
                        canvas.removeShape(selectionRectangle);
                        selectionRectangle = null;
                        canvas.reDraw();
                        testIntersects();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 1) {
                        int css = ITool.CLICK_SELECTION_SIZE;
                        selectionShape = new Rectangle2D.Float(x-css/2, y-css/2, css, css);
                        testIntersects();
                    }
                }
            };
        }
        return mouseAdapter;
    }

    private void testIntersects() {
        boolean flag;
        for (CanvasShape s : canvas.getShapes()) {
            flag = true;
            if (s.getShape().intersects(selectionShape)) {
                for(ITool t : App.BASIC_TOOLS){
                    if(t == s.getTool()){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    canvas.openTool(s.getTool(), s);
                }
                return;
            }
        }
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        this.canvas = canvas;
        selection_rectangle_size = CanvasShape.DASHED_LINE;
    }

    @Override
    public String getToolName() {
        return NAME;
    }

    @Override
    public Cursor getCursor() {
        return CURSOR;
    }

    @Override
    public List<JPanel> getUtilityPanel() {
        return null;
    }

    @Override
    public void onClose() {
        canvas.removeShape(selectionRectangle);
    }

    @Override
    public void reOpen(Object... args) {

    }

}
