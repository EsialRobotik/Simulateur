# PrincessViewer
A simple 2D Eurobot robot simulator to planify, test, replay or follow 
in live a Eurobot match.

It is developed by EsialRobotik, our robots are princesses so there 
is the PrincessViewer !

## Usage

### Scale
The viewer use a scale of 1 px for 1 mm, so you have to zoom out to see
all the table

### Customize your robot
By default, you will see the EsialRobotik robots representation. You can
override them in javascript/currentYear.js by providing your own png files.
You can also switch year and table orientation

## Dependencies
This tools use EsaeJs to manipulate a canvas and TweenJs to perform the 
animations. Both are documented here : https://createjs.com

## Parse instructions
This simulator expect an object for each instruction. You can find some json
strategy example in the repository. Feel free to implement a new parser if you want.

### Instruction object
```json
{
    "task": "Position de départ",
    "command": "start",
    "position": {
      "x": 2800,
      "y": 1775,
      "theta": 3.141592653589793
    }
}
```
task : Description of the instruction
command: Task to execute
position.x : Robot X position
position.y : Robot Y position
position.theta : Robot Angle

### Commands
You can use anything for the command, the only key word are `delete-zone#zone-id`
`add-zone#zone-id` to add or remove a forbidden zone

### table.json
This is a json description of the forbidden zone of the table. There is two type of
zone :
- zonesInterdites : Static forbidden zone, like table border
- elementsJeu : Dynamic forbidden zone, addable and removale, like a play element