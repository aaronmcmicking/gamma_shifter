# gamma_shifter

A client-side FabricMC mod that restores the ability to set the gamma (brightness) setting beyond 100% in Minecraft 1.19.2

## Default Keybinds

Decrease gamma by 20%: [ <br>
Increase gamma by 20%: ]

## Features

Gamma settings can be changed with hotkeys without using the pause menu <br>
Gamma settings are loaded and written from options.txt without being clamped to 100%

## Notes

The write() method in GameOptions.java will still log an error when trying to save a gamma value > 1.0 <br>
Manually written gamma values will always appear as the last line of options.txt <br>
The server will still receive information about the client settings (GameOptions.sendClientSettings())