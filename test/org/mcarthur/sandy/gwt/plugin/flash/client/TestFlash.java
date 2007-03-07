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

package org.mcarthur.sandy.gwt.plugin.flash.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.mcarthur.sandy.gwt.plugin.client.Flash;

/**
 * Test Flash plugins.
 *
 * @author Sandy McArthur
 */
public class TestFlash implements EntryPoint {
    private Flash flash = new Flash("test.swf"); //new Flash("flash_to_javascript.swf");
    private FlowPanel fp = new FlowPanel();
    private VerticalPanel log = new VerticalPanel();

    public void onModuleLoad() {
        //flash.setLoop(true);
        //flash.setPlay(false);
        //flash.setSwLiveConnect(true);
        flash.setWidth("160");
        flash.setHeight("120");
        RootPanel.get("flash").add(flash);
        RootPanel.get("log").add(log);

        flash.addFSCommandListener(new Flash.FsCommandListener() {
            public void fsCommand(final String command, final String args) {
                log.insert(new Label("fsCommand: command: " + command + ", args: " + args), 0);
            }
        });

        final Button play = new Button("Play");
        play.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().play();
            }
        });
        fp.add(play);

        final Button rewind = new Button("Rewind");
        rewind.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().rewind();
            }
        });
        fp.add(rewind);

        final Button stop = new Button("Stop");
        stop.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().stopPlay();
            }
        });
        fp.add(stop);

        final Button isPlaying = new Button("isPlaying?");
        isPlaying.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                log.insert(new Label("isPlaying: " + flash.getMethods().isPlaying()), 0);
            }
        });
        fp.add(isPlaying);

        final Button percentLoaded = new Button("Percent Loaded?");
        percentLoaded.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                log.insert(new Label("Percent Loaded: " + flash.getMethods().percentLoaded()), 0);
            }
        });
        fp.add(percentLoaded);

        final Button totalFrames = new Button("Total Frames?");
        totalFrames.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                log.insert(new Label("Total Frames: " + flash.getMethods().totalFrames()), 0);
            }
        });
        fp.add(totalFrames);

        final Button zoomIn = new Button("+");
        zoomIn.setTitle("Zoom In");
        zoomIn.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().zoom(90);
            }
        });
        fp.add(zoomIn);

        final Button zoomOut = new Button("-");
        zoomOut.setTitle("Zoom Out");
        zoomOut.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().zoom(110);
            }
        });
        fp.add(zoomOut);

        final Button resetZoom = new Button("=");
        resetZoom.setTitle("Reset Zoom");
        resetZoom.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().zoom(0);
            }
        });
        fp.add(resetZoom);

        final Button panL = new Button("&lt;");
        panL.setTitle("Pan Left");
        panL.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().pan(-25, 0, Flash.Methods.PanMode.PIXEL);
            }
        });
        fp.add(panL);

        final Button panU = new Button("/\\");
        panU.setTitle("Pan Up");
        panU.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().pan(0, -25, Flash.Methods.PanMode.PIXEL);
            }
        });
        fp.add(panU);

        final Button panR = new Button("&gt;");
        panR.setTitle("Pan Right");
        panR.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().pan(25, 0, Flash.Methods.PanMode.PIXEL);
            }
        });
        fp.add(panR);

        final Button panD = new Button("\\/");
        panD.setTitle("Pan Down");
        panD.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().pan(0, 25, Flash.Methods.PanMode.PIXEL);
            }
        });
        fp.add(panD);

        final HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Label("Frame: "));
        final TextBox frame = new TextBox();
        hp.add(frame);
        final Button frameGo = new Button("Go");
        frameGo.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                flash.getMethods().gotoFrame(Integer.parseInt(frame.getText()));
            }
        });
        hp.add(frameGo);
        fp.add(hp);

        RootPanel.get("buttons").add(fp);
    }
}
