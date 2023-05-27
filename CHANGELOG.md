# Gamma Shifter 1.0.0-beta.2

### Additions
-Added Auto Night Mode<br>
-Added `Auto Night Mode` category to configuration menu <br>
-Added option to toggle enforcement of Gamma Shifter's max/min values (0% - 1000%) <br>

### Changes
-Refactored `InitGammaHelper` to behave statically and changed class name to `GammaInitializer` <br>
-"Saved options" log message now only appears when closing the game. <br>

### Fixes
-Fixed bug where buffered inputs for `showGammaKey` were not flushed <br>

### Notes
-Auto Night Mode: <br>
&ensp;&ensp;&ensp;&ensp; -Increases/decreases gamma automatically at dusk/dawn <br>
&ensp;&ensp;&ensp;&ensp; -Defaults to 250% <br>
&ensp;&ensp;&ensp;&ensp; -Stores separate value than used in manual toggling <br>
&ensp;&ensp;&ensp;&ensp; -Can be enabled/disabled and customized through ModMenu <br>
&ensp;&ensp;&ensp;&ensp; -Disabled by default <br>