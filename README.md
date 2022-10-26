# Codecamp Instructions

- lein uberjar
- docker build -t cc22/game-api
- docker run -p 3000:3000 cc22/game-api
- http://127.0.0.1:3000/people


# Comparing strategies

Use this to simulate a game of one strategy vs another:
```clojure
(defn trial [p1 v1 p2 v2 board]
  (let [newBoardWithStatus (apply p1 board v1 '())]
    ; (prn newBoardWithStatus)
    (if (= GAME_IN_PROGRESS (newBoardWithStatus :status))
      (trial p2 v2 p1 v1 (newBoardWithStatus :board))
      (newBoardWithStatus :status)
      )))
```
Call it like this:
```clojure
(trial cpuOpponent2 :X cpuOpponentRandomMoves :O {})
```
To simulate a lot of games and count results do this:
```clojure
(sort 
  (frequencies 
    (take 1000 (repeatedly #(trial cpuOpponent2 :X cpuOpponentRandomMoves :O {})))))
=> (["DRAW" 171] ["LOST" 24] ["WON" 805])
```


# clojure-game-api

FIXME: description

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

**build**:

    $ lein uberjar

**run**:

    $ java -jar clojure-game-api-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
