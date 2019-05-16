/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.bezier;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import lib.MathF;

/**
 *
 * @author dpf
 */
public class BezierCurves {

    private static final int STEPS_FACTOR = 6;

    public final class ControlPoint {

        private int x, y;
        private Shape area;
        private final static int CPR = Bezier.CONTROL_POINT_RADIUS;

        public ControlPoint(int x, int y) {
            moveTo(x, y);
        }

        public void moveTo(int x, int y) {
            this.x = x;
            this.y = y;
            //area = new Ellipse2D.Float(x - CPR / 2, y - CPR / 2, CPR, CPR);
            area = new Rectangle2D.Float(x-CPR/2, y-CPR/2, CPR, CPR);
            
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    private List<ControlPoint> controlPoints;

    public BezierCurves() {
        controlPoints = new ArrayList<>();
    }

    public int getDegree(){
        return controlPoints.size();
    }
    
    public Path2D getPointsShape() {
        Path2D points = new Path2D.Float();
        for (int i = 0; i < controlPoints.size(); i++) {
            points.moveTo(controlPoints.get(i).x, controlPoints.get(i).y);
            points.append(controlPoints.get(i).area, false);
        }
        return points;
    }

    public Path2D getPolygonShape() {
        Path2D controlPolygon = new Path2D.Float();
        for (int i = 0; i < controlPoints.size(); i++) {
            controlPolygon.moveTo(controlPoints.get(i).x, controlPoints.get(i).y);
            if (i <= controlPoints.size() - 2) {
                controlPolygon.append(new Line2D.Float(controlPoints.get(i).x, controlPoints.get(i).y, controlPoints.get(i + 1).x, controlPoints.get(i + 1).y), false);
            }
        }
        return controlPolygon;
    }

    public Path2D getBezierShape() {
        Path2D bezierCurve = new Path2D.Float();
        if (controlPoints.size() < 2) {
            return bezierCurve;
        }
        double step = 1.0 / (STEPS_FACTOR * controlPoints.size());
        bezierCurve.moveTo(controlPoints.get(0).x, controlPoints.get(0).y);
        double[] point;
        for (double t = 0; t <= 1; t += step) {
            point = bezier(t);
            //point = castlejau(t);
            bezierCurve.lineTo(point[0], point[1]);
        }
        bezierCurve.lineTo(controlPoints.get(controlPoints.size() - 1).x, controlPoints.get(controlPoints.size() - 1).y);
        return bezierCurve;
    }

    private double[] bezier(double t) {
        double[] Bt = {0.0, 0.0};
        int degree = controlPoints.size() - 1;
        double b, comb;
        for (int i = 0; i <= degree; i++) {
            comb = MathF.combinatorial(degree, i);
            b = (comb * Math.pow(t, i) * Math.pow((1 - t), (degree - i)));
            Bt[0] += controlPoints.get(i).x * b;
            Bt[1] += controlPoints.get(i).y * b;
        }
        return Bt;
    }
    
    private double[] castlejau(double t) {
        //TODO
        return null;
    }

    public void degreeRaisingBezier() {
        if (controlPoints.size()>1) {
            List<ControlPoint> ncp = new ArrayList<>();
            ncp.add(controlPoints.get(0));
            double n = controlPoints.size() - 1; // degree
            double nx, ny;
            for (int i = 1; i <= n; i++) {
                nx = ((i / (n + 1.0)) * controlPoints.get(i - 1).x) + (1.0 - (i / (n + 1))) * controlPoints.get(i).x;
                ny = ((i / (n + 1.0)) * controlPoints.get( i - 1).y) + (1.0 - (i / (n + 1))) * controlPoints.get(i).y;
                ncp.add(new ControlPoint((int) nx, (int) ny));
            }
            ncp.add(controlPoints.get(controlPoints.size() - 1));
            controlPoints = ncp;
        }
    }
    
    public void degreeRaisingCasteljau(){
        if (controlPoints.size()>1) {
            List<ControlPoint> ncp = new ArrayList<>();
            ncp.add(controlPoints.get(0));
            double step = 1.0 / controlPoints.size();
            double nx, ny;
            for(int i = 1 ; i < controlPoints.size() ; i++){
                nx = controlPoints.get(i).x + step*i*(controlPoints.get(i-1).x-controlPoints.get(i).x);
                ny = controlPoints.get(i).y + step*i*(controlPoints.get(i-1).y-controlPoints.get(i).y);
                ncp.add(new ControlPoint((int) nx, (int) ny));
            }
            ncp.add(controlPoints.get(controlPoints.size() - 1));
            controlPoints = ncp;
        }
    }
    
    public BezierCurves subdivision1(double t){
        if(!(t > 0 && t < 1)){
            return null;
        }
        t = 1-t;
        double nx, ny;
        
        List<ControlPoint> ncp1 = new ArrayList<>();
        List<ControlPoint> ncp2 = new ArrayList<>();
        
        while(controlPoints.size()>2){
            
        }
        
        
        
        return null;
    }
    
    public BezierCurves subdivision(double t){
        if(!(t > 0 && t < 1)){
            return null;
        }
        t = 1-t;
        double nx, ny;
        int max = (controlPoints.size()*2) - (controlPoints.size()+3);
        for(int j = 0 ; j< max ;j++){
            List<ControlPoint> ncp = new ArrayList<>();
            ncp.add(controlPoints.get(0));
            for(int i = 1 ; i < controlPoints.size() ; i++){
                nx = controlPoints.get(i).x + t*(controlPoints.get(i-1).x-controlPoints.get(i).x);
                ny = controlPoints.get(i).y + t*(controlPoints.get(i-1).y-controlPoints.get(i).y);
                ncp.add(new ControlPoint((int) nx, (int) ny));
            }
            ncp.add(controlPoints.get(controlPoints.size() - 1));
            controlPoints = ncp;
        }

        List<ControlPoint> ncp1 = new ArrayList<>();
        List<ControlPoint> ncp2 = new ArrayList<>();
        
        int i;
        for(i = 0; i< controlPoints.size()/2 ;i++){
            ncp1.add(controlPoints.get(i));
        }
        nx = controlPoints.get(i).x + t*(controlPoints.get(i-1).x-controlPoints.get(i).x);
        ny = controlPoints.get(i).y + t*(controlPoints.get(i-1).y-controlPoints.get(i).y);
        ControlPoint p1 = new ControlPoint((int) nx, (int) ny);
        i++;
        nx = controlPoints.get(i).x + t*(controlPoints.get(i-1).x-controlPoints.get(i).x);
        ny = controlPoints.get(i).y + t*(controlPoints.get(i-1).y-controlPoints.get(i).y);
        ControlPoint p4 = new ControlPoint((int) nx, (int) ny);
        nx = p4.x + t*(p1.x-p4.x);
        ny = p4.y + t*(p1.y-p4.y);
        ControlPoint p2 = new ControlPoint((int) nx, (int) ny);
        ControlPoint p3 = new ControlPoint((int) nx, (int) ny);
        ncp1.add(p1);
        ncp1.add(p2);
        ncp2.add(p3);
        ncp2.add(p4);
        
        for(i = controlPoints.size()/2+1; i< controlPoints.size() ;i++){
            ncp2.add(controlPoints.get(i));
        }
        
        controlPoints = ncp1;
        BezierCurves newCurve = new BezierCurves();
        newCurve.controlPoints = ncp2;

        return newCurve;

    }

    public ControlPoint isControlPoint(double x, double y) {
        for (ControlPoint p : controlPoints) {
            if (p.area.contains(x, y)) {
                return p;
            }
        }
        return null;
    }

    public void addPoint(int x, int y) {
        controlPoints.add(new ControlPoint(x, y));
    }

    public void removePoint(ControlPoint p) {
        controlPoints.remove(p);
    }

    public void movePoint(ControlPoint p, int new_x, int new_y) {
        p.moveTo(new_x, new_y);
    }

}
