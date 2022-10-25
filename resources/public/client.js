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
                gboard = boardAndStatus.board;
                paintGrid(gboard);
                document.getElementById("status").innerText = boardAndStatus.status;
                 if (boardAndStatus.status == "WON") {
                     alert("You win!");
                 }
                 if (boardAndStatus.status == "LOST") {
                     alert("You lose!");
                 }
            })
}
