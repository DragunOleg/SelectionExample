## WHAT
This is simple example of how to use recycler-selection library for very simple case of just selecting list of items in recycler. Select/deselect all. And select with some custom filter.

![selectionExampleVid](https://user-images.githubusercontent.com/8080919/163710871-49197474-8f6c-4a3e-abac-719d5e9f82c3.gif) 

## WHY
All the tutorials are looked complicated and using approach for hard case with action mode, what is unnessessary for humble homie like me (and probably you).

Library is not bad for the cases with drag-and-drop support, action mode and other complicated stuff. BUT if you need just simple list with selection it is really hard to find good understandable example, so i tried to make one.

## MISSUNDERSTANDINGS & UNCLEAR STUFF
What was unclear at the start of googling "recycler-selection" and becomes clear later:

1. No need to handle deselect event.

For selected item library is simply intercepts user click, set element to not selected state and call onBindViewHolder

2. No need to do anything with adapter (like notifyDataSetChanged) in SelectionObserver.

Just work with selected/not selected in onBind inside viewHoler. 
