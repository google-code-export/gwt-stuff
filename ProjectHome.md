GWT-Stuff is a collection of modules and components for the [Google Web Toolkit](http://code.google.com/webtoolkit/).

# June 10, 2007: #
You need to download the appropiate GWT-Stuff for your version of GWT. The GWT 1.4.10RC indtroduced some changes that necessitated some changes to the internals of the GWT-Stuff widgets. You are encouraged to use the following links to find the appropriate download:
  * [GWT-Stuff for GWT-1.3](http://code.google.com/p/gwt-stuff/downloads/list?q=label:GWT-1.3)
  * [GWT-Stuff for GWT-1.4](http://code.google.com/p/gwt-stuff/downloads/list?q=label:GWT-1.4)

# March 7, 2007: #
Significant release: the major highlight is ObjectListTable no longer crashes some versions of IE6. The IE6 workaround necessitated some incompatible API changes. These changes shouldn't affect many people but hey may require you to refactor the way you do things such as using the AttachRenderer if your table is very dynamic. ObjectListTable got a new feature in the ColSpecRenderer, this allows you to better control the column widths for more advanced table layouts. The ColSpecRenderer and related classes are still young and may change in incompatible ways if you choose to use them.

Other changes include smaller bug fixes to both the EventList module and the Table module. Finally many helpful assert statements have been added to provide additional info as to why something may not behaving as expected. Be sure to add '-ea' to your java launch command to enable assertions in hosted mode. Doing this will save you time in the future.

# February 27, 2007 #
The ObjectListTable widget recently got a significant update including some bug fixes and performance improvements. The EventList library has received some significant work and now has a more complete API. Expect some activity on the EventList module as the API is matured and stabilized.