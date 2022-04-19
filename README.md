# Suggestion Tweaker

This is a client and server mod which changes the way suggestions are picked in the suggestions tab when writing a command in chat.

## Improved filtering

Vanilla usually starts comparing the input with the start of the suggestion candidate and discards if it doesn't match. In some cases it checks for a `.` or `_` and then the input. This can often be annoying when trying to search for something because it is too strict. This mod changes that, so now any entry containing the input anywhere inside it is included.

## Server and client

Suggestions can be separated into two groups - ones that are filtered on the server(based on the client's input) and ones that aren't. The former are things such as functions, loot tables, item modifiers, predicates, and a few others. The latter are things such as scoreboard criteria, colors, blocks, and others. Since the former's filtering is controlled by the server, a workaround is established to let the client use the new way of filtering suggestions independent of the server. This mod is also a server mod and provides better suggestions even for clients without the mod(but only the former group of suggestions).

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
* The built jar file will be in build/libs/