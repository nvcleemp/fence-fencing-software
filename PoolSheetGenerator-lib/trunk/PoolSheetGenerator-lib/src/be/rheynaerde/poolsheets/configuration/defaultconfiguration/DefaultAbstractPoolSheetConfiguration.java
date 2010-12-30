/* DefaultAbstractPoolSheetConfiguration.java
 * =========================================================================
 * This file is part of the PoolSheetGenerator project
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

package be.rheynaerde.poolsheets.configuration.defaultconfiguration;

import be.rheynaerde.poolsheets.boutorder.BoutOrder;
import be.rheynaerde.poolsheets.boutorder.EmptyBoutOrder;
import be.rheynaerde.poolsheets.configuration.AbstractPoolSheetConfiguration;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author nvcleemp
 */
public abstract class DefaultAbstractPoolSheetConfiguration implements AbstractPoolSheetConfiguration{

    private float squareCellSize;
    private Image image;
    private String title;
    private String subtitle;
    private Locale locale;

    public DefaultAbstractPoolSheetConfiguration(float squareCellSize, Image image, Locale locale) {
        this(squareCellSize, image, locale, null, null);
    }

    public DefaultAbstractPoolSheetConfiguration(float squareCellSize, Image image, Locale locale, String title, String subtitle) {
        this.squareCellSize = squareCellSize;
        this.image = image;
        this.locale = locale;
        ResourceBundle bundle =
                ResourceBundle.getBundle
                ("be.rheynaerde.poolsheets.configuration.defaultconfiguration.defaultabstractpoolsheet",
                locale);
        this.title = title == null ? bundle.getString("title") : title;
        this.subtitle = subtitle == null ? bundle.getString("subtitle") : subtitle;
    }

    public String getTitle() {
        return title;
    }

    public Font getTitleFont(){
        return FontFactory.getFont("helvetica", 32f, Font.BOLD);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Font getSubtitleFont(){
        return FontFactory.getFont("helvetica", 24f, Font.BOLD);
    }

    public Font getHeaderFont(){
        return FontFactory.getFont("helvetica", 12f, Font.BOLD);
    }

    public float getSquareCellSize() {
        return squareCellSize;
    }

    public Image getImage() {
        return image;
    }

    public boolean includeOrderOfBouts() {
        return true;
    }

    public BoutOrder getBoutOrder() {
        return EmptyBoutOrder.getInstance();
    }

    public int getBoutOrderColumns() {
        return 1;
    }

    public int getBoutOrderSpacing() {
        return 0;
    }

    public boolean putBoutOrderOnNewPage() {
        return false;
    }

    public Locale getLocale(){
        return locale;
    }

    public String getResult(int player, int opponent){
        return null;
    }

    public boolean isLandscape(int page){
        return false;
    }
}
