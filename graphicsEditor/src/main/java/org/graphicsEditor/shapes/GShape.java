package org.graphicsEditor.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.InvocationTargetException;

abstract public class GShape implements Cloneable {
    public enum EAnchor {
        eNW,
        eNN,
        eNE,
        eEE,
        eSE,
        eSS,
        eSW,
        eWW,
        eRotate,
        eMove,
    }
    private boolean isSelected;

    protected Shape shape;
    private final Anchors anchors;

    public GShape() {
        this.isSelected = false;
        this.anchors = new Anchors();
    }
    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();
            cloned.shape = (Shape) (((RectangularShape) this.shape).clone());
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    // getter and setter
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            this.anchors.setPosition(this.shape.getBounds());
        }
    }

    public EAnchor onShape(int x, int y) {
        EAnchor eAnchor = null;
        if (isSelected) {
            eAnchor = this.anchors.onShape(x, y);
        }
        if (eAnchor == null) {
            if (this.shape.contains(x, y)) {
                eAnchor = EAnchor.eMove;
            }
        }
        return eAnchor;
    }

    public void draw(Graphics2D g) {
        if (isSelected) {
            this.anchors.draw(g);
        }
        g.draw(shape);
    }
    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}
    public void translate(int dx, int dy) {}

    private static class Anchors {
        public int w = 15;
        public int h = 15;

        private final Ellipse2D[] anchors;

        public Anchors() {
            anchors = new Ellipse2D[EAnchor.values().length-1];
            for (int i = 0; i < anchors.length-1; i++) {
                this.anchors[i] = new Ellipse2D.Float();
            }
        }
        public EAnchor onShape(int x, int y) {
             for (int i = 0; i < anchors.length-1; i++) {
                if (this.anchors[i].contains(x, y)) {
                    return EAnchor.values()[i];
                }
            }
            return null;
        }
        public void setPosition(Rectangle br) {
            int brw = br.width;
            int brh = br.height;
            this.anchors[EAnchor.eNW.ordinal()].setFrame(br.x, br.y - h, w, h);
            this.anchors[EAnchor.eNN.ordinal()].setFrame(br.x, br.y - h, w, h);
            this.anchors[EAnchor.eNE.ordinal()].setFrame(br.x, br.y - h, w, h);
            this.anchors[EAnchor.eEE.ordinal()].setFrame(br.x, br.y - h, w, h);
            this.anchors[EAnchor.eSE.ordinal()].setFrame(br.x+brw, br.y+brh - h, w, h);
            this.anchors[EAnchor.eSS.ordinal()].setFrame(br.x, br.y - h, w, h);
            this.anchors[EAnchor.eSW.ordinal()].setFrame(br.x, br.y - h, w, h);
            this.anchors[EAnchor.eWW.ordinal()].setFrame(br.x, br.y - h, w, h);
 //           this.anchors[EAnchor.eRotate.ordinal()].setFrame(br.x, br.y - h, w, h);
        }
        public void draw(Graphics2D g) {
            for (int i = 0; i < anchors.length-1; i++) {
                g.draw(anchors[i]);
            }
        }
    }

}
