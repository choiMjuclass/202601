package org.graphicsEditor.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
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
    protected AffineTransform affineTransform;

    public GShape() {
        this.selected = false;
        this.affineTransform = new AffineTransform();
    }

    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();
            cloned.shape = (Shape) (((RectangularShape) this.shape).clone());
            cloned.affineTransform = (AffineTransform) this.affineTransform.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    public Shape getShape() {
        return shape;
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
        Point p = new Point(x, y);
        try {
            this.affineTransform.inverseTransform(p, p);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        if (this.selected) {
            Rectangle r = this.shape.getBounds();
            int w = r.width;
            int h = r.height;
            int x_ = r.x;
            int y_ = r.y;

            if (getAnchor(x_, y_).contains(p.x, p.y)) return EAnchor.eNW;
            if (getAnchor(x_ + w / 2, y_).contains(p.x, p.y)) return EAnchor.eN;
            if (getAnchor(x_ + w, y_).contains(p.x, p.y)) return EAnchor.eNE;
            if (getAnchor(x_ + w, y_ + h / 2).contains(p.x, p.y)) return EAnchor.eE;
            if (getAnchor(x_ + w, y_ + h).contains(p.x, p.y)) return EAnchor.eSE;
            if (getAnchor(x_ + w / 2, y_ + h).contains(p.x, p.y)) return EAnchor.eS;
            if (getAnchor(x_, y_ + h).contains(p.x, p.y)) return EAnchor.eSW;
            if (getAnchor(x_, y_ + h / 2).contains(p.x, p.y)) return EAnchor.eW;
            if (getAnchor(x_ + w / 2, y_ - 30).contains(p.x, p.y)) return EAnchor.eRR;
        }

        if (this.shape.contains(p.x, p.y)) {
            return EAnchor.eMove;
        } else {
            return null;
        }
    }

    public void draw(Graphics2D g) {
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        g.draw(transformedShape);
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

        g.draw(this.affineTransform.createTransformedShape(getAnchor(x, y)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x + w / 2, y)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x + w, y)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x + w, y + h / 2)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x + w, y + h)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x + w / 2, y + h)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x, y + h)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x, y + h / 2)));
        g.draw(this.affineTransform.createTransformedShape(getAnchor(x + w / 2, y - 30)));
    }

    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}

}
