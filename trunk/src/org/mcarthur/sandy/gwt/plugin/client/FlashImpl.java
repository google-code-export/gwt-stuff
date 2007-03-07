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
 * FlashImpl for most browsers.
 *
 * @author Sandy McArthur
 */
class FlashImpl {

    protected Flash flash;

    protected Element base;

    void setFlash(final Flash flash) {
        this.flash = flash;
    }

    /**
     * Create the browser specific plugin element.
     *
     * @return the browser's plugin element.
     */
    public Element createPluginElement() {
        base = DOM.createElement("embed");
        return base;
    }

    /**
     * Do any browser specific initialization here.
     */
    public void init() {
        updatePluginsPage();
        setAttribute(base, "type", "application/x-shockwave-flash");
    }

    public void updateDeviceFont() {
        setAttribute(base, "devicefont", Boolean.toString(flash.isDeviceFont()));
    }

    public void updateMovie() {
        if (flash.getMovie() != null) {
            setAttribute(base, "src", flash.getMovie());
        } else {
            removeAttribute(base, "src");
        }
    }

    public void updateClassId() {
        // object only
    }

    public void updateCodeBase() {
        // object only
    }
    
    public void updatePluginsPage() {
        if (flash.getPluginsPage() != null) {
            setAttribute(base, "pluginspage", flash.getPluginsPage());
        } else {
            removeAttribute(base, "pluginspage");
        }
    }

    public void updateSwLiveConnect() {
        setAttribute(base, "swliveconnect", Boolean.toString(flash.isSwLiveConnect()));
    }

    public void updatePlay() {
        setAttribute(base, "play", Boolean.toString(flash.isPlay()));
    }

    public void updateLoop() {
        setAttribute(base, "loop", Boolean.toString(flash.isLoop()));
    }

    public void updateQuality() {
        if (flash.getQuality() != null) {
            setAttribute(base, "quality", flash.getQuality());
        } else {
            removeAttribute(base, "quality");
        }
    }

    public void updateBgColor() {
        if (flash.getBgColor() != null) {
            setAttribute(base, "bgcolor", flash.getBgColor());
        } else {
            removeAttribute(base, "bgcolor");
        }
    }

    public void updateScale() {
        if (flash.getScale() != null) {
            setAttribute(base, "scale", flash.getScale());
        } else {
            removeAttribute(base, "scale");
        }
    }

    public void updateAlign() {
        if (flash.getAlign() != null) {
            setAttribute(base, "align", flash.getAlign());
        } else {
            removeAttribute(base, "align");
        }
    }

    public void updateSalign() {
        if (flash.getSalign() != null) {
            setAttribute(base, "salign", flash.getSalign());
        } else {
            removeAttribute(base, "salign");
        }
    }

    public void updateBase() {
        if (flash.getBase() != null) {
            setAttribute(base, "base", flash.getBase());
        } else {
            removeAttribute(base, "base");
        }
    }

    public void updateMenu() {
        setAttribute(base, "menu", Boolean.toString(flash.isMenu()));
    }

    public void updateWmode() {
        if (flash.getWmode() != null) {
            setAttribute(base, "wmode", flash.getWmode());
        } else {
            removeAttribute(base, "wmode");
        }
    }

    public void updateAllowScriptAccess() {
        if (flash.getAllowScriptAccess() != null) {
            setAttribute(base, "allowScriptAccess", flash.getAllowScriptAccess());
        } else {
            removeAttribute(base, "allowScriptAccess");
        }
    }

    public void updateSeamlessTabbing() {
        setAttribute(base, "seamlesstabbing", Boolean.toString(flash.isSeamlessTabbing()));
    }

    public void updateId() {
        if (flash.getId() != null) {
            setAttribute(base, "id", flash.getId());
        } else {
            removeAttribute(base, "id");
        }
    }

    public void updateName() {
        if (flash.getName() != null) {
            setAttribute(base, "name", flash.getName());
        } else {
            removeAttribute(base, "name");
        }
    }
    
    protected static native void setAttribute(Element elem, String attr, String value) /*-{
        elem.setAttribute(attr,value);
    }-*/;

    protected static native void removeAttribute(Element elem, String attr) /*-{
        elem.removeAttribute(attr);
    }-*/;
}
