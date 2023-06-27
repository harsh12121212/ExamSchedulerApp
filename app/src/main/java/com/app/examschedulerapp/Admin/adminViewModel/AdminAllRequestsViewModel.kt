package com.app.examschedulerapp.Admin.adminViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.repository.ExamRepository

class AdminAllRequestsViewModel(private val examRepository: ExamRepository) : ViewModel() {
    private val _exams: MutableLiveData<List<examdata>> = MutableLiveData()
    val exams: LiveData<List<examdata>> = _exams

    fun loadExams() {
        examRepository.getExams { exams ->
            _exams.postValue(exams)
        }
    }
}