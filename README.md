
# JBUtils
This is a plugin for JailBreakMC that adds features to help devs create anything.

## Dependencies
This plugin has no real dependencies. Only "soft" dependencies. Add them for more features, but are not required.

 - [Skript](https://github.com/SkriptLang/Skript)

 
## Commands
- `/loottable|ltb` Opens the Loot Table menu. ~ **Perm: jbutils.admin**
- `/ltb roll <table> <amount>` Will roll x amount of items from the table. ~ **Perm: jbutils.admin**
## Skript Usage


`%amount to win% of loottable %id of loottable%`

aka

`%number% of loottable %string%`

example:

`set {_winnings::*} to 15 of loottable "lottery"`