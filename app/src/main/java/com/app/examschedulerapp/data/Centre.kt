package com.app.examschedulerapp.data

data class Centre(val name: String, val centre: Int, val slot: Int) {
    constructor() : this("", 0, 0)
}