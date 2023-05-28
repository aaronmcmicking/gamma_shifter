# Gamma Shifter 1.0.0-beta.3

### Additions
-Added Auto Night Mode<br>
-Added `Auto Night Mode` category to configuration menu <br>
-Added option to toggle enforcement of Gamma Shifter's max/min values (0% - 1000%) <br>

### Changes
-Renamed `Brightness` option to `Custom Brightness` and updated tooltip <br>
-Changed default custom brightness to 1000% <br>
-Config menu now references "gamma" instead of "brightness" <br>
-Gamma overlay now shows when mod effects are disabled
-"Saved options" log message now only appears when closing the game <br>
-Changed order of `HUD location` and `Show gamma message on change` in config menu <br>
-Refactored `InitGammaHelper` to behave statically and changed class name to `GammaInitializer` <br>

### Fixes
-Fixed bug where buffered inputs for `showGammaKey` were not flushed <br>

### Notes
-Auto Night Mode: <br>
&ensp;&ensp;&ensp;&ensp; -Increases/decreases gamma automatically at dusk/dawn <br>
&ensp;&ensp;&ensp;&ensp; -Defaults to 300% <br>
&ensp;&ensp;&ensp;&ensp; -Stores separate value than used in manual toggling <br>
&ensp;&ensp;&ensp;&ensp; -Can be enabled/disabled and customized through ModMenu <br>
&ensp;&ensp;&ensp;&ensp; -Disabled by default <br>