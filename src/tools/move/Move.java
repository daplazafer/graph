/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.move;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import types.CanvasShape;
import types.ICanvas;
import types.ITool;

/**
 *
 * @author dpf
 */
public class Move implements ITool {

    private static final String NAME = "Move";
    private static final String ICON = "move.png";
    private static final Cursor CURSOR = new Cursor(Cursor.MOVE_CURSOR);
    private ICanvas canvas = null;

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

                Shape movingShape;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == 1) {
                        int css = ITool.CLICK_SELECTION_SIZE;
                        Rectangle2D selection = new Rectangle2D.Float(e.getX() - css / 2, e.getY() - css / 2, css, css);
                        for (CanvasShape shape : canvas.getShapes()) {
                            if (shape.getShape().intersects(selection)) {
                                movingShape = shape.getShape();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == 0 && movingShape != null) {
                        //TODO: move
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == 1) {
                        movingShape = null;
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {

                }
            };
        }
        return mouseAdapter;
    }

    @Override
    public List<JPanel> getUtilityPanel() {
        return null;
    }

    @Override
    public void setCanvas(ICanvas canvas) {
        this.canvas = canvas;
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
    public void onClose() {

    }

    @Override
    public void reOpen(Object... args) {

    }

}
