/* SegmentedLineBorder.java
 * =========================================================================
 * This file is part of the Fence project
 * More info can be found at http://nvcleemp.wordpress.com
 * 
 * Copyright (C) 2010-2011 Nico Van Cleemput
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package be.rheynaerde.pufmanager.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/**
 * A class that implements a line border of a single color and an
 * arbitrary thickness for each of the 4 sides. This class is loosely
 * based upon the LineBorder class in Swing.
 *
 * @author nvcleemp
 */
public class SegmentedLineBorder extends AbstractBorder {
    protected int thicknessTop, thicknessLeft, thicknessBottom, thicknessRight;
    protected Color lineColor;

    /**
     * Creates a segmented line border with the specified color and a
     * thickness = 1 for all sides.
     * @param color the color for the border
     */
    public SegmentedLineBorder(Color color) {
        this(color, 1, 1, 1, 1);
    }

    /**
     * Creates a line border with the specified color and thickness
     * for all sides.
     * @param color the color of the border
     * @param thickness the thickness of the border for all sides
     */
    public SegmentedLineBorder(Color color, int thickness)  {
        this(color, thickness, thickness, thickness, thickness);
    }

    /**
     * Creates a line border with the specified color and thickness
     * for each sides.
     * @param color the color of the border
     * @param thicknessTop the thickness of the top of the border
     * @param thicknessLeft the thickness of the left of the border
     * @param thicknessBottom the thickness of the bottom of the border
     * @param thicknessRight the thickness of the right of the border
     */
    public SegmentedLineBorder(Color color, int thicknessTop, int thicknessLeft, int thicknessBottom, int thicknessRight)  {
        lineColor = color;
        this.thicknessTop = thicknessTop;
        this.thicknessLeft = thicknessLeft;
        this.thicknessBottom = thicknessBottom;
        this.thicknessRight = thicknessRight;
    }

    /**
     * Paints the border for the specified component with the
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g = g.create();

        g.setColor(lineColor);

        //top
        if(thicknessTop!=0)
            g.fillRect(x, y, width-1, thicknessTop);

        if(thicknessLeft!=0)
            g.fillRect(x, y, thicknessLeft, height-1);

        if(thicknessBottom!=0)
            g.fillRect(x, y + height - 1 - thicknessBottom, width - 1, thicknessBottom);

        if(thicknessRight!=0)
            g.fillRect(x + width - 1 - thicknessRight, y, thicknessRight, height-1);

    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    @Override
    public Insets getBorderInsets(Component c)       {
        return new Insets(thicknessTop, thicknessLeft, thicknessBottom, thicknessRight);
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = thicknessLeft;
        insets.top = thicknessTop;
        insets.right = thicknessRight;
        insets.bottom = thicknessBottom;
        return insets;
    }

    /**
     * Returns the color of the border.
     */
    public Color getLineColor()     {
        return lineColor;
    }

}
