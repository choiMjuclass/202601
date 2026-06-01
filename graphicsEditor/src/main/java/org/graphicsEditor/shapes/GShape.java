package org.graphicsEditor.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.InvocationTargetException;

abstract public class GShape implements Cloneable {
    public enum EAnchor {
        eNW, eN, eNE, eE, eSE, eS, eSW, eW, eRR,
        eMove,
        eResize
    }
    protected final float ANCHOR_WIDTH = 10;
    protected final float ANCHOR_HEIGHT = 10;

    protected int x0, y0, x1, y1;
    protected boolean selected;
    protected Shape shape;

    public GShape() {
        this.selected = false;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private Ellipse2D getAnchor(int x, int y) {
        return new Ellipse2D.Float(x - ANCHOR_WIDTH / 2, y - ANCHOR_HEIGHT / 2, ANCHOR_WIDTH, ANCHOR_HEIGHT);
    }

    public EAnchor onShape(int x, int y) {
        if (this.selected) {
            Rectangle r = this.shape.getBounds();
            int w = r.width;
            int h = r.height;
            int x_ = r.x;
            int y_ = r.y;

            if (getAnchor(x_, y_).contains(x, y)) return EAnchor.eNW;
            if (getAnchor(x_ + w / 2, y_).contains(x, y)) return EAnchor.eN;
            if (getAnchor(x_ + w, y_).contains(x, y)) return EAnchor.eNE;
            if (getAnchor(x_ + w, y_ + h / 2).contains(x, y)) return EAnchor.eE;
            if (getAnchor(x_ + w, y_ + h).contains(x, y)) return EAnchor.eSE;
            if (getAnchor(x_ + w / 2, y_ + h).contains(x, y)) return EAnchor.eS;
            if (getAnchor(x_, y_ + h).contains(x, y)) return EAnchor.eSW;
            if (getAnchor(x_, y_ + h / 2).contains(x, y)) return EAnchor.eW;
            if (getAnchor(x_ + w / 2, y_ - 30).contains(x, y)) return EAnchor.eRR;
        }

        if (this.shape.contains(x, y)) {
            return EAnchor.eMove;
        } else {
            return null;
        }
    }

    public void draw(Graphics2D g) {
        g.draw(shape);
        if (this.selected) {
            this.drawAnchors(g);
        }
    }

    private void drawAnchors(Graphics2D g) {
        Rectangle r = this.shape.getBounds();
        int w = r.width;
        int h = r.height;
        int x = r.x;
        int y = r.y;

        g.draw(getAnchor(x, y));
        g.draw(getAnchor(x + w / 2, y));
        g.draw(getAnchor(x + w, y));
        g.draw(getAnchor(x + w, y + h / 2));
        g.draw(getAnchor(x + w, y + h));
        g.draw(getAnchor(x + w / 2, y + h));
        g.draw(getAnchor(x, y + h));
        g.draw(getAnchor(x, y + h / 2));
        g.draw(getAnchor(x + w / 2, y - 30));
    }

    public void resize(int x, int y) {

    }
    public void rotate(int x, int y) {
    }
    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}
    public void translate(int dx, int dy) {}


}
