/* AbstractPoolSheetConfiguration.java
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

package be.rheynaerde.poolsheets.configuration;

import be.rheynaerde.poolsheets.boutorder.BoutOrder;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public interface AbstractPoolSheetConfiguration {
    public static final int TITLE_PAGE = 0;
    public static final int BOUT_ORDER_PAGE = 1;

    String getTitle();
    Font getTitleFont();
    String getSubtitle();
    Font getSubtitleFont();
    Font getHeaderFont();
    float getSquareCellSize();
    Image getImage();
    boolean includeOrderOfBouts();
    BoutOrder getBoutOrder();
    int getBoutOrderColumns();
    int getBoutOrderSpacing();
    boolean putBoutOrderOnNewPage();
    Locale getLocale();
    String getResult(int player, int opponent);
    boolean isLandscape(int page);
}
