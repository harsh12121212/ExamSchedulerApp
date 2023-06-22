package com.app.examschedulerapp.Student.studentView

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.Student.studentViewModel.StudentStatusViewModel
import com.app.examschedulerapp.adapters.StudentStatusAdapter
import com.app.examschedulerapp.data.*
import com.app.examschedulerapp.databinding.FragmentStudentStatusBinding
import com.google.firebase.database.*

class StudentStatusFragment : Fragment() {

    private lateinit var binding: FragmentStudentStatusBinding
    private lateinit var viewModel: StudentStatusViewModel
    private lateinit var userAdapter: StudentStatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentStatusBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(StudentStatusViewModel::class.java)
        viewModel.init()

        observeData()

        return binding.root
    }

    private fun observeData() {
        viewModel.examData.observe(viewLifecycleOwner) { examDataList ->
            userAdapter = StudentStatusAdapter(requireContext(), examDataList)
            binding.rvData.layoutManager = LinearLayoutManager(activity)
            binding.rvData.adapter = userAdapter
        }
    }
}