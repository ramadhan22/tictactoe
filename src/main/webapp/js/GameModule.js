var gameModule = angular.module('gameModule', []);

gameModule.controller('newGameController', ['$rootScope', '$scope', '$http', '$location',

    function (rootScope, scope, http, location) {

        rootScope.gameId = null;
        scope.newGameData = null;

        scope.newGameOptions = {
            availablePieces: [
                {name: 'X'},
                {name: 'O'}
            ],
            selectedPiece: {name: 'O'},
            availableGameTypes: [
                {name: 'PLAYER'},
                {name: 'COMPUTER'}
            ],
            selectedBoardDimension: {name: 'COMPUTER'}
        };

        scope.createNewGame = function () {

            var data = scope.newGameData;
            var params = JSON.stringify(data);

            http.post("/game/create", params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                rootScope.gameId = data.id;
                location.path('/game/' + rootScope.gameId);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        }

    }
]);


gameModule.controller('playerGamesController', ['$scope', '$http', '$location', '$routeParams',
    function (scope, http, location, routeParams) {

        scope.playerGames = [];

        http.get('/game/player/list').success(function (data) {
            scope.playerGames = data;
        }).error(function (data, status, headers, config) {
            location.path('/player/panel');
        });

        scope.loadGame = function (id) {
            console.log(id);
            location.path('/game/' + id);
        }

    }]);


gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
       var gameStatus;
       getInitialData()

        function getInitialData() {

           http.get('/game/' + routeParams.id).success(function (data) {
                scope.gameProperties = data;
                gameStatus = scope.gameProperties.gameStatus;
                scope.currentPlayer = scope.gameProperties.firstPlayer;
                prepareBoard(data.totalRowColumn);
                getMoveHistory();
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
        }

        function getMoveHistory() {
            scope.movesInGame = [];
          return  http.get('/move/list').success(function (data) {
                scope.movesInGame = data;
                scope.playerMoves = [];

                //paint the board with positions from the retrieved moves
                angular.forEach(scope.movesInGame, function(move) {
                    scope.rows[move.boardRow - 1][move.boardColumn - 1].letter = move.playerPieceCode;
                });
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed to load moves in game"
            });
        }

        function getNextMove() {

        scope.nextMoveData = []

        // COMPUTER IS A SECOND PLAYER
        if(!scope.gameProperties.secondPlayer) {
            http.get("/move/autocreate").success(function (data, status, headers, config) {
                scope.nextMoveData = data;
                getMoveHistory().success(function () {
                    var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].gameStatus;
                    if (gameStatus != 'IN_PROGRESS') {
                        alert(gameStatus)
                    }
                    scope.currentPlayer = scope.gameProperties.firstPlayer;
                });
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Can't send the move"
            });

            // SECOND PLAYER IS A REAL USER
        } else {
          console.log(' another player move');
        }
        }

        function checkIfBoardCellAvailable(boardRow, boardColumn) {

            for (var i = 0; i < scope.movesInGame.length; i++) {
                var move = scope.movesInGame[i];
                if (move.boardColumn == boardColumn && move.boardRow == boardRow) {
                    return false;
                }
            }
            return true;
        }

        scope.rows = [];

        console.log(scope.rows);

        function prepareBoard(totalRowColumn) {
            for (let indexRow = 1; indexRow <= totalRowColumn; indexRow++) {
                console.log(indexRow);
                var column = [];
                for (let indexColumn = 1; indexColumn <= totalRowColumn; indexColumn++) {
                    var columnId = ((indexRow*10)+indexColumn);
                    column.push({
                        'id': columnId.toString(),
                        'letter': '',
                        'class': 'box'
                    });
                }
                scope.rows.push(column);
    
                
            }
        }

        scope.markPlayerMove = function (column) {
            var boardRow = parseInt(column.id.charAt(0));
            var boardColumn = parseInt(column.id.charAt(1));
            var params = {'boardRow': boardRow, 'boardColumn': boardColumn, 'player': scope.currentPlayer}

            if (checkIfBoardCellAvailable(boardRow, boardColumn) == true) {
                http.post("/move/create", params, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function () {
                    getMoveHistory().success(function () {

                        var gameStatus = scope.movesInGame[scope.movesInGame.length - 1].gameStatus;
                        if (gameStatus == 'IN_PROGRESS') {
                            getNextMove();
                        } else {
                            alert(gameStatus)
                        }

                        // change current player
                        if (scope.currentPlayer == scope.gameProperties.firstPlayer) {
                            scope.currentPlayer = scope.gameProperties.secondPlayer;
                        } else {
                            scope.currentPlayer = scope.gameProperties.firstPlayer;
                        }
                    });

                }).error(function (data, status, headers, config) {
                    scope.errorMessage = "Can't send the move"
                });
            }
        };


    }
]);



