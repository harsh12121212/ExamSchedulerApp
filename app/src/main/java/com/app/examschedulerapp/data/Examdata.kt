package com.app.examschedulerapp.data

data class Examdata(
    var sf_city: String? = null,
    var sf_centre: String? = null,
    var sf_slot: String? = null,
    var sf_examdate: String? = null,
    var status: String = "Waiting",
    var studentName: String = "",
    var studentEmailId: String = "",
    var studentId: String = "",
    var countid: Int = 0
) {
    constructor() : this("", "", "", "", "NA", "", "", "")
}
