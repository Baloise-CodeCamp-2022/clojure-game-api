var gboard = {};

function paintGrid(board) {

    function paintCell(cell) {
        function nn(val) {
            return val ? val : "";
        }

        document.getElementById(cell).innerText = nn(board[cell]);
    }

    paintCell("A1");
    paintCell("A2");
    paintCell("A3");
    paintCell("B1");
    paintCell("B2");
    paintCell("B3");
    paintCell("C1");
    paintCell("C2");
    paintCell("C3");
}

function move(cell) {
    const resp = fetch('/tictactoe/move', {
        method: 'POST',
        body: JSON.stringify({
            "board": gboard,
            "move": {
                "field": cell,
                "value": "X"
            }
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    resp.then(res => res.json())
            .then(boardAndStatus => {
                let message
                gboard = boardAndStatus.board;
                let message;
                paintGrid(gboard);
                if (boardAndStatus.status == "WON") {
                    message = "You win!";
                }
                if (boardAndStatus.status == "LOST") {
                    message = "You lose!";
                }
                if (boardAndStatus.status == "DRAW") {
                   message = "Draw!";
                }
                document.getElementById("status").innerText = message;
            })
}

function saveGame(name) {
    const resp = fetch('/tictactoe/game', {
        method: 'POST',
        body: JSON.stringify({
            "board": gboard,
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    resp.then(res => res.json())
            .then(boardAndStatus => {
                gboard = boardAndStatus.board;
                paintGrid(gboard);
                document.getElementById("status").innerText = boardAndStatus.status;
            })
}

function loadGame(name) {
    const resp = fetch(`/tictactoe/game/${name}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    resp.then(res => res.json())
            .then(boardAndStatus => {
                gboard = boardAndStatus.board;
                paintGrid(gboard);
                document.getElementById("status").innerText = boardAndStatus.status;
            })
}
