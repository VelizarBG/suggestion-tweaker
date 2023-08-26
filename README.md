# Suggestion Tweaker

This is a client/server mod that improves the way suggestions are filtered and sorted when writing a command.

## Dependencies

This mod requires [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config). [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) is optional in case you want more configuration like enabling case sensitivity and changing the filtering mode.

## Improved filtering

- Vanilla usually starts comparing the input with the start of the suggestion candidate and discards if it doesn't match. In some cases it checks for a `.` or `_` and then the input. This can often be annoying when trying to search for something because it is too strict. This mod changes that, so now you have a choice between different filtering modes:
  - `STRICT` - the vanilla way
  - `SLIGHTLY LOOSE` - shows all suggestions containing the input anywhere inside
  - `LOOSE` - like the previous but with multiple words, separated by an underscore
  - `VERY LOOSE` - shows all suggestions containing every letter in the input


- Suggestions with a specific prefix can be hidden. That applies to both directory and file names. For example, if the specific prefix is an underscore, an identifier named `datapack:_function` will be hidden and so will be `datapack:_directory/function` but `datapack:directory/function` won't be. It works for all identifiers, not just functions. This feature is disabled by default.

## Improved sorting

Suggestions are sorted from the strictest to most loose match. Vanilla only sorts alphabetically, lacking multiple filtering modes.

## Server and client

Suggestions can be separated into two groups:
1. ones that are filtered on the server(based on the client's input);
    - e.g. functions, loot tables, item modifiers, predicates, and a few others
2. ones that are filtered on the client.
    - e.g. scoreboard criteria, colors, blocks, and others

Since the first group's filtering is controlled by the server, a special workaround is established to let the client use the new way of filtering suggestions, independent of the server. Therefore, this mod is also a server mod and provides better suggestions even for clients without the mod.

## Examples

Top - vanilla, bottom - Suggestion Tweaker.

<img src="https://user-images.githubusercontent.com/31567122/163831589-613e5483-f946-4afa-b8a7-069d714d9943.png" width="512">
<br/>
<img src="https://user-images.githubusercontent.com/31567122/163831940-3ea6063e-1adc-494c-b3cc-91f358331c7f.png" width="512">
<br/>
<img src="https://user-images.githubusercontent.com/31567122/163832068-dca72913-b8bb-4cda-b827-ae49fd43beba.png" width="512">

## Idea

Credit goes to SnaveSutit for the cool idea!

## Compiling
* Clone the repository
* Open a command prompt/terminal to the repository directory
* Run 'gradlew build'
* The built jar files will be in fabric/build/libs/ and forge/build/libs