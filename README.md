# Codecamp clojure-game experiments

See <https://clojure-game-api.apps.okd.baloise.dev/tictactoe>

## Codecamp Instructions

- lein uberjar
- docker build -t cc22/game-api
- docker run -p 3000:3000 cc22/game-api
- http://127.0.0.1:3000/people

## Objectives & Lessons learned

### "Prod first"

All our experiments are built using a ci pipeline (gh actions) with
Leiningen and the uberjar target. We
deploy the artiacts on openshift: <https://github.com/baloise-incubator/code-camp-apps/tree/master/clojure-game>
accessible
below <https://clojure-game-api.apps.okd.baloise.dev/>.

### "Real" application with web ui and persistence

We wanted to write a "real" application with a web ui and some persistence --
all stuff that is not purely functional. With clojure that was quite easily
done with libs like ring or compojure. As to persistence we only used an
in-memory store.

### Use "clean architecture" patterns

We wanted to have "clean" and "pure" core modules with no technical stuff in
them and use the core from a web adapter or from a persistence adapter. In
clojure we had to use a different way of addressing these issues from what
we were used to in the java world. One can see the taken solution in
the separation of the core, main and web modules. But as everything is
public there is no guarantee that the core part does not call the web part
directly.

We tried to have a common web module which routes traffic to /tictactoe or
to /sudoku respectively. But it should not know about routes below these
contexts. But we never managed to do this in a good way.

### Use test driven development

We managed to start our tictactoe experiment with TDD - and this worked quite
well. But then, we slowly grew accustomed to using the REPL and this was then
the main driver for development. Like REPL, REPL, ... , Copy Impl from REPL and
write a test.

### Other remarks

- Some of us used the cursive plugin in intellij. This provides a very good development experience.
- The clojredocs are very good. In particular the examples are useful (eg <https://clojuredocs.org/clojure.core/take>)


## Tictactow

### Comparing strategies

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

### Sudoku


