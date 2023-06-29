package com.app.examschedulerapp.admin.adminView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.admin.adminadapters.AdminAllRequestsAdapter
import com.app.examschedulerapp.admin.adminViewmodel.AdminAllRequestsViewModel
import com.app.examschedulerapp.databinding.FragmentAdminAllRequestsBinding
import com.app.examschedulerapp.repository.ExamRepository

class AdminAllRequestsFragment : Fragment() {
    private lateinit var binding: FragmentAdminAllRequestsBinding
    private lateinit var viewModel: AdminAllRequestsViewModel
    private lateinit var AdminAllRequestsAdapter: AdminAllRequestsAdapter

    private class AdminAllRequestsViewModelFactory(private val examRepository: ExamRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminAllRequestsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminAllRequestsViewModel(examRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminAllRequestsBinding.inflate(inflater, container, false)

        val examRepository = ExamRepository()
        viewModel = ViewModelProvider(this, AdminAllRequestsViewModelFactory(examRepository))[AdminAllRequestsViewModel::class.java]

        AdminAllRequestsAdapter = AdminAllRequestsAdapter(this)
        binding.adminallreqRv.layoutManager = LinearLayoutManager(activity)
        binding.adminallreqRv.adapter = AdminAllRequestsAdapter

        viewModel.exams.observe(viewLifecycleOwner) { exams ->
            AdminAllRequestsAdapter.setExams(exams)
        }

        viewModel.loadExams()

        return binding.root
    }
}