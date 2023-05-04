package com.hl7soft.sevenedit.util.licapi.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.util.HashMap;

public class XYLayout implements LayoutManager2 {
    HashMap compPos = new HashMap<>();

    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints == null) {
            this.compPos.put(comp, comp.getLocation());
            return;
        }
        if (!(constraints instanceof Point))
            throw new RuntimeException("Constraints parameter should be java.awt.Point!");
        this.compPos.put(comp, constraints);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.0F;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.0F;
    }

    public void invalidateLayout(Container target) {}

    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    public void addLayoutComponent(String name, Component comp) {}

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int nComps = parent.getComponentCount();
        for (int i = 0; i < nComps; i++) {
            Component c = parent.getComponent(i);
            if (c.isVisible()) {
                Dimension d = c.getPreferredSize();
                Point pos = (Point)this.compPos.get(c);
                if (pos != null)
                    c.setBounds((int)pos.getX(), (int)pos.getY(), (int)d.getWidth(), (int)d.getHeight());
            }
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(100, 100);
    }

    public Dimension preferredLayoutSize(Container parent) {
        int xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE, xmax = 0, ymax = 0;
        int nComps = parent.getComponentCount();
        for (int i = 0; i < nComps; i++) {
            Component c = parent.getComponent(i);
            if (c.isVisible()) {
                Dimension d = c.getPreferredSize();
                Point pos = (Point)this.compPos.get(c);
                if (pos != null) {
                    xmin = (int)Math.min(xmin, pos.getX());
                    ymin = (int)Math.min(xmin, pos.getY());
                    xmax = (int)Math.max(xmax, pos.getX() + d.getWidth());
                    ymax = (int)Math.max(ymax, pos.getY() + d.getHeight());
                }
            }
        }
        return new Dimension(xmax - xmin, ymax - ymin);
    }

    public void removeLayoutComponent(Component comp) {
        this.compPos.remove(comp);
    }
}
