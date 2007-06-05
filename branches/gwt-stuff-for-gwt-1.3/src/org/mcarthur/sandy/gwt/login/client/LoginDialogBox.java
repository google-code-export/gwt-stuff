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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;

/**
 * TODO: Write JavaDoc
 *
 * @author Sandy McArthur
 */
public class LoginDialogBox extends DialogBox {
    //private final DialogBox loginDialogBox = new DialogBox();
    private final LoginPanel.LoginListener loginListener;
    private final LoginPanel loginPanel;

    public LoginDialogBox(final LoginPanel.LoginListener loginListener) {
        super();
        this.loginListener = loginListener;
        //initWidget(loginDialogBox);
        loginPanel = new LoginPanel(loginListener);
        //loginDialogBox.setText("Foo");
        setText("Login");
        //loginDialogBox.setWidget(loginPanel);
        setWidget(loginPanel);
        //setWidget(new Label("burp"));
    }
}
