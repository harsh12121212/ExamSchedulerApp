package com.app.examschedulerapp.student.studentView

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.student.studentViewModel.StudentStatusViewModel
import com.app.examschedulerapp.student.studentAdapter.StudentStatusAdapter
import com.app.examschedulerapp.databinding.FragmentStudentStatusBinding
import com.app.examschedulerapp.repository.ExamRepository
import com.app.examschedulerapp.repository.ExamRepositoryInterface

class StudentStatusFragment : Fragment() {
    private lateinit var binding: FragmentStudentStatusBinding
    private lateinit var viewModel: StudentStatusViewModel
    private lateinit var userAdapter: StudentStatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentStatusBinding.inflate(inflater, container, false)

        val repository: ExamRepositoryInterface = ExamRepository()
        viewModel = ViewModelProvider(this, StudentStatusViewModelFactory(repository))[StudentStatusViewModel::class.java]

        observeData()

        return binding.root
    }

    private fun observeData() {
        viewModel.examData.observe(viewLifecycleOwner) { examDataList ->
            userAdapter = StudentStatusAdapter(examDataList)
            binding.rvData.layoutManager = LinearLayoutManager(activity)
            binding.rvData.adapter = userAdapter
        }
    }
}

class StudentStatusViewModelFactory(private val repository: ExamRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentStatusViewModel::class.java)) {
            return StudentStatusViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}