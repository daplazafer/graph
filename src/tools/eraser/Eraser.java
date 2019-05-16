/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.eraser;

import types.ICanvas;
import types.ITool;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import tools.SizeChooserPanel;
import types.CanvasShape;


/**
 *
 * @author dpf
 */
public class Eraser implements ITool{
    
    private static final String NAME = "Eraser";
    private static final String ICON = "eraser.png";
    private static final Cursor CURSOR = null;
    private static final int MAX_SIZE = 50;
    private ICanvas canvas = null;
    private final SizeChooserPanel scp;

    public Eraser() {
        scp = new SizeChooserPanel(MAX_SIZE);
    }

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
                
                CanvasShape currentShape = null;
                Path2D shape = null;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == 1) {
                        shape = new Path2D.Float();
                        shape.moveTo(e.getX(), e.getY());
                        currentShape = new CanvasShape(Eraser.this, shape, canvas.getBackgroundColor(),scp.getDrawSize());
                        canvas.addShape(currentShape);
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (currentShape != null) {
                        shape.lineTo(e.getX(), e.getY());
                        canvas.reDraw();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == 1) {
                        currentShape = null;
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 1) {
                        canvas.addShape(new CanvasShape(Eraser.this, new Rectangle2D.Float(e.getX() - 0.5f, e.getY() - 0.5f, 1, 1), canvas.getBackgroundColor(), scp.getDrawSize()));
                        canvas.reDraw();
                    } else if (e.getButton() == 3 && currentShape != null) {
                        canvas.removeShape(currentShape);
                        canvas.reDraw();
                    }
                }

            };
        }

        return mouseAdapter;
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
    public List<JPanel> getUtilityPanel() {
        List<JPanel> up = new LinkedList<>();
        up.add(scp);
        return up;
    }

    @Override
    public void onClose() {
        
    }

    @Override
    public void reOpen(Object... args) {

    }
    
}
