package com.connected.codingkata

import java.util.Collections

class Lift(val speaker: Speaker) {
    val MAX_FLOORS = 10
    var floor: Int = 0
    var targetFloor: Int? = null
    var pushedButtons: HashMap<Int, Boolean> = HashMap()
    val direction: Direction
        get() {
            val tf = targetFloor ?: return Direction.NO_DIRECTION
            return when {
                floor < tf -> Direction.UP
                floor > tf -> Direction.DOWN
                else -> Direction.NO_DIRECTION
            }
        }
    var doors: Doors = Doors.CLOSED

    fun findNextDestination(): Int {
        var min = 0
        var max = MAX_FLOORS
        when (direction) {
            Direction.UP -> min = floor + 1
            Direction.DOWN -> max = floor - 1
            else -> {
                min = floor
                max = floor
            }
        }

        //go through the range and check the pushed buttons and find
        //the first button that is true and return it

        for (i in min..max) {
            if (pushedButtons[i] == true) {
                return i
            }
        }

        //add safeguard
        return floor
    }

    // When someone presses the up or down button outside of an elevator
    // floor - Floor on which the person pressed the button
    // direction - Up or down depending on what was pressed
    fun call(calledFloor: Int, calledDirection: Direction) {
        if (!calledDirection.equals(direction) && direction != Direction.NO_DIRECTION) {
            return
        }

        targetFloor = calledFloor
        pushedButtons[calledFloor] = true
    }

    fun timeStep() {
        if (doors == Doors.OPENED) {
            doors = Doors.CLOSED
        }

        if (targetFloor == null){
            return
        }

        if (floor < targetFloor!!) {
            floor++
        } else if (floor > targetFloor!!) {
            floor--
        }

        if (pushedButtons.get(floor) == true) {
            speaker.ding()
            doors = Doors.OPENED
            pushedButtons[floor] = false
        }
    }

    fun timeStep(count: Int) {
        for (i in 0..count) {
            timeStep()
        }
    }

    // When someone presses "destination" from inside of the elevator
    fun pushFloor(floor: Int) {
        if (direction == Direction.DOWN && floor >= this.floor
            || direction == Direction.UP && floor <= this.floor) {
            return
        }

        pushedButtons[floor] = true

        if(targetFloor == null){
            targetFloor = floor
            return
        }

        targetFloor = when (direction) {
            Direction.UP -> Math.max(floor, targetFloor!!)
            Direction.DOWN -> Math.min(floor, targetFloor!!)
            Direction.NO_DIRECTION -> floor
        }
    }
}

enum class Direction {
    UP, DOWN, NO_DIRECTION
}

enum class Doors {
    OPENED, CLOSED
}

class Speaker {
    fun ding() {}
}