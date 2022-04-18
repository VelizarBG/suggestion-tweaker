# Suggestion Tweaker

This is a client mod which changes the way identifiers(such as functions, item modifiers, loot tables, etc.) and other things(scoreboard objective criteria, opped players, datapacks, and others) are picked in the suggestions tab when writing a command in chat.

Vanilla usually starts comparing the input with the start of the suggestion candidate and discards if it doesn't match. In some cases it checks for a `.` or `_` and then the input. This can often be annoying when trying to search for something because it is too strict. This mod changes that, so now any entry containing the input anywhere inside it is included.

## Examples

Top - vanilla, bottom - Suggestion Tweaker.

<img src="https://user-images.githubusercontent.com/31567122/163831589-613e5483-f946-4afa-b8a7-069d714d9943.png" width="512">
<br/>
<img src="https://user-images.githubusercontent.com/31567122/163831940-3ea6063e-1adc-494c-b3cc-91f358331c7f.png" width="512">
<br/>
<img src="https://user-images.githubusercontent.com/31567122/163832068-dca72913-b8bb-4cda-b827-ae49fd43beba.png" width="512">

## Compiling
* Clone the repository
* Open a command prompt/terminal to the repository directory
* Run 'gradlew build'
* The built jar file will be in build/libs/