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

import com.google.gwt.i18n.client.Messages;

/**
 * Interface for I18N of the Login Panel's messages.
 *
 * @author Sandy McArthur
 */
public interface LoginMessages extends Messages {
    public String usernamePrompt();
    public String passwordPrompt();
    public String loginButton();
}
