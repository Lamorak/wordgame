# Wordgame

[[https://github.com/lamorak/wordgame/blob/master/game.png|alt=game screenshot]]

## Description
This code challenge presents a simple game where user is presented one word in english and one word in spanish and has to decide withing given limit whether those words have the same meaning. The score represents number of right answers minus number of wrong answers but cannot be lower than zero. The app remembers five highscores with names of players.

## Technology
The app relies on `RxJava-2` in combination with `Java-8` lambdas to handle most of the tasks. Input data is stored in `assets` and parsed with `GSON`. Storage is handled by Android's `SharedPreferences` as implementing a propper database would consume too much  time and would not be necessary for the game in current state. The archiecture was also compromised to simple Activity based MVC in order to save time on sacrificing testability. 

## Room for improvement
No designer was present in the development process, therefore, user interface is very simple and designed just to be functional. It is the most underinvested part of the application which definitely needs improvement.

A propper architecture like MVVM would allow presence of tests and make the code maintainable in case of adding more features. Current architecture is compromised in order to avoid over-engineering.

## Decisions
I have decided to stay with Java eventhough I believe Kotlin is more suited for Android projects. Currently I am still more fluent in Java.

The applications architecture and small scale render writing tests as inefficient as the potential time saved by easier debugging is smaller than time required to write tests.

## Time invested
In clean numbers approximately 5:30 hours were spent on the project. The distribution of time is as follows:

- 2:00 Date storage and model
- 2:00 Game mechanics
- 0:30 User interface
- 0:30 Testing, debugging, optimization
- 0:30 Project setup, documentation