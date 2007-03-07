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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * IE6 version of FlashImpl.
 *
 * @author Sandy McArthur
 */
class FlashImplIE6 extends FlashImpl {

    public Element createPluginElement() {
        base = DOM.createElement("object");
        return base;
    }

    /**
     * Do any browser specific initialization here.
     */
    public void init() {
        updateClassId();
        updateCodeBase();
    }

    private Element deviceFontParam;
    public void updateDeviceFont() {
        if (deviceFontParam == null) {
            deviceFontParam = createParam("devicefont");
            DOM.appendChild(base, deviceFontParam);
        }
        setAttribute(deviceFontParam, "value", Boolean.toString(flash.isDeviceFont()));
    }

    private Element movieParam;
    public void updateMovie() {
        final String value = flash.getMovie();
        if (movieParam == null && value != null) {
            movieParam = createParam("movie");
            DOM.appendChild(base, movieParam);
        } else if (movieParam != null && value == null) {
            DOM.removeChild(base, movieParam);
            movieParam = null;
        }
        if (value != null) {
            setAttribute(movieParam, "value", value);
        }
    }

    public void updateClassId() {
        setAttribute(base, "classid", flash.getClassId());
    }

    public void updateCodeBase() {
        setAttribute(base, "codebase", flash.getCodeBase());
    }

    public void updatePluginsPage() {
        // embed only
    }

    public void updateSwLiveConnect() {
        // embed only
    }

    private Element playParam;
    public void updatePlay() {
        if (playParam == null) {
            playParam = createParam("play");
            DOM.appendChild(base, playParam);
        }
        setAttribute(playParam, "value", Boolean.toString(flash.isPlay()));
    }

    private Element loopParam;
    public void updateLoop() {
        if (loopParam == null) {
            loopParam = createParam("loop");
            DOM.appendChild(base, loopParam);
        }
        setAttribute(loopParam, "value", Boolean.toString(flash.isLoop()));
    }

    private Element qualityParam;
    public void updateQuality() {
        final String value = flash.getQuality();
        if (qualityParam == null && value != null) {
            qualityParam = createParam("quality");
            DOM.appendChild(base, qualityParam);
        } else if (qualityParam != null && value == null) {
            DOM.removeChild(base, qualityParam);
            qualityParam = null;
        }
        if (value != null) {
            setAttribute(qualityParam, "value", value);
        }
    }

    private Element bgColorParam;
    public void updateBgColor() {
        final String value = flash.getBgColor();
        if (bgColorParam == null && value != null) {
            bgColorParam = createParam("bgcolor");
            DOM.appendChild(base, bgColorParam);
        } else if (bgColorParam != null && value == null) {
            DOM.removeChild(base, bgColorParam);
            bgColorParam = null;
        }
        if (value != null) {
            setAttribute(bgColorParam, "value", value);
        }
    }

    private Element scaleParam;
    public void updateScale() {
        final String value = flash.getScale();
        if (scaleParam == null && value != null) {
            scaleParam = createParam("scale");
            DOM.appendChild(base, scaleParam);
        } else if (scaleParam != null && value == null) {
            DOM.removeChild(base, scaleParam);
            scaleParam = null;
        }
        if (value != null) {
            setAttribute(scaleParam, "value", value);
        }
    }

    private Element salignParam;
    public void updateSalign() {
        final String value = flash.getSalign();
        if (salignParam == null && value != null) {
            salignParam = createParam("salign");
            DOM.appendChild(base, salignParam);
        } else if (salignParam != null && value == null) {
            DOM.removeChild(base, salignParam);
            salignParam = null;
        }
        if (value != null) {
            setAttribute(salignParam, "value", value);
        }
    }

    private Element baseParam;
    public void updateBase() {
        final String value = flash.getBase();
        if (baseParam == null && value != null) {
            baseParam = createParam("base");
            DOM.appendChild(base, baseParam);
        } else if (baseParam != null && value == null) {
            DOM.removeChild(base, baseParam);
            baseParam = null;
        }
        if (value != null) {
            setAttribute(baseParam, "value", value);
        }
    }

    private Element menuParam;
    public void updateMenu() {
        if (menuParam == null) {
            menuParam = createParam("menu");
            DOM.appendChild(base, menuParam);
        }
        setAttribute(menuParam, "value", Boolean.toString(flash.isMenu()));
    }

    private Element wmodeParam;
    public void updateWmode() {
        final String value = flash.getWmode();
        if (wmodeParam == null && value != null) {
            wmodeParam = createParam("wmode");
            DOM.appendChild(base, wmodeParam);
        } else if (wmodeParam != null && value == null) {
            DOM.removeChild(base, wmodeParam);
            wmodeParam = null;
        }
        if (value != null) {
            setAttribute(wmodeParam, "value", value);
        }
    }

    private Element allowScriptAccessParam;
    public void updateAllowScriptAccess() {
        final String value = flash.getAllowScriptAccess();
        if (allowScriptAccessParam == null && value != null) {
            allowScriptAccessParam = createParam("allowScriptAccess");
            DOM.appendChild(base, allowScriptAccessParam);
        } else if (allowScriptAccessParam != null && value == null) {
            DOM.removeChild(base, allowScriptAccessParam);
            allowScriptAccessParam = null;
        }
        if (value != null) {
            setAttribute(allowScriptAccessParam, "value", value);
        }
    }

    private Element seamlessTabbingParam;
    public void updateSeamlessTabbing() {
        if (seamlessTabbingParam == null) {
            seamlessTabbingParam = createParam("SeamlessTabbing");
            DOM.appendChild(base, seamlessTabbingParam);
        }
        setAttribute(seamlessTabbingParam, "value", Boolean.toString(flash.isSeamlessTabbing()));
    }

    private static Element createParam(final String name) {
        assert name != null;
        final Element param = DOM.createElement("param");
        setAttribute(param, "name", name);
        return param;
    }
}
