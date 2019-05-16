/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.draw;

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
import tools.ColorChooserPanel;
import tools.SizeChooserPanel;
import types.CanvasShape;

/**
 *
 * @author dpf
 */
public class Draw implements ITool {

    private static final String NAME = "Draw";
    private static final String ICON = "draw.png";
    private static final Cursor CURSOR = null;
    private static final int MAX_SIZE = 20;
    private ICanvas canvas = null;
    private final ColorChooserPanel ccp;
    private final SizeChooserPanel scp;

    public Draw() {
        ccp = new ColorChooserPanel();
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
                        currentShape = new CanvasShape(Draw.this, shape, ccp.getColor(), scp.getDrawSize());
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
                        canvas.addShape(new CanvasShape(Draw.this, new Rectangle2D.Float(e.getX() - 0.5f, e.getY() - 0.5f, 1, 1), ccp.getColor(), scp.getDrawSize()));
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
        //return Toolkit.getDefaultToolkit().createCustomCursor(getIcon().getImage(), new Point(0, 0), "custom cursor");
    }

    @Override
    public List<JPanel> getUtilityPanel() {
        List<JPanel> up = new LinkedList<>();
        up.add(ccp);
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
