/*
 * Copyright 2006 Sandy McArthur, Jr.
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

package org.mcarthur.sandy.gwt.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * EntryPoint for Testing Login Widgets.
 *
 * <p>
 * This is used with <code>org/mcarthur/sandy/gwt/login/public/TestLoginPanel.html</code> during
 * developement.
 *
 * @author Sandy McArthur
 */
public class TestLoginPanel implements EntryPoint {

    private final LoginPanel.LoginListener loginListener = new LoginPanel.LoginListener() {
        public void onSubmit(final LoginPanel loginPanel) {
            loginPanel.setErrorMessage("Login failed!");
            final Timer t = new Timer() {
                public void run() {
                    loginPanel.reenable();
                }
            };
            t.schedule(1500);
        }
    };
    private final LoginPanel defaultPanel = new LoginPanel(loginListener);
    private final LoginPanel bannerPanel = new LoginPanel(loginListener);
    private final LoginPanel messagePanel = new LoginPanel(loginListener);

    private LoginDialogBox defaultLoginDialogBox;

    public void onModuleLoad() {
        final RootPanel defaultLoginPanelRoot = RootPanel.get("defaultLoginPanel");
        defaultLoginPanelRoot.add(defaultPanel);

        final RootPanel defaultLoginDialogBoxRoot = RootPanel.get("defaultLoginDialogBox");
        defaultLoginDialogBox = new LoginDialogBox(loginListener);
        final Button defaultLoginDialogBoxButton = new Button("Show");
        defaultLoginDialogBoxButton.addClickListener(new ClickListener() {
            public void onClick(final Widget sender) {
                defaultLoginDialogBox.setPopupPosition(100,100);
                defaultLoginDialogBox.show();
                Window.alert("clicked");
            }
        });
        defaultLoginDialogBoxRoot.add(defaultLoginDialogBoxButton);



        final RootPanel bannerLoginPanel = RootPanel.get("bannerLoginPanel");
        final Image banner = new Image("http://code.google.com/images/code_sm.png");
        bannerPanel.setBanner(banner);
        bannerLoginPanel.add(bannerPanel);

        final RootPanel messageLoginPanel = RootPanel.get("messageLoginPanel");
        messagePanel.setMessage("Please login.");
        messageLoginPanel.add(messagePanel);

    }

}
