# ocic2replicator
Adds an IC2 Replicator Driver for OpenComputers

Huge thanks to https://github.com/raymondbh for making the original ocdriversplus because of them I didn't have to spend too long figuring this driver stuff out.

Usage:

Access the driver like usual through `component.driver_name`
In this case it is `component.ic2_replicator` (NOTE:ic2_te_replicator is the default driver supplied by OC that doesn't support changing the patterns)

ocic2replicator provides 7 functions to help you control the ic2:replicator.
```lua
function setPatternNamed(string tileName);
function setPatternIndex(int index);
function setMode(int mode);
function getPatterns();
function getCurrent();
function getUUAmount();
function getPatternIndex(string tileName);
```

NOTE: You cannot force an item to be replicated, it will only be replicated if the nearby pattern storage contains your item.

`function setPatternNamed(string tileName);`
> Sets the current pattern to tileName.

> eg `component.ic2_replicator.setPatternNamed("tile.chest")`
This sets the replicator to replicate minecraft:chest
Note:This will only work if the pattern storage also contains the item ("tile.chest")


`function setPatternIndex(int index);`
> Sets the pattern index to (index)

> This will set the replicator to replicate the item in the pattern storage at index

So say if you have no patterns, you add minecraft:stone to be replicated then you can call `setPatternIndex(0)`
this sets the replicator to replicate the stone item
If you add another item and you call `setPatternIndex(1)` it will set that item to be replicated etc


`function setMode(int mode);`
> This will start the replicator, it has three modes, `STOPPED, SINGLE, CONTINUOUS`, pass (0, 1 or 2) respectively

> Returns the new mode.


`function getPatterns();`
> Returns the patterns in the pattern storage, returns them as a table eg 

In minecraft 1.12.2 the item.damage refers to the metadata, so even though minecraft:stone referes to stone and granite the damage value is what distinguishes them
for stone the dmge is 0 for granite it is 1, this is very IMPORTANT to know because ic2 uses this for all of their machines, all their machines are ic2:te and the only
seperating each machine is the metadata (damage), the replicator block is damage=63 the scanner is damage=64.

`function getCurrent();`
> Returns the current selected pattern item, similar formatting to the elements in getPatterns()

`function getUUAmount();`
> Returns the amount of UU matter liquid stored in the replicator.

`function getPatternIndex(string tileName);`
> Return the index of the tileName item inside the pattern storage

eg if you have rftools:frame in slot 5 of the pattern storage then it will return 4 (because we start at 0 not 1)
