# Introduction #

Hopefully we've made using GWT-Stuff is easy as possible.

# Download #

First you will need to [download](http://code.google.com/p/gwt-stuff/downloads/list?can=3) the GWT-Stuff-`YYYYMMDD`.jar you want to use. For simplicity in this GettingStarted guide we'll assume you downloaded the latest jar that contains recent updates and we'll refer to it as _GWT-Stuff.jar_ .

# Including GWT-Stuff in your GWT Project #

You need to do two things to add GWT-Stuff modules to your project. First is add the
GWT-Stuff.jar to your classpath and second is to inherit the required GWT Module.

## Updating Classpath ##

Once you've downloaded the needed jar(s) you need to add them to the classpath for your
project. This will ultimately depend on how you build your project but if you are using
the CommandLineTools provided with GWT then you'll need to edit the _Module-shell_ and
_Module-compile_ scripts. Open each of them up and find the classpath (-cp) option and
append ":lib/GWT-Stuff.jar" to the end. (On windows it will be ";lib/GWT-Stuff.jar") If
you're not sure where that is look for ".jar" and add it after the lass occurrence of ".jar".

## GWT Module Imports ##

See the various modules pages for how to import them into your GWT Module.
  * GwtStuffEventList
  * GwtStuffLogin
  * GwtStuffPropertyChange
  * GwtStuffTable

# Using GWT-Stuff Modules #

The best way to get started using a GWT-Stuff module is to take a look at the Javadocs
linked from the [project page](http://code.google.com/p/gwt-stuff/). Each package page of
the Javadocs will include a short example near the bottom to help get you started.

# License #

All code released by the GWT-Stuff project is released under the Apache License, Version 2.0