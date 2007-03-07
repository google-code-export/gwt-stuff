/*
 * Copyright 2007 Sandy McArthur, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mcarthur.sandy.gwt.plugin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Flash Plugin widget.
 *
 * <p>
 * While it may be possible to change the exposed properties after they've been set, what exactly
 * will happen is undefined. Be sure to set everything you want to set before adding this Widget to
 * a panel. If you need to control the Flash movie after it's started playing use
 * {@link #getMethods()}.
 * </p>
 *
 * <p>
 * The defaults in this implementation will more or less track the "current" defaults and try not to
 * impose any specific flash version. If you want to target a specific flash version minimum then
 * consider using one of the subclasses of this class.
 * </p>
 *
 * @author Sandy McArthur
 * @see <a href="http://www.adobe.com/cfusion/knowledgebase/index.cfm?id=tn_12701">Flash OBJECT and EMBED tag attributes</a>
 */
public class Flash extends Widget {
    // TODO: http://www.adobe.com/support/flash/publishexport/scriptingwithflash/

    private FlashImpl impl = (FlashImpl)GWT.create(FlashImpl.class);

    /**
     * (Optional) Specifies whether static text objects that the Device Font option has not been selected for will be drawn using device fonts anyway, if the necessary fonts are available from the operating system.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000834.html">devicefont attribute/parameter</a>
     */
    private boolean deviceFont = false;

    /**
     * Specifies the name of the SWF file to be loaded. Applies to the object tag only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000835.html">src attribute</a>
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000836.html">movie parameter</a>
     */
    private String movie;
    
    /**
     * Identifies the ActiveX control for the browser.
     * The value must be entered exactly as shown.
     * Applies to the object tag only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000837.html">classid attribute</a>
     */
    private final String classId = "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000";

    /**
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000838.html">width attribute</a>
     */
    private String width;

    /**
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000839.html">height attribute</a>
     */
    private String height;

    /**
     * Identifies the location of the Flash Player ActiveX control so that the browser can automatically download it if it is not already installed.
     * The value must be entered exactly as shown.
     * Applies to the object tag only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000840.html">codebase attribute</a>
     */
    // http://livedocs.adobe.com/flash/8/main/wwhelp/wwhimpl/common/html/wwhelp.htm?context=LiveDocs_Parts&file=00000832.html
    private String codeBase = "http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab";

    /**
     * Identifies the location of the Flash Player plug-in so that the user can download it if it is not already installed.
     * The value must be entered exactly as shown.
     * Applies to the embed tag only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000841.html">pluginspage attribute</a>
     */
    private String pluginsPage = "http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash";

    /**
     * Specifies whether the browser should start Java when loading Flash Player for the first time.
     * The default value is false if this attribute is omitted.
     * If you use JavaScript and Flash on the same page, Java must be running for the fscommand() function to work.
     * However, if you use JavaScript only for browser detection or another purpose unrelated to fscommand()actions, you can prevent Java from starting by setting SWLIVECONNECT to false.
     * You can also force Java to start when you are not using JavaScript with Flash by explicitly setting the SWLIVECONNECT attribute to true.
     * Starting Java substantially increases the time it takes to start a SWF file; set this tag to true only when necessary.
     * Applies to the embed tag only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000842.html">swliveconnect attribute</a>
     */
    private boolean swLiveConnect = false;

    /**
     * Specifies whether the application begins playing immediately on loading in the web browser.
     * If your Flash application is interactive, you might want to let the user initiate play by clicking a button or performing another task.
     * In this case, set the play attribute to false to prevent the application from starting automatically.
     * The default value is true if this attribute is omitted.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000843.html">play attribute/parameter</a>
     */
    private boolean play = true;

    /**
     * Specifies whether the Flash content repeats indefinitely or stops when it reaches the last frame.
     * The default value is true if this attribute is omitted.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000844.html">loop attribute/parameter</a>
     */
    private boolean loop = true;

    /**
     * (Optional) Specifies the level of anti-aliasing to be used when your application plays. Because anti-aliasing requires a faster processor to smooth each frame of the SWF file before it is rendered on the viewer's screen, select one of the following values based on whether your priority is speed or appearance:
     * <ul>
     * <li>Low favors playback speed over appearance and never uses anti-aliasing.</li>
     * <li>Autolow emphasizes speed at first but improves appearance whenever possible. Playback begins with anti-aliasing turned off. If Flash Player detects that the processor can handle it, anti-aliasing is turned on.</li>
     * <li>Autohigh emphasizes playback speed and appearance equally at first but sacrifices appearance for playback speed if necessary. Playback begins with anti-aliasing turned on. If the frame rate drops below the specified frame rate, anti-aliasing is turned off to improve playback speed. Use this setting to emulate the Antialias command in Flash (View > Preview Mode > Antialias).</li>
     * <li>Medium applies some anti-aliasing and does not smooth bitmaps. It produces a better quality than the Low setting but a lower quality than the High setting.</li>
     * <li>High favors appearance over playback speed and always applies anti-aliasing. If the SWF file does not contain animation, bitmaps are smoothed; if the SWF file has animation, bitmaps are not smoothed.</li>
     * <li>Best provides the best display quality and does not consider playback speed. All output is anti-aliased, and all bitmaps are smoothed.</li>
     * </ul>
     * The default value for quality is high if this attribute is omitted.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000845.html">quality attribute/parameter</a>
     */
    private String quality = "high"; // low | medium | high | autolow | autohigh | best

    /**
     * (Optional) Specifies the background color of the application.
     * Use this attribute to override the background color setting specified in the Flash SWF file.
     * This attribute does not affect the background color of the HTML page.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000846.html">bgcolor attribute/parameter</a>
     */
    private String bgColor;

    /**
     * (Optional) Defines how the application is placed within the browser window when width and height values are percentages.
     * <ul>
     * <li>Showall (Default) makes the entire Flash content visible in the specified area without distortion while maintaining the original aspect ratio of the application. Borders can appear on two sides of the application.</li>
     * <li>Noborder scales the Flash content to fill the specified area, without distortion but possibly with some cropping, while maintaining the original aspect ratio of the application.</li>
     * <li>Exactfit makes the entire Flash content visible in the specified area without trying to preserve the original aspect ratio. Distortion can occur.</li>
     * </ul>
     * The default value is showall if this attribute is omitted (and width and height values are percentages).
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000847.html">scale attribute/parameter</a>
     */
    private String scale; // showall | noborder | exactfit

    /**
     * Specifies the align value for the object, embed, and img tags and determines how the Flash SWF file is positioned within the browser window.
     * <p>
     * Default centers the application in the browser window and crops edges if the browser window is smaller than the application.
     * </p>
     * <p>
     * L, R, T, and B align the application along the left, right, top, and bottom edge, respectively, of the browser window and crop the remaining three sides as needed.
     * </p>
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000848.html">align attribute</a>
     */
    private String align; // Default | L | R | T | B

    /**
     * (Optional) Specifies where a scaled Flash SWF file is positioned within the area defined by the width and height settings.
     * For more information about these conditions, see scale attribute/parameter.
     * <p>
     * L, R, T, and B align the application along the left, right, top or bottom edge, respectively, of the browser window and crop the remaining three sides as needed.
     * </p>
     * <p>
     * TL and TR align the application to the top left and top right corner, respectively, of the browser window and crop the bottom and remaining right or left side as needed.
     * </p>
     * <p>
     * BL and BR align the application to the bottom left and bottom right corner, respectively, of the browser window and crop the top and remaining right or left side as needed.
     * </p>
     * <p>
     * If this attribute is omitted, the Flash content is centered in the browser window.
     * </p>
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000849.html">salign parameter</a>
     */
    private String salign; // L | R | T | B | TL | TR | BL | BR

    /**
     * (Optional) Specifies the base directory or URL used to resolve all relative path statements in the Flash SWF file. This attribute is helpful when your SWF files are kept in a different folder from your other files.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000850.html">base attribute</a>
     */
    private String base;

    /**
     * (Optional) Specifies what type of menu appears when the viewer right-clicks (Windows) or Command-clicks (Macintosh) the application area in the browser.
     * <ul>
     * <li>true shows the full menu, which gives the user several options to enhance or control playback.</li>
     * <li>false shows a menu that contains only the About Macromedia Flash Player 6 option and the Settings option.</li>
     * </ul>
     * The default value is true if this attribute is omitted.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000851.html">menu attribute/parameter</a>
     */
    private boolean menu = true;

    /**
     * (Optional) Lets you use the transparent Flash content, absolute positioning, and layering capabilities available in Internet Explorer 4.0. This attribute/parameter works only in Windows with the Flash Player ActiveX control.
     * <ul>
     * <li>Window plays the application in its own rectangular window on a web page. Window indicates that the Flash application has no interaction with HTML layers and is always the topmost item.</li>
     * <li>Opaque makes the application hide everything behind it on the page.</li>
     * <li>Transparent makes the background of the HTML page show through all the transparent portions of the application and can slow animation performance.</li>
     * <li>Opaque windowless and Transparent windowless both interact with HTML layers, letting layers above the SWF file block out the application. The difference between the two is that Transparent allows transparency so that HTML layers below the SWF file might show through if a section of the SWF file has transparency; opaque does not.</li>
     * </ul>
     * The default value is Window if this attribute is omitted. Applies to object only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000852.html">wmode attribute/parameter</a>
     */
    private String wmode; // Window | Opaque | Transparent

    /**
     * Use allowscriptaccess to let your Flash application communicate with the HTML page hosting it. This is required because fscommand() and getURL() operations can cause JavaScript to use the permissions of the HTML page, which can be different from the permissions of your Flash application. This has important implications for cross-domain security.
     * <ul>
     * <li>always permits scripting operations at all times.</li>
     * <li>never forbids all scripting operations.</li>
     * <li>samedomain permits scripting operations only if the Flash application is from the same domain as the HTML page.</li>
     * </ul>
     * The default value used by all HTML publish templates is samedomain.
     * 
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000853.html">allowscriptaccess attribute/parameter</a>
     */
    private String allowScriptAccess; // always | never | samedomain

    /**
     * (Optional) Lets you set the ActiveX control to perform seamless tabbing, so that the user can tab out of a Flash application. This parameter works only in Windows with the Flash Player ActiveX control, version 7 and higher.
     * <ul>
     * <li>true (or omitted) sets the ActiveX control will perform seamless tabbing: after users tab through the Flash application, the next tab keypress will move the focus out of the Flash application and into the surrounding HTML content or to the browser status bar if there is nothing that can have focus in the HTML following the Flash application.</li>
     * <li>false sets the ActiveX control to behave as it did in version 6 and earlier: After users tab through the Flash application, the next tab keypress will wrap the focus around to the beginning of the Flash application. In this mode, the focus cannot be advanced past the Flash application by using the tab key.</li>
     * </ul>
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000854.html">SeamlessTabbing parameter</a>
     */
    private boolean seamlessTabbing = true;

    /**
     * XXX: Flash 9 feature.
     * @see <a href="http://www.adobe.com/devnet/flashplayer/articles/full_screen_mode.html">Exploring full-screen mode in Flash Player 9</a>
     */
    private boolean allowFullScreen = false;


    /**
     * @see <a href="http://www.adobe.com/cfusion/knowledgebase/index.cfm?id=tn_16417">FlashVars TechNote</a>
     */
    private Map/*<String,String>*/ flashVars;

    /**
     * Identifies the Flash movie to the host environment (a web browser, for example) so that it can be referenced using a scripting language. OBJECT-specific.
     */
    private String id;

    /**
     * Identifies the Flash movie to the host environment (a web browser, typically) so that it can be referenced using a scripting language such as JavaScript or VBScript. EMBED-specific.
     */
    private String name;

    private Methods methods;

    private int instances = 0;

    public Flash(final String movie) {
        impl.setFlash(this);
        setElement(impl.createPluginElement());
        impl.init();
        setMovie(movie);

        // likely a uniq name
        setName("gwtStuffFlash" + instances++);
    }

    public boolean isDeviceFont() {
        return deviceFont;
    }

    public void setDeviceFont(final boolean deviceFont) {
        this.deviceFont = deviceFont;
        impl.updateDeviceFont();
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
        impl.updateMovie();
    }

    public String getClassId() {
        return classId;
    }

    public String getCodeBase() {
        return codeBase;
    }

    public void setCodeBase(final String codeBase) {
        this.codeBase = codeBase;
        impl.updateCodeBase();
    }

    public String getPluginsPage() {
        return pluginsPage;
    }

    public void setPluginsPage(final String pluginsPage) {
        this.pluginsPage = pluginsPage;
        impl.updatePluginsPage();
    }

    public boolean isSwLiveConnect() {
        return swLiveConnect;
    }

    public void setSwLiveConnect(final boolean swLiveConnect) {
        this.swLiveConnect = swLiveConnect;
        impl.updateSwLiveConnect();
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(final boolean play) {
        this.play = play;
        impl.updatePlay();
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(final boolean loop) {
        this.loop = loop;
        impl.updateLoop();
    }

    public String getQuality() {
        return quality;
    }

    private static List qualities;

    public void setQuality(final String quality) {
        if (!GWT.isScript()) {
            if (qualities == null) {
                qualities = Arrays.asList(new String[] {null, "low", "medium", "high", "autolow", "autohigh", "best"});
            }
            if (quality != null && !qualities.contains(quality)) {
                GWT.log("warning: quality is not found in " + qualities, new IllegalArgumentException(quality));
            }
        }
        this.quality = quality;
        impl.updateQuality();
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(final String bgColor) {
        this.bgColor = bgColor;
        impl.updateBgColor();
    }

    public String getScale() {
        return scale;
    }

    private static List scales;

    public void setScale(final String scale) {
        if (!GWT.isScript()) {
            if (scales == null) {
                scales = Arrays.asList(new String[] {null, "showall", "noborder", "exactfit"});
            }
            if (scale != null && !scales.contains(scale)) {
                GWT.log("warning: scale is not found in " + scales, new IllegalArgumentException(scale));
            }
        }
        this.scale = scale;
        impl.updateScale();
    }

    public String getAlign() {
        return align;
    }

    private static List aligns;

    public void setAlign(final String align) {
        if (!GWT.isScript()) {
            if (aligns == null) {
                aligns = Arrays.asList(new String[] {null, "default", "left", "l", "right", "r", "top", "t", "bottom", "b"});
            }
            if (!aligns.contains(align)) {
                GWT.log("warning: align is not found in " + aligns, new IllegalArgumentException(align));
            }
        }
        this.align = align;
        impl.updateAlign();
    }

    public String getSalign() {
        return salign;
    }

    private static List saligns;

    public void setSalign(final String salign) {
        if (!GWT.isScript()) {
            if (saligns == null) {
                saligns = Arrays.asList(new String[] {null, "l", "r", "t", "b", "tl", "tr", "bl", "br"});
            }
            if (!saligns.contains(salign)) {
                GWT.log("warning: salign is not found in " + saligns, new IllegalArgumentException(salign));
            }
        }
        this.salign = salign;
        impl.updateSalign();
    }

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
        impl.updateBase();
    }

    public boolean isMenu() {
        return menu;
    }

    public void setMenu(final boolean menu) {
        this.menu = menu;
        impl.updateMenu();
    }

    public String getWmode() {
        return wmode;
    }

    private static List wmodes;

    public void setWmode(final String wmode) {
        if (!GWT.isScript()) {
            if (wmodes == null) {
                wmodes = Arrays.asList(new String[] {null, "window", "opaque", "transparent"});
            }
            if (!wmodes.contains(wmode)) {
                GWT.log("warning: wmode is not found in " + wmodes, new IllegalArgumentException(wmode));
            }
        }

        this.wmode = wmode;
        impl.updateWmode();
    }

    public String getAllowScriptAccess() {
        return allowScriptAccess;
    }

    private static List allowScriptAccesses;

    public void setAllowScriptAccess(final String allowScriptAccess) {
        if (!GWT.isScript()) {
            if (allowScriptAccesses == null) {
                allowScriptAccesses = Arrays.asList(new String[] {null, "always", "never", "samedomain"});
            }
            if (!allowScriptAccesses.contains(allowScriptAccess)) {
                GWT.log("warning: allowScriptAccess is not found in " + allowScriptAccesses, new IllegalArgumentException(allowScriptAccess));
            }
        }
        this.allowScriptAccess = allowScriptAccess;
        impl.updateAllowScriptAccess();
    }

    public boolean isSeamlessTabbing() {
        return seamlessTabbing;
    }

    public void setSeamlessTabbing(final boolean seamlessTabbing) {
        this.seamlessTabbing = seamlessTabbing;
        impl.updateSeamlessTabbing();
    }

    // TODO FlashVars

    String getId() {
        return id;
    }

    /**
     * Sets the element's id.
     * @param id the flash plugin's DOM id.
     */
    private void setId(final String id) {
        this.id = id;
        impl.updateId();
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the element's name.
     * @param name the flash plugin's name.
     */
    public void setName(final String name) {
        this.name = name;
        impl.updateName();
        setId(name);
    }

    public Methods getMethods() {
        if (methods == null) {
            methods = new FlashMethods(this);
        }
        return methods;
    }

    /**
     * Flash methods that can be called to control the Flash movie from GWT.
     * For all of these methods to work the user must have Flash 5 or above.
     * Programmers are not expected to implement this interface.
     * @see org.mcarthur.sandy.gwt.plugin.client.Flash#getMethods()
     * @see <a href="http://www.adobe.com/support/flash/publishexport/scriptingwithflash/scriptingwithflash_03.html">Flash Methods</a>
     */
    public interface Methods {
        public void gotoFrame(int frame);
        public boolean isPlaying();
        public static class PanMode {
            public static final PanMode PIXELS = new PanMode(0);
            public static final PanMode PERCENT = new PanMode(1);

            private final int mode;

            private PanMode(final int mode) {
                this.mode = mode;
            }

            int getMode() {
                return mode;
            }
        }
        public void pan(int x, int y, PanMode mode);
        public int percentLoaded();
        public void play();
        public void rewind();
        public void setZoomRect(int left, int top, int right, int bottom);
        public void stopPlay();
        public int totalFrames();
        public void zoom(int zoom);

        public void loadMovie(int layer, String url);
        public int tCurrentFrame(String target);
        public String tCurrentLabel(String target);
        public void tGotoFrame(String target, int frame);
        public void tGotoLabel(String target, String label);
        public void tPlay(String target);
        public void tStopPlay(String target);

        public String getVariable(String varName);
        public void setVariable(String varName, String value);
        public void tCallFrame(String target, int frame);
        public void tCallLabel(String target, String label);

        /**
         * @see <a href="http://www.adobe.com/support/flash/publishexport/scriptingwithflash/scriptingwithflash_04.html#69558">Getting and setting properties</a>
         */
        public static class Property {
            public static final Property X_POS = new Property(0);
            public static final Property Y_POS = new Property(1);
            public static final Property X_SCALE = new Property(2);
            public static final Property Y_SCALE = new Property(3);
            public static final Property CURRENT_FRAME = new Property(4);
            public static final Property TOTAL_FRAMES = new Property(5);
            public static final Property ALPHA = new Property(6);
            public static final Property VISIBLE = new Property(7);
            public static final Property WIDTH = new Property(8);
            public static final Property HEIGHT = new Property(9);
            public static final Property ROTATE = new Property(10);
            public static final Property TARGET = new Property(11);
            public static final Property FRAMES_LOADED = new Property(12);
            public static final Property NAME = new Property(13);
            public static final Property DROP_TARGET = new Property(14);
            public static final Property URL = new Property(15);
            public static final Property HIGH_QUALITY = new Property(16);
            public static final Property FOCUS_RECT = new Property(17);
            public static final Property SOUND_BUF_TIME = new Property(18);
            private final int propertyNumber;

            private Property(final int propertyNumber) {
                this.propertyNumber = propertyNumber;
            }

            int getPropertyNumber() {
                return propertyNumber;
            }
        }
        public String tGetProperty(String target, Property property);
        public int tGetPropertyAsNumber(String target, Property property);
        public void tSetProperty(String target, Property property, String value);
        public void tSetProperty(String target, Property property, int value);
    }

    private List/*<EventsListener>*/ listeners;
    private boolean callbackRegistered = false;
    public void addFSCommandListener(final FsCommandListener listener) {
        if (!callbackRegistered) {
            listeners = new ArrayList();
            registerCallbacks(this, getName());
        }
        listeners.add(listener);
    }
    private void doFSCommand(final String command, final String args) {
        if (listeners != null) {
            final Object[] listenersArray = listeners.toArray();
            for (int i=0; i < listenersArray.length; i++) {
                final FsCommandListener fsCommandListener = (FsCommandListener)listenersArray[i];
                fsCommandListener.fsCommand(command, args);
            }
        }
    }
    private native void registerCallbacks(Flash instance, String name) /*-{
        $wnd[name+'_DoFSCommand'] = function (command, args) {
            instance.@org.mcarthur.sandy.gwt.plugin.client.Flash::doFSCommand(Ljava/lang/String;Ljava/lang/String;)(command, args);
        };
    }-*/;
    /**
     * @see <a href="http://www.adobe.com/support/flash/publishexport/scriptingwithflash/scriptingwithflash_03.html">Flash Events</a>
     */
    public interface FsCommandListener {
        public void fsCommand(String command, String args);
    }
}
