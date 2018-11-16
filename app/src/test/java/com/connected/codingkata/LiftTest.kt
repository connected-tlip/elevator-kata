package com.connected.codingkata

import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LiftTest {

    lateinit var lift: Lift

    @Before
    fun setup() {
        lift = Lift(speaker = mock(Speaker::class.java))
    }

    @Test
    fun `starts on floor 0`() {
        assertThat(lift.floor, equalTo(0))
    }

    @Test
    fun `move resets direction to NO_DIRECTION`() {
        lift.floor = 1

        lift.targetFloor = 4

        for (i in 1..3)
            lift.timeStep()

        assertThat(lift.direction, equalTo(Direction.NO_DIRECTION))
    }

    @Test
    fun `move up - floor test`() {
        lift.targetFloor = 4
        for (i in 1..4) {
            lift.timeStep()
        }
        assertThat(lift.floor, equalTo(4))
    }

    @Test
    fun `move down - 1 floor test`() {
        lift.targetFloor = -1
        lift.timeStep()
        assertThat(lift.floor, equalTo(-1))
    }

    @Test
    fun `call elevator - from ground floor`() {
        val floor = 0
        val direction = Direction.UP

        lift.floor = -1

        lift.call(floor, direction)
        lift.timeStep()

        //test doors open, test direction, test what floor it's on
        assertThat(lift.floor, equalTo(0))
        assertThat(lift.doors, equalTo(Doors.OPENED))
    }

    @Test
    fun `call elevator - from floor test`() {
        lift.floor = 7

        lift.call(9, Direction.DOWN)
        lift.timeStep()
        lift.timeStep()

        assertThat(lift.floor, equalTo(9))
    }

    @Test
    fun `given elevator arrives at destination - makes ding sound on arrival`() {
        lift.floor = 4

        lift.call(5, Direction.DOWN)
        lift.timeStep()

        verify(lift.speaker).ding()
    }

    @Test
    fun `it takes 1 second to move 1 floor`() {
        lift.floor = 1
        lift.targetFloor = 3

        assertThat(lift.floor, equalTo(1))
        assertThat(lift.direction, equalTo(Direction.UP))

        lift.timeStep()

        assertThat(lift.floor, equalTo(2))
        assertThat(lift.direction, equalTo(Direction.UP))
    }

    @Test
    fun `it moves towards the caller at 1 floor per second`() {
        lift.floor = 4
        lift.call(8, Direction.DOWN)

        lift.timeStep()

        assertThat(lift.floor, equalTo(5))
        assertThat(lift.direction, equalTo(Direction.UP))
    }

    @Test
    fun `while elevator is in motion - doors are closed`() {
        lift.floor = 0
        lift.doors = Doors.OPENED

        lift.targetFloor = 3
        lift.timeStep()
        lift.timeStep()

        assertEquals(Doors.CLOSED, lift.doors)
    }

    @Test
    fun `push floor - going down - stops at that floor`() {
        //setup
        lift.floor = 3
        lift.pushFloor(1)

        lift.timeStep()
        assertEquals(Doors.CLOSED, lift.doors)

        lift.timeStep()
        assertEquals(Doors.OPENED, lift.doors)
        assertEquals(1, lift.floor)
    }

    @Test
    fun `push floor - going up stops at that floor`() {
        //setup
        lift.floor = 1
        lift.pushFloor(3)

        lift.timeStep()
        assertEquals(2, lift.floor)
        assertEquals(Doors.CLOSED, lift.doors)

        lift.timeStep()
        assertEquals(Doors.OPENED, lift.doors)
        assertEquals(3, lift.floor)
    }

    @Test
    fun `push floor - consecutive button presses`() {
        lift.floor = 1

        lift.pushFloor(2)
        lift.pushFloor(3)

        lift.timeStep()

        assertEquals(2, lift.floor)
        assertEquals(Doors.OPENED, lift.doors)

        lift.timeStep()
        assertEquals(3, lift.floor)
        assertEquals(Doors.OPENED, lift.doors)
    }

    @Test
    fun `floors and doors - when pressing floors out of order going up - opens in order`() {
        lift.floor = 1

        lift.pushFloor(2)
        lift.pushFloor(5)
        lift.pushFloor(3)

        lift.timeStep()
        lift.timeStep()
        assertEquals(3, lift.floor)
        assertEquals(Doors.OPENED, lift.doors)

        lift.timeStep()
        lift.timeStep()
        assertEquals(5, lift.floor)
        assertEquals(Doors.OPENED, lift.doors)
    }

    @Test
    fun `floors and doors - when pressing floors out of order going down - opens in order`() {
        lift.floor = 5

        lift.pushFloor(2)
        lift.pushFloor(3)

        lift.timeStep()
        lift.timeStep()
        assertEquals(3, lift.floor)
        assertEquals(Doors.OPENED, lift.doors)

        lift.timeStep()
        assertEquals(2, lift.floor)
        assertEquals(Doors.OPENED, lift.doors)
    }

//    @Test
//    fun `push multiple floor - stop at each floor`() {
//        //setup
//        lift.floor = 0
//        lift.call(3, Direction.DOWN)
//        lift.timeStep()
//        lift.timeStep()
//        lift.timeStep()
//
//        // Elevator is at floor 3
//        lift.pushFloor(2)
//        lift.pushFloor(0)
//
//        lift.timeStep()
//        assertEquals(Doors.OPENED, lift.doors)
//        assertEquals(2, lift.floor)
//
//        lift.timeStep()
//        assertEquals(Doors.CLOSED, lift.doors)
//
//        lift.timeStep()
//        assertEquals(Doors.OPENED, lift.doors)
//        assertEquals(0, lift.floor)
//    }


}