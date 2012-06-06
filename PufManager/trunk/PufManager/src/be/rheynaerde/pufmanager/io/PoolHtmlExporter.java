/* PoolHtmlExporter.java
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

package be.rheynaerde.pufmanager.io;

import be.rheynaerde.pufmanager.data.CompetitionPool;
import be.rheynaerde.pufmanager.data.util.SummaryValue;
import be.rheynaerde.pufmanager.data.util.SummaryValuesFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Convenience class to export a pool to HTML. The generated HTML can be
 * formatted using e.g. the following CSS code:
 * <pre>
 * #pouletable tr td {
 *     border: 1px solid #E7E7E7;
 *     padding: 1px;
 * }
 * #pouletable td.pouletd-result {
 *     text-align: center;
 * }
 * #pouletable td.pouletd-total {
 *     background-color: #CCCCCC;
 *     text-align: center;
 * }
 * #pouletable td.pouletd-place {
 *     background-color: #FFFF00;
 *     text-align: center;
 * }
 * #pouletable td.pouletd-head {
 *     font-weight: bold;
 *     text-align: center;
 * }
 * #pouletable td.pouletd-result {
 *     text-align: center;
 * }
 * </pre>
 * 
 * @author nvcleemp
 */
public class PoolHtmlExporter {
    
    private static final List<SummaryValue> DEFAULT_SUMMARIES = 
            Collections.unmodifiableList(SummaryValuesFactory.getCompactList());

    private PoolHtmlExporter() {
        //do not instaniate
    }
    
    public static String exportPool(CompetitionPool pool,
                            Locale locale) throws IOException{
        return exportPool(pool, DEFAULT_SUMMARIES, locale);
    }
    
    public static String exportPool(CompetitionPool pool, 
                            List<SummaryValue> summaries,
                            Locale locale) throws IOException{
        final ResourceBundle bundle = 
                ResourceBundle.getBundle("be.rheynaerde.pufmanager.gui.export", 
                                        locale);
        String number = bundle.getString("poolsheet.html.number");
        String name = bundle.getString("poolsheet.html.name");
        
        StringBuilder builder = new StringBuilder();
        builder.append("<table id=\"pouletable\" border=\"2\" cellspacing=\"0\">\n");
        builder.append("<tbody>\n");
        builder.append("<tr valign=\"bottom\">");
        builder.append(String.format(
                     "<td class=\"pouletd-head\" align=\"center\">%s</td>",
                     number)
                    );
        builder.append(String.format(
                     "<td class=\"pouletd-head\" align=\"center\">%s</td>\n",
                     name)
                    );
        for (int i = 1; i <= pool.getPoolSize(); i++) {
            builder
               .append("<td class=\"pouletd-head\" align=\"center\" width=\"20\">")
               .append(i).append("</td>\n");
        }
        for (SummaryValue summaryValue : summaries) {
            builder
               .append("<td class=\"pouletd-head\" align=\"center\">")
               .append(summaryValue.getName()).append("</td>\n");
        }
        builder.append("</tr>\n");
        
        for (int i = 0; i < pool.getPoolSize(); i++) {
            builder.append("\n");
            builder.append("<tr valign=\"bottom\">\n");
            builder
               .append("<td class=\"pouletd-head\" align=\"center\">")
               .append(i+1).append("</td>\n");
            builder
               .append("<td class=\"pouletd-name\" align=\"center\">")
               .append(pool.getFencerAt(i).getName()).append("</td>\n");
            for (int j = 0; j < pool.getPoolSize(); j++) {
                if(i==j){
                    builder
                       .append("<td class=\"pouletd-black\" style=\"background-color: rgb(0, 0, 0);\" align=\"center\"></td>\n");
                } else if (pool.getResult(pool.getFencerAt(i), pool.getFencerAt(j))!=null){
                    builder
                       .append("<td class=\"pouletd-result\" align=\"center\">")
                       .append(pool.getResult(pool.getFencerAt(i), pool.getFencerAt(j)).toString())
                       .append("</td>\n");
                } else {
                    builder
                       .append("<td class=\"pouletd-result\" align=\"center\">&nbsp;</td>\n");
                }
            }
            for (SummaryValue summaryValue : summaries) {
                if(SummaryValuesFactory.POSITION.equals(summaryValue)){
                    builder
                       .append("<td class=\"pouletd-place\" align=\"center\">")
                       .append(summaryValue.getValue(i, pool).toString())
                       .append("</td>\n");
                } else {
                    builder
                       .append("<td class=\"pouletd-total\" align=\"center\">")
                       .append(summaryValue.getValue(i, pool).toString())
                       .append("</td>\n");
                }
            }
            builder.append("</tr>\n");
        }
        
        builder.append("</tbody>\n");
        builder.append("</table>\n");
        return builder.toString();
    }
}
