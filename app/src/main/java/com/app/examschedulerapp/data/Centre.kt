package com.app.examschedulerapp.data

data class Centre(val name: String, val centre: String, val slot: Int) {
    constructor() : this("", "", 0)
}