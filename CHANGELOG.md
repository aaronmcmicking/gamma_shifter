# Gamma Shifter 0.3.0
***Changelog from 0.2.1***

### Additions
-Added option to snap new gamma values to multiples of the custom change-per-step value<br>
-Added option to always save a custom gamma value to file, even if mod effects are disabled<br>
-Added two custom preset hotkeys (unbound by default)<br>
-Added key to display the current gamma without changing it (unbound by default)<br>

### Changes
-Changed max change-per-input value to 1000% (was 250%)<br>

### Fixes
-Fixed bug where buffered inputs would not be flushed<br>
-Fixed bug where toggling mod effects with hotkey would not be saved to gamma_shifter.properties (now saved more often)<br>
-Values read from gamma_shifter.properties are now bounded to min/max values<br>
-Fixed bug where gamma values > 100% would still be initialized from options.txt when `alwaysStartEnabled` was set to `false`<br>

### Notes
-New options are available through ModMenu (requires ModMenu and Cloth Config API)