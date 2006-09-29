package org.mcarthur.sandy.gwt.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * EntryPoint for Testing Login Widgets.
 * This entry point does more work than the {@link Login} entry point and should not be used in
 * production.
 *
 * <p>
 * This is used with <code>org/mcarthur/sandy/gwt/login/public/LoginTest.html</code> during
 * developement.
 *
 * @author Sandy McArthur
 */
public class LoginTest implements EntryPoint {

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
