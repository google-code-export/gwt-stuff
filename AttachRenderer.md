# Introduction #

The AttachRenderer interface provides callbacks when table row groups are attached or detached to the browser's document in the ObjectListTable widget. You should use this if you need access to the row groups after the have been rendered.

Stuff you should do in a AttachRenderer:
  * Add and remove mouse listeners else you'll likely leak memory.
  * Update the state of the widgets in the table cells to reflect changes in the model that may have happened when the row group was detached.