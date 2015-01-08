/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozcanlab.utils;

/**
 *
 * @author SaqibIsGreat
 */
public class Constants {

    public static final String APP_TAG = "TestGoogleGlass";
    public static final String HOME_CARD = "HomeCardIdent";
    
    public static final int PORT = 9090;
    public static final String ADDRESS = "devpc02.ee.ucla.edu"; // Devpc02.ee.ucla.edu
    
    // For communicating throught URI
    public static final String URI_FORM = "rdtglass://";
    public static final String HOME_CARD_HTML =
            "<article>\n  "
            + "   <section>\n"
            + "      <p class=\"text-large\">"
            + "          Welcome to <br /><em class=\"red\">Rapid Diagnostics Tester</em><br />\n"
            + "      </p>\n  "
            + "      <hr />\n"
            + "      <p class=\"text-x-small\">"
            + "          <b>Tap</b> process to get <em class=\"green\">started</em>"
            + "      </p>"
            + "   </section>\n"
            + "</article>\n";
    public static final String IN_PROGRESS_UPPER =
            "<article>\n"
            + "  <figure>\n"
            + "    <img src=\"http://blogs.fco.gov.uk/nicolearbour/files/2013/07/all-you-need-to-know-about-big-data.jpg\"/>\n"
            + "  </figure>\n"
            + "  <section>\n"
            + "    <p>\n"
            + "      <span class=\"single-line text-x-large\">Processing</span>\n"
            + "      <span class=\"single-line text-x-small\">OraQuick At-Home HIV</span><br/>\n"
            + "      <span class=\"text-minor right\">Started on ";
    
    public static final String IN_PROGRESS_LOWER =
            "      <span class=\"text-x-small\">\n"
            + "    </p>\n"
            + "    <br/>\n"
            + "    <hr/>\n"
            + "    <p class=\"text-small\">\n"
            + "      <em class=\"green\">In Progress</em>\n"
            + "    </p>\n"
            + "  </section>\n"
            + "</article>";
    
    public static final String PROCESSED_OK_UPPER =
            "<article>\n"
            + "  <figure>\n"
            + "    <img src=\"http://www.clker.com/cliparts/e/2/a/d/1206574733930851359Ryan_Taylor_Green_Tick.svg.med.png\"/>\n"
            + "  </figure>\n"
            + "  <section>\n"
            + "    <p>\n"
            + "      <span class=\"single-line text-x-large\">Processed</span>\n"
            + "      <span class=\"single-line text-x-small\">OraQuick At-Home HIV</span><br/>\n"
            + "      <span class=\"text-minor right\">Done on ";
    
    public static final String PROCESSED_OK_LOWER = 
                     "<span class=\"text-x-small\">\n"
            + "    </p>\n"
            + "    <br/>\n"
            + "    <hr/>\n"
            + "    <p class=\"text-small\">\n"
            + "      <em class=\"green\">This test is Negative</em>\n"
            + "    </p>\n"
            + "  </section>\n"
            + "</article>";
}
