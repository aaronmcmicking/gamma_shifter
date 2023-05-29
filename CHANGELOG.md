# Gamma Shifter 1.0.0

### Additions
-Added Auto Night Mode<br>
-Verified QuiltMC v0.18.10 compatability <br>
-Added `Auto Night Mode` category to configuration menu <br>
-Added option to toggle enforcement of Gamma Shifter's max/min values (0% - 2000%) <br>
-Added default keybinds for `Preset 1`, `Preset 2`, and `Show current gamma` (`;`, `'`, and `m`, respectively) <br>

### Changes
-Renamed `Brightness` option to `Custom Gamma` and updated tooltip <br>
-Changed default max gamma to 2000% <br>
-Changed default custom gamma to 2000% <br>
-Changed default preset values to 500% and 1000% <br>
-Config menu now references "gamma" instead of "brightness" <br>
-Gamma overlay and hotbar message now show when mod effects are disabled <br>
-"Saved options" log message now only appears when closing the game <br>
-Reordered options on `HUD` configuration page <br>

### Fixes
-Fixed bug where buffered inputs for `showGammaKey` were not flushed <br>

### Notes
<!-- This use of the HTML space tag is messy but native MD lists looked bad here -->
-Auto Night Mode: <br>
&ensp;&ensp;&ensp;&ensp; -Increases/decreases gamma automatically at dusk/dawn <br>
&ensp;&ensp;&ensp;&ensp; -Defaults to 300% <br>
&ensp;&ensp;&ensp;&ensp; -Stores separate value than used in manual toggling <br>
&ensp;&ensp;&ensp;&ensp; -Can be enabled/disabled and customized through ModMenu <br>
&ensp;&ensp;&ensp;&ensp; -Disabled by default <br>