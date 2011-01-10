/* CompetitionSettings.java
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

package be.rheynaerde.pufmanager.data;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nvcleemp
 */
public class CompetitionSettings {

    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("be.rheynaerde.pufmanager.data.settings");

    private String title;
    private String subtitle;
    private Locale locale;
    private URL imageUrl;
    private Image image;

    public CompetitionSettings() {
        this.title = BUNDLE.getString("title");
        this.subtitle = BUNDLE.getString("subtitle");
        this.locale = Locale.getDefault();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        if(!this.imageUrl.equals(imageUrl)){
            this.imageUrl = imageUrl;
            image = null;
        }
    }

    public Image getImage() {
        if(image!=null || imageUrl == null)
            return image;
        else {
            try {
                image = Image.getInstance(imageUrl);
            } catch (BadElementException ex) {
                Logger.getLogger(CompetitionSettings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(CompetitionSettings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CompetitionSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
            return image;
        }
    }

}
