# Introduction #

ObjectListTable is the first table widget implementation provided by the GWT-Stuff project. Odds are it will be the foundation for future table widgets from GWT-Stuff.

# Details #

ObjectListTable inverts the useage model you may expect from GWT's HTMLTable Widgets (Grid, FlexTable). With the HTMLTables you call methods on the table widget to poke values into the table. With the ObjectListTable you provide it with an EventList of Objects and a Renderer to convert those Objects into table rows. The ObjectListTable then monitors the EventList for changes and calls out to the Renderer as needed to convert new Objects into rows for the table.

This design is more restrictive because it limits you to the very common useage of a vertical list of data.