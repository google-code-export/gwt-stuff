package org.mcarthur.sandy.gwt.login.client.test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import org.mcarthur.sandy.gwt.login.client.LoginPanel;

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

    public void onModuleLoad() {
        final RootPanel defaultLoginPanelRoot = RootPanel.get("defaultLoginPanel");
        defaultLoginPanelRoot.add(defaultPanel);


        final RootPanel bannerLoginPanel = RootPanel.get("bannerLoginPanel");
        final Image banner = new Image("http://code.google.com/images/code_sm.png");
        bannerPanel.setBanner(banner);
        bannerLoginPanel.add(bannerPanel);

        final RootPanel messageLoginPanel = RootPanel.get("messageLoginPanel");
        messagePanel.setMessage("Please login.");
        messageLoginPanel.add(messagePanel);

    }

}
