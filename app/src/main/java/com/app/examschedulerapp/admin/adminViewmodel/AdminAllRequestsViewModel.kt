package com.app.examschedulerapp.admin.adminViewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.examschedulerapp.data.Examdata
import com.app.examschedulerapp.repository.ExamRepository

class AdminAllRequestsViewModel(private val examRepository: ExamRepository) : ViewModel() {
    private val _exams: MutableLiveData<List<Examdata>> = MutableLiveData()
    val exams: LiveData<List<Examdata>> = _exams

    fun loadExams() {
        examRepository.getExams { exams ->
            _exams.postValue(exams)
        }
    }
}