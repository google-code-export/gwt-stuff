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

/**
 * Flash 8 Plugin widget.
 * This is mostly the same as the {@link Flash} class but some defaults will be different and if
 * the user has an older version of Flash installed the browser should encourage the user to upgrade.
 *
 * @author Sandy McArthur
 */
public class Flash8 extends Flash {
    /**
     * Identifies the location of the Flash Player ActiveX control so that the browser can automatically download it if it is not already installed.
     * The value must be entered exactly as shown.
     * Applies to the object tag only.
     *
     * @see <a href="http://livedocs.adobe.com/flash/8/main/00000840.html">codebase attribute</a>
     */
    // http://livedocs.adobe.com/flash/8/main/wwhelp/wwhimpl/common/html/wwhelp.htm?context=LiveDocs_Parts&file=00000832.html
    private String codeBase = "http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0";

    public Flash8(final String movie) {
        super(movie);
    }

    public String getCodeBase() {
        return codeBase;
    }

    public void setCodeBase(final String codeBase) {
        this.codeBase = codeBase;
    }
}
