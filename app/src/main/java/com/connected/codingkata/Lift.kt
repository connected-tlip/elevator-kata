package com.connected.codingkata

import java.util.Collections

class Lift (val speaker: Speaker){
    val MAX_FLOORS = 10
    var floor: Int = 0
    var targetFloor: Int = 0
    var pushedButtons: ArrayList<Boolean> = ArrayList(Collections.nCopies(MAX_FLOORS, false))
    val direction: Direction
        get() = when {
            floor < targetFloor -> Direction.UP
            floor > targetFloor -> Direction.DOWN
            else -> Direction.NO_DIRECTION
        }
    var doors: Doors = Doors.CLOSED

    // decide which floor to move to next
    fun move(destination: Int) {
        targetFloor = destination
    }

    fun findNextDestination(): Int {
        var min = 0
        var max = MAX_FLOORS
        when(direction) {
            Direction.UP -> min = floor + 1
            Direction.DOWN -> max = floor - 1
            else -> {
                min = floor
                max = floor
            }
        }

        //go through the range and check the pushed buttons and find
        //the first button that is true and return it

        for (i in min .. max) {
            if (pushedButtons[i]) {
                return i
            }
        }

        //add safeguard
        return floor
    }

    // When someone presses the up or down button outside of an elevator
    // floor - Floor on which the person pressed the button
    // direction - Up or down depending on what was pressed
    fun call(floor: Int, direction: Direction) {
        move(floor)
    }

    fun timeStep() {
        if (doors == Doors.OPENED) {
            doors = Doors.CLOSED
        }

        if (floor < targetFloor) {
            floor++
        } else if ( floor > targetFloor){
            floor--
        }

        if(floor == targetFloor) {
            speaker.ding()
            doors = Doors.OPENED
        }
    }

    // When someone presses "destination" from inside of the elevator
    fun pushFloor(floor: Int) {
        pushedButtons[floor] = true
        val target = findNextDestination()
        System.out.println(target)
        move(target)
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