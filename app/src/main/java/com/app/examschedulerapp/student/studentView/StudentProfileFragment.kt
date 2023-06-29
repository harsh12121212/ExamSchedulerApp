package com.app.examschedulerapp.student.studentView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.examschedulerapp.student.studentModel.Student
import com.app.examschedulerapp.student.studentViewModel.StudentProfileViewModel
import com.app.examschedulerapp.databinding.FragmentStudentProfileBinding
import com.app.examschedulerapp.repository.UserRepository

class StudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentStudentProfileBinding
    private lateinit var viewModel: StudentProfileViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        userRepository = UserRepository()
        viewModel = ViewModelProvider(this)[StudentProfileViewModel::class.java]
        viewModel.setUserRepository(userRepository)

        viewModel.studentData.observe(viewLifecycleOwner) { student ->
            bindStudentData(student)
        }

        viewModel.fetchStudentData()

        return binding.root
    }

    private fun bindStudentData(student: Student?) {
        student?.let {
            binding.tvStudentprofileStudentname.text = "Name: ${student.name}"
            binding.tvStudentprofileStudentemail.text = "Email: ${student.email}"
            binding.tvStudentprofileStudentdob.text = "Birthdate: ${student.dob}"
            binding.tvStudentprofileStudenteducation.text = "Education: ${student.education}"
            binding.tvStudentprofileStudenttechnologytraining.text = "Technical Trainings: ${student.technology}"
            binding.tvStudentprofileStudentworkexp.text = "Work Experience: ${student.exp} years"
            binding.tvStudentprofileStudenttechchoice.text = "Technology Chosen: ${student.techchoice}"
            binding.tvStudentprofileStudentcity.text = "Preferred City of Exam: ${student.loc}"
        }
    }
}