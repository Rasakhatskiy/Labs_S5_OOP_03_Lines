package com.example.lines

enum class BallColor(val value: Int) {
    Red(0),
    Blue(1),
    Brown(2),
    Green(3),
    Yellow(4),
    None(-1);

    companion object {
        fun fromInt(value: Int) = BallColor.values().first { it.value == value }
    }
}