/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.bezier;

import types.ICanvas;
import types.ITool;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import tools.bezier.BezierCurves.ControlPoint;
import types.CanvasShape;

/**
 *
 * @author dpf
 */
public class Bezier implements ITool {

    private static final String NAME = "Bezier curves";
    private static final String ICON = "bezier.png";
    private static final Cursor CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
    public static final int CONTROL_POINTS_SIZE = 1;
    public static final int CONTROL_POINT_RADIUS = 10;
    private static final Color CONTROL_COLOR = Color.BLACK;
    private static final int MAX_SIZE = 20;

    private final BezierUtilityPanel bup;

    private ICanvas canvas = null;
    private BezierCurves bc = null;
    private CanvasShape bezier = null;
    private Path2D points = null;
    private Path2D polygon = null;
    private Path2D curve = null;
    private ControlPoint controlPoint = null;
    public int control_polygon_size;

    public Bezier() {
        bup = new BezierUtilityPanel(this, MAX_SIZE);
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        MouseAdapter mouseAdapter = null;
        if (canvas != null) {
            mouseAdapter = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == 1 && bc != null) {
                        controlPoint = bc.isControlPoint(e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (controlPoint != null) {
                        controlPoint.moveTo(e.getX(), e.getY());
                        recompute();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (controlPoint != null) {
                        controlPoint = null;
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 1) {
                        if (bc == null) {
                            bc = new BezierCurves();
                            bup.setBezierCurves(bc);
                        } else if (bc.isControlPoint(e.getX(), e.getY()) != null) {
                            return;
                        }
                        bc.addPoint(e.getX(), e.getY());
                        recompute();
                    } else if (e.getButton() == 3 && (bc.isControlPoint(e.getX(), e.getY())) != null) {
                        bc.removePoint(bc.isControlPoint(e.getX(), e.getY()));
                        recompute();
                    } else if (e.getButton() == 2) {
                        bc.degreeRaisingBezier();
                        recompute();
                    }
                }

            };
        }
        return mouseAdapter;
    }

    public void recompute() {
        if (bc != null) {
            canvas.removeShape(bezier);
            points = bc.getPointsShape();
            polygon = bc.getPolygonShape();
            curve = bc.getBezierShape();
            List<CanvasShape> children = new LinkedList<>();
            children.add(new CanvasShape(this, points, CONTROL_COLOR, CONTROL_POINTS_SIZE));
            children.add(new CanvasShape(this, polygon, CONTROL_COLOR, control_polygon_size));
            bezier = new CanvasShape(this, curve, bup.getColor(), bup.getDrawSize(), children, bc, bup.getColor(), bup.getDrawSize());
            canvas.addShape(bezier);
            canvas.reDraw();
            bup.updateDegreeLabel();
        }
    }

    public void subdivision(float t) {
        if (bc != null) {
            BezierCurves newBc = bc.subdivision(t);
            if (newBc != null) {
                Path2D newPoints = newBc.getPointsShape();
                Path2D newPolygon = newBc.getPolygonShape();
                Path2D newCurve = newBc.getBezierShape();
                List<CanvasShape> children = new LinkedList<>();
                children.add(new CanvasShape(this, newPoints, CONTROL_COLOR, CONTROL_POINTS_SIZE));
                children.add(new CanvasShape(this, newPolygon, CONTROL_COLOR, control_polygon_size));
                CanvasShape newBezier = new CanvasShape(this, newCurve, bup.getColor(), bup.getDrawSize(), children, newBc, bup.getColor(), bup.getDrawSize());
                canvas.addShape(newBezier);
                recompute();
            }
        }
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
    public void setCanvas(ICanvas canvas) {
        this.canvas = canvas;
        control_polygon_size = CanvasShape.DASHED_LINE;
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
        up.add(bup);
        return up;
    }

    @Override
    public void onClose() {
        bc = null;
        bezier = null;
        points = null;
        polygon = null;
        curve = null;
        controlPoint = null;
    }

    @Override
    public void reOpen(Object... args) {
        bc = (BezierCurves) args[0];
        bup.setColor((Color) args[1]);
        bup.setDrawSize((int) args[2]);
        bup.setBezierCurves(bc);
        recompute();
    }

}
