
GWT-Stuff - a collection of Google Web Toolkit components

Everything in this package is released under the Apache 2 license.

Building:

You can build the whole project from source by simply running ant from the
project base directory. This assumes you have GWT installed in the same
location I do. If you have GWT installed somewhere else then odds are
the build fails with an error about "package com.google.... does not exists"
then you'll need to tell ant where to find the GWT base dir. eg:
$ ant -Dgwt.basedir=c:\gwt-0.0.0\
