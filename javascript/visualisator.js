var stage;

var bigPrincess;
var pmiPrincess;
var margin = 0;

var stratSimulator;
var stratLog;
var stratIndex = 0;
var timestampLog;

var stratPmiSimulator;
var stratPmiLog;
var stratPmiIndex = 0;
var timestampPmiLog;

var rotationTime = 100;
var moveTime = 600;
var detected = [];
var detectedPmi = [];

/**
 * Initialisation des robots à animer
 * @var object big : {x, y, theta, regX, regY}
 * @var object small : {x, y, theta, regX, regY}
 */
function init(big, small) {
    stage = new createjs.Stage("canvas");

    bigPrincess = new createjs.Bitmap('./bigPrincess.png');
    bigPrincess.x = 800;
    bigPrincess.y = 800;
    // Décallage du point d'animation au centre du robot
    bigPrincess.regX = big.regX;
    bigPrincess.regY = big.regY;
    bigPrincess.alpha = 0.7;
    stage.addChild(bigPrincess);

    pmiPrincess = new createjs.Bitmap('./pmiPrincess.png');
    pmiPrincess.x = 1200;
    pmiPrincess.y = 800;
    // Décallage du point d'animation au centre du robot
    pmiPrincess.regX = small.regX;
    pmiPrincess.regY = small.regY;
    pmiPrincess.alpha = 0.7;
    stage.addChild(pmiPrincess);

    stage.update();

    createjs.Ticker.setFPS(60);
    createjs.Ticker.addEventListener("tick", stage);
    initRobot(big, small);

    var inputs = document.querySelectorAll('.inputfile');
    Array.prototype.forEach.call(inputs, function (input) {
        var label = input.nextElementSibling,
            labelVal = label.innerHTML;

        input.addEventListener('change', function (e) {
            var fileName = '';
            if (this.files && this.files.length > 1)
                fileName = (this.getAttribute('data-multiple-caption') || '').replace('{count}', this.files.length);
            else
                fileName = e.target.value.split('\\').pop();

            if (fileName)
                label.querySelector('span').innerHTML = fileName;
            else
                label.innerHTML = labelVal;
        });
    });
}

/**
 * Mise en position de départ des robots
 * @var object big : {x, y, theta, regX, regY}
 * @var object small : {x, y, theta, regX, regY}
 */
function initRobot(big, small) {
    createjs.Tween.get(bigPrincess)
        .to({rotation: radiansToDegrees(big.theta)}, rotationTime, createjs.Ease.getPowInOut(4))
        .to({x: big.x, y: big.y}, moveTime, createjs.Ease.getPowInOut(4));

    createjs.Tween.get(pmiPrincess)
        .to({rotation: radiansToDegrees(small.theta)}, rotationTime, createjs.Ease.getPowInOut(4))
        .to({x: small.x, y: small.y}, moveTime, createjs.Ease.getPowInOut(4));
}

/**
 * Déplacement robot principal
 * @param x Position en X
 * @param y Position en Y
 * @param rotation Angle
 * @param speed Temps d'exécution
 */
function moveRobot(x, y, rotation, speed, auto = false) {
    var shape = new createjs.Shape();
    shape.graphics
        .setStrokeStyle(3)
        .beginStroke('rgba(255,20,147,1)')
        .moveTo(bigPrincess.x, bigPrincess.y)
        .lineTo(y, x);
    stage.addChild(shape);
    stage.update();

    var tRotation = rotationTime;
    var tMove = moveTime;
    if (speed) {
        tRotation = speed * 1/3;
        tMove = speed * 2/3;
    }

    createjs.Tween.get(bigPrincess)
        .to({rotation: radiansToDegrees(Math.PI - rotation)}, tRotation, createjs.Ease.getPowInOut(4))
        .to({x: y, y: x}, tMove, createjs.Ease.getPowInOut(4))
        .call(() => {
            if (auto) {
                autoPlay();
            }
        });
}

/**
 * Déplacement robot PMI
 * @param x Position en X
 * @param y Position en Y
 * @param rotation Angle
 * @param speed Temps d'exécution
 */
function movePmi(x, y, rotation, speed, auto = false) {
    var shape = new createjs.Shape();
    shape.graphics
        .setStrokeStyle(3)
        .beginStroke('rgb(255,105,180,1)')
        .moveTo(pmiPrincess.x, pmiPrincess.y)
        .lineTo(y, x);
    stage.addChild(shape);
    stage.update();

    var tRotation = rotationTime;
    var tMove = moveTime;
    if (speed) {
        tRotation = speed * 1/3;
        tMove = speed * 2/3;
    }

    createjs.Tween.get(pmiPrincess)
        .to({rotation: radiansToDegrees(Math.PI - rotation)}, tRotation, createjs.Ease.getPowInOut(4))
        .to({x: y, y: x}, tMove, createjs.Ease.getPowInOut(4))
        .call(() => {
            if (auto) {
                autoPlayPmi();
            }
        });
}

/**
 * Convertion des angles en degrés
 * @param radians
 * @returns {number}
 */
function radiansToDegrees(radians) {
    var pi = Math.PI;
    return radians * (180 / pi);
}

/**
 * Chargement de la table et affichage des zones interdites
 * @returns {boolean}
 */
function loadTable(jsonTable, flip = false) {
    margin = jsonTable.marge;

    // Zones interdites fixes
    jsonTable.zonesInterdites.forEach(zone => {
        displayZone(zone, jsonTable, 'rgba(255,0,0,0.6)', 'rgba(200,0,0,0.4)');
    });

    // Bordure haut
    var shape = new createjs.Shape();
    shape.graphics
        .beginFill('rgba(200,0,0,0.4)')
        .drawRect(0, 0, flip ? 2000 : 3000, jsonTable.marge);
    stage.addChild(shape);

    // Bordure bas
    shape = new createjs.Shape();
    shape.graphics
        .beginFill('rgba(200,0,0,0.4)')
        .drawRect(0, (flip ? 3000 : 2000) - jsonTable.marge, flip ? 2000 : 3000, flip ? 3000 : 2000);
    stage.addChild(shape);

    // Bordure gauche
    shape = new createjs.Shape();
    shape.graphics
        .beginFill('rgba(200,0,0,0.4)')
        .drawRect(0, 0, jsonTable.marge, flip ? 3000 : 2000);
    stage.addChild(shape);

    // Bordure droite
    shape = new createjs.Shape();
    shape.graphics
        .beginFill('rgba(200,0,0,0.4)')
        .drawRect((flip ? 2000 : 3000) - jsonTable.marge, 0, flip ? 2000 : 3000, flip ? 3000 : 2000);
    stage.addChild(shape);

    // Zones interdites mobiles
    jsonTable.elementsJeu.forEach(zone => {
        displayZone(zone, jsonTable, 'rgba(255,255,0,0.6)', 'rgba(255,165,0,0.4)');
    });
    stage.update();

    deleteZone('start0');
    deleteZone('start0_1');
//    deleteZone('start0_2');
    deleteZone('start0_3');
    deleteZone('start0_4');
    deleteZone('start0_5');
    deleteZone('start3000');
    deleteZone('start3000_1');
//    deleteZone('start3000_2');
    deleteZone('start3000_3');
    deleteZone('start3000_4');
    deleteZone('start3000_5');
}

/**
 * Chargement du fichier tbl pour validation
 *
 */
function loadTbl() {
    var file = document.getElementById('tblCheck');
    if (file && file.files && file.files.length) {
        var reader = new FileReader();
        reader.onload = function (e) {
            var tblArray = e.target.result.split('\n');
            var size = tblArray.shift().split(' ');
            for (var y = 0; y < size[1]/10; y++) {
                for (var x = 0; x < size[0]/10; x++) {
                    if (tblArray[x].charAt(y) == 'x') {
                        var shape = new createjs.Shape();
                        tblArray.forEach(value => {
                            shape
                                .graphics
                                .beginFill('rgba(255,0,0,0.4)')
                                .drawRect(y*10, x*10, 9, 9);
                        });
                        stage.addChild(shape);
                    }
                }
            }
            stage.update();
        };
        reader.readAsBinaryString(file.files[0]);

        return true;
    }
}

/**
 * Affichage des zones interdites
 * @param zone
 * @param jsonTable
 * @param colorPrimary
 * @param colorSecondary
 */
function displayZone(zone, jsonTable, colorPrimary, colorSecondary) {
    if (zone.forme == 'polygone') {
        if (zone.points.length === 4) {
            var topLeftCorner = zone.points[0];
            var bottomRightCorner = zone.points[0];
            zone.points.forEach(point => {
                if (topLeftCorner.x >= point.x && topLeftCorner.y >= point.y) {
                    topLeftCorner = point;
                }
                if (bottomRightCorner.x <= point.x && bottomRightCorner.y <= point.y) {
                    bottomRightCorner = point;
                }
            });

            var shape = new createjs.Shape();
            shape.name = zone.id;
            shape.visible = zone.active;
            shape.graphics
                .beginFill(colorPrimary)
                .drawRect(
                    topLeftCorner.y,
                    topLeftCorner.x,
                    bottomRightCorner.y - topLeftCorner.y,
                    bottomRightCorner.x - topLeftCorner.x
                );
            stage.addChild(shape);

            shape = new createjs.Shape();
            shape.name = zone.id + '_margin';
            shape.visible = zone.active;
            shape.graphics
                .beginFill(colorSecondary)
                .drawRect(
                    topLeftCorner.y - jsonTable.marge,
                    topLeftCorner.x - jsonTable.marge,
                    (bottomRightCorner.y - topLeftCorner.y) + jsonTable.marge * 2,
                    (bottomRightCorner.x - topLeftCorner.x) + jsonTable.marge * 2
                );
            stage.addChild(shape);
        } else if (zone.points.length === 3) {
            // Pour un triangle, on ajoute le margin à la mano parce que c'est trop chiant à calculer
            var color = zone.id.match('_margin') ? colorSecondary : colorPrimary;

            var shape = new createjs.Shape();
            shape.name = zone.id;
            shape.visible = zone.active;
            shape.graphics
                .beginFill(color)
                .moveTo(zone.points[0].y, zone.points[0].x)
                .lineTo(zone.points[1].y, zone.points[1].x)
                .lineTo(zone.points[2].y, zone.points[2].x)
                .lineTo(zone.points[0].y, zone.points[0].x);
            stage.addChild(shape);
        } else {
            alert('Je gère pas encore ce cas, have fun : ' + zone.id + '-' + zone.forme + '-' + zone.active);
        }
    } else if (zone.forme == 'cercle') {
        var shape = new createjs.Shape();
        shape.name = zone.id;
        shape.visible = zone.active;
        shape.graphics
            .beginFill(colorPrimary)
            .drawCircle(zone.centre.y, zone.centre.x, zone.rayon);
        stage.addChild(shape);

        shape = new createjs.Shape();
        shape.name = zone.id + '_margin';
        shape.visible = zone.active;
        shape.graphics
            .beginFill(colorSecondary)
            .drawCircle(zone.centre.y, zone.centre.x, zone.rayon + jsonTable.marge);
        stage.addChild(shape);
    }
}

/**
 * Suppression d'une zone interdite
 * @param zoneName
 */
function deleteZone(zoneName) {
    if (stage.getChildByName(zoneName)) {
        stage.getChildByName(zoneName).visible = false;
        stage.getChildByName(zoneName + '_margin').visible = false;
        stage.update();
    }
}

/**
 * Activation d'une zone interdite
 * @param zoneName
 */
function addZone(zoneName) {
    if (stage.getChildByName(zoneName)) {
        stage.getChildByName(zoneName).visible = true;
        stage.getChildByName(zoneName + '_margin').visible = true;
        stage.update();
    }
}

/**
 * Chargement de la start simulateur du robot principal
 * @returns {boolean}
 */
function loadSimulatorStrat(strategy) {
    stratSimulator = strategy;
    stratIndex = 0;
    document.getElementById('princessNext').disabled = false;
    document.getElementById('princessAuto').disabled = false;
    return true;
}

/**
 * Chargement de la start simulateur du robot PMI
 * @returns {boolean}
 */
function loadSimulatorStratPmi(strategy) {
    stratPmiSimulator = strategy;
    stratPmiIndex = 0;
    document.getElementById('pmiNext').disabled = false;
    document.getElementById('pmiAuto').disabled = false;
    return true;
}

/**
 * Chargement des logs d'un match du robot principal
 * @returns {boolean}
 */
function loadStratLog() {
    var file = document.getElementById('stratLog');
    if (file && file.files && file.files.length) {
        var reader = new FileReader();
        reader.onload = function (e) {
            stratLog = cleanLogFile(e.target.result);
            rotationTime = 50;
            moveTime = 50;
            stratIndex = 0;

            for (var key in stratLog) {
                if (stratLog[key].includes('TRACE: Position :')) {
                    var regexpAsserv = /.+\[Asserv\].+#([-0-9]+);([-0-9]+);([-0-9\.]+).+/;
                    var parseAsserv = regexpAsserv.exec(stratLog[key]);
                    if (parseAsserv != null) {
                        createjs.Tween.get(bigPrincess)
                            .to({rotation: radiansToDegrees(parseAsserv[3])}, rotationTime, createjs.Ease.getPowInOut(4))
                            .to({x: parseAsserv[2], y: parseAsserv[1]}, moveTime, createjs.Ease.getPowInOut(4));
                        console.log('Princess init : ' + parseAsserv[1] + ' - ' + parseAsserv[2] + ' - ' + radiansToDegrees(parseAsserv[3]));
                        break;
                    }
                }
            }
        };
        reader.readAsBinaryString(file.files[0]);
        document.getElementById('princessNext').disabled = false;
        document.getElementById('princessAuto').disabled = false;
        return true;
    }
}

/**
 * Chargement des logs d'un match du robot principal
 * @returns {boolean}
 */
function loadStratLogPmi() {
    var file = document.getElementById('stratPmiLog');
    if (file && file.files && file.files.length) {
        var reader = new FileReader();
        reader.onload = function (e) {
            stratPmiLog = cleanLogFile(e.target.result);
            rotationTime = 50;
            moveTime = 50;
            stratPmiIndex = 0;

            for (var key in stratPmiLog) {
                if (stratPmiLog[key].includes('TRACE: Position :')) {
                    var regexpAsserv = /.+\[Asserv\].+#([-0-9]+);([-0-9]+);([-0-9\.]+).+/;
                    var parseAsserv = regexpAsserv.exec(stratPmiLog[key]);
                    if (parseAsserv != null) {
                        createjs.Tween.get(pmiPrincess)
                            .to({rotation: radiansToDegrees(parseAsserv[3])}, rotationTime, createjs.Ease.getPowInOut(4))
                            .to({x: parseAsserv[2], y: parseAsserv[1]}, moveTime, createjs.Ease.getPowInOut(4));
                        console.log('PMI init : ' + parseAsserv[1] + ' - ' + parseAsserv[2] + ' - ' + radiansToDegrees(parseAsserv[3]));
                        break;
                    }
                }
            }
        };
        reader.readAsBinaryString(file.files[0]);
        document.getElementById('pmiNext').disabled = false;
        document.getElementById('pmiAuto').disabled = false;
        return true;
    }
}

function cleanLogFile(file) {
    var res = [];
    var drop = true;
    var split = file.split('\n');
    for (var key in split) {

        if (drop && split[key].includes('Tirette pull, begin of the match')) {
            drop = false;
        }
        if (!drop && split[key] !== '' && !split[key].includes('DEBUG: Detection NOK')) {
            res.push(split[key]);
        }
    }
    return res;
}

/**
 * Récupération et application de l'instruction suivante de la Princess
 * @returns {boolean}
 */
function nextInstruction(auto = false) {
    var startFile = stratSimulator ? stratSimulator : stratLog;
    if (stratIndex >= startFile.length) {
        return true;
    }
    var instruction = startFile[stratIndex];
    stratIndex++;

    if (stratSimulator) {
        playSimulatorInstruction(instruction, 'data', 'princess', auto);
    } else {
        playLogInstruction(instruction, 'princess', auto);
    }
    return false;
}

/**
 * Récupération et application de l'instruction suivante de la PMI
 * @returns {boolean}
 */
function nextInstructionPmi(auto = false) {
    var startFile = stratPmiSimulator ? stratPmiSimulator : stratPmiLog;
    if (stratPmiIndex >= startFile.length) {
        return true;
    }
    var instruction = startFile[stratPmiIndex];
    stratPmiIndex++;

    if (stratPmiSimulator) {
        playSimulatorInstruction(instruction, 'dataPmi', 'pmi', auto);
    } else {
        playLogInstruction(instruction, 'pmi', auto);
    }
    return false;
}

/**
 * Exécution d'une instruction du simulateur
 * @param instruction
 * @param divId
 * @param who
 */
async function playSimulatorInstruction(instruction, divId, who, auto = false) {
    var regexpZone = /(delete|add)-zone#(.+)/;
    var parseZone = regexpZone.exec(instruction.command);

    var dataDiv = document.getElementById(divId);
    dataDiv.insertAdjacentHTML('beforeend', '<strong>' + instruction.task + '</strong> : ' + instruction.command + '<br>');
    dataDiv.scrollTop = dataDiv.scrollHeight;
    if (who === 'princess') {
        moveRobot(instruction.position.x, instruction.position.y, instruction.position.theta);
    } else {
        movePmi(instruction.position.x, instruction.position.y, instruction.position.theta);
    }
    if (parseZone !== null) {
        if (parseZone[1] === 'delete') {
            deleteZone(parseZone[2]);
        } else {
            addZone(parseZone[2]);
        }
    }
    await sleep(moveTime + rotationTime);
    if (auto) {
        if (who === 'princess') {
            nextInstruction(true);
        } else {
            nextInstructionPmi(true);
        }
    }
}

async function playLogInstruction(instruction, who, auto = false) {
    var regexpTimestamp = /([0-9]{4}-[0-9]{2}-[0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2}),([0-9]+) .+/;
    var parseTimestamp = regexpTimestamp.exec(instruction);
    var delta = undefined;
    var divId;
    if (parseTimestamp != null) {
        var date = Date.parse(parseTimestamp[1] + 'T' + parseTimestamp[2] + ':' + parseTimestamp[3] + ':' + parseTimestamp[4] + '.' + parseTimestamp[5] + '+00:00');
        if (who === 'princess') {
            delta = date - timestampLog;
            timestampLog = date;
            divId = 'data';
        } else {
            delta = date - timestampPmiLog;
            timestampPmiLog = date;
            divId = 'dataPmi';
        }
    }

    if (delta == undefined || delta == NaN) {
        delta = 0;
    }

    var regexpAsserv = /.+\[Asserv\].+#([-0-9]+);([-0-9]+);([-0-9\.]+).+/;
    var parseAsserv = regexpAsserv.exec(instruction);
    if (parseAsserv != null) {
        if (who === 'princess') {
            moveRobot(parseAsserv[1], parseAsserv[2], parseAsserv[3], delta, auto);
        } else {
            movePmi(parseAsserv[1], parseAsserv[2], parseAsserv[3], delta, auto);
        }
    } else {
        var regexpInfo = /.+INFO :(.+)/;
        var parseInfo = regexpInfo.exec(instruction);
        if (parseInfo != null) {
            var dataDiv = document.getElementById(divId);
            dataDiv.insertAdjacentHTML('beforeend', parseInfo[1] + '<br>');
            dataDiv.scrollTop = dataDiv.scrollHeight;
        }

        var regexpDetection = /.+\[UltraSoundManager\] INFO : (.+) : STOP \(([-0-9]+),([-0-9]+)\)/;
        var parseDetection = regexpDetection.exec(instruction);
        if (parseDetection != null) {
            var colorPrimary = 'rgba(100,100,100,0.6)';
            var colorSecondary = 'rgba(200,200,200,0.4)'

            if (stage.getChildByName(who + '_' + parseDetection[1].replaceAll(' ', '_'))) {
                stage.removeChild(
                    stage.getChildByName(who + '_' + parseDetection[1].replaceAll(' ', '_')),
                    stage.getChildByName(who + '_' + parseDetection[1].replaceAll(' ', '_') + '_margin')
                );
                stage.update();
            }

            var shape = new createjs.Shape();
            shape.name = who + '_' + parseDetection[1].replaceAll(' ', '_');
            shape.active = true;
            shape.graphics
                .beginFill(colorPrimary)
                .drawCircle(parseDetection[3], parseDetection[2], 150);
            stage.addChild(shape);

            shape = new createjs.Shape();
            shape.name = who + '_' + parseDetection[1].replaceAll(' ', '_') + '_margin';
            shape.active = true;
            shape.graphics
                .beginFill(colorSecondary)
                .drawCircle(parseDetection[3], parseDetection[2], 150 + margin);
            stage.addChild(shape);
            stage.update();
            detected.push(who + '_' + parseDetection[1].replaceAll(' ', '_'));
        }

        if (instruction.includes('INFO : OK devant')) {
            var keep = [];
            detected.forEach(val => {
                if (val.includes(who)) {
                    stage.removeChild(
                        stage.getChildByName(val),
                        stage.getChildByName(val + '_margin')
                    );
                } else {
                    keep.push(val);
                }
            });
            detected = keep;
            stage.update();
        }

        // TODO virer les fantomes des logs
        // TODO logs propre des zones interdites

        await sleep(delta);

        if (auto) {
            if (who === 'princess') {
                nextInstruction(true);
            } else {
                nextInstructionPmi(true);
            }
        }
    }
}

async function autoPlay() {
    nextInstruction(true);
}

async function autoPlayPmi() {
    nextInstructionPmi(true);
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function loadFiles() {
    //loadTable();
    //loadSimulatorStrat();
    //loadSimulatorStratPmi();
    loadStratLog();
    loadStratLogPmi();
    loadTbl();
}

function connectSocket() {
    var socket = null;
    try {
        socket = new WebSocket("ws://192.168.0.103:4269");
    } catch (exception) {
        console.error(exception);
    }

    // Récupération des erreurs.
    // Si la connexion ne s'établie pas,
    // l'erreur sera émise ici.
    socket.onerror = function (error) {
        console.error(error);
    };

    // Lorsque la connexion est établie.
    socket.onopen = function (event) {
        console.log("Connexion établie.");

        // Lorsque la connexion se termine.
        this.onclose = function (event) {
            console.log("Connexion terminé.");
        };

        // Lorsque le serveur envoi un message.
        this.onmessage = function (event) {
            var regexpLog = /([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]+) \[([a-z]+)\](.+)/;
            var parseLog = regexpLog.exec(event.data);
            if (parseLog != null) {
                playLogInstruction(parseLog[1] + ' ' + parseLog[3], parseLog[2], false);
            } else {
                var dataDiv = document.getElementById('data');
                dataDiv.insertAdjacentHTML('beforeend', event.data + '<br>');
                dataDiv.scrollTop = dataDiv.scrollHeight;
            }
        };

        this.send("loggerListener");
    };
}
