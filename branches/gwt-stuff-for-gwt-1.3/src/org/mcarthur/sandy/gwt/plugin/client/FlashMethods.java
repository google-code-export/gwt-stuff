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
import com.google.gwt.user.client.Element;

/**
 * JSNI between GWT and Flash method calls.
 *
 * @author Sandy McArthur
 */
class FlashMethods implements Flash.Methods {

    private final Flash flash;
    private final Element flashElement;

    FlashMethods(final Flash flash) {
        this.flash = flash;
        flashElement = flash.getElement();
    }

    public native void gotoFrame(int frame) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).GotoFrame(frame);
    }-*/;

    public native boolean isPlaying() /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).IsPlaying();
    }-*/;

    public void pan(final int x, final int y, final PanMode mode) {
        callPan(x,y,mode.getMode());
    }

    private native void callPan(int x, int y, int mode) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).Pan(x, y, mode);
    }-*/;

    public native int percentLoaded() /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).PercentLoaded();
    }-*/;

    public native void play() /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).Play();
    }-*/;

    public native void rewind() /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).Rewind();
    }-*/;

    public native void setZoomRect(int left, int top, int right, int bottom) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).SetZoomRect(left, top, right, bottom);
    }-*/;

    public native void stopPlay() /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).StopPlay();
    }-*/;

    public native int totalFrames() /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TotalFrames();
    }-*/;

    public native void zoom(int zoom) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).Zoom(zoom);
    }-*/;

    public native void loadMovie(int layer, String url) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).LoadMovie(layer, url);
    }-*/;

    public native int tCurrentFrame(String target) /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TCurrentFrame(target);
    }-*/;

    public native String tCurrentLabel(String target) /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TCurrentLabel(target);
    }-*/;

    public native void tGotoFrame(String target, int frame) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TGotoFrame(target, frame);
    }-*/;

    public native void tGotoLabel(String target, String frameLabel) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TGotoLabel(target, frameLabel);
    }-*/;

    public native void tPlay(String target) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TPlay(target);
    }-*/;

    public native void tStopPlay(String target) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TStopPlay(target);
    }-*/;

    public native String getVariable(String varName) /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).GetVariable(varName);
    }-*/;

    public native void setVariable(String varName, String value) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).SetVariable(varName, value);
    }-*/;

    public native void tCallFrame(String target, int frame) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TCallFrame(target, frame);
    }-*/;

    public native void tCallLabel(String target, String frameLabel) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TCallLabel(target, frameLabel);
    }-*/;

    public String tGetProperty(final String target, final Property property) {
        return callTGetProperty(target, property.getPropertyNumber());
    }

    private native String callTGetProperty(String target, int propertyNumber) /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TGetProperty(target, propertyNumber);
    }-*/;

    public int tGetPropertyAsNumber(final String target, final Property property) {
        return callTGetPropertyAsNumber(target, property.getPropertyNumber());
    }

    private native int callTGetPropertyAsNumber(String target, int propertyNumber) /*-{
       return (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TGetPropertyAsNumber(target, propertyNumber);
    }-*/;

    public void tSetProperty(final String target, final Property property, final String value) {
        callTSetProperty(target, property.getPropertyNumber(), value);
    }

    private native void callTSetProperty(String target, int propertyNumber, String value) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TSetProperty(target, propertyNumber, value);
    }-*/;

    public void tSetProperty(final String target, final Property property, final int value) {
        callTSetProperty(target, property.getPropertyNumber(), value);
    }

    private native void callTSetProperty(String target, int propertyNumber, int value) /*-{
       (this.@org.mcarthur.sandy.gwt.plugin.client.FlashMethods::flashElement).TSetProperty(target, propertyNumber, value);
    }-*/;

    /**
     * This is just here to make unused inspections happy.
     */
    protected void discardedByTheCompiler() {
        if (flash == null || flashElement == null) {
            GWT.log("This should never be called", null);
        }
    }
}
