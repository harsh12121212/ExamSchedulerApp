package com.app.examschedulerapp.student.studentView

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.examschedulerapp.R
import com.app.examschedulerapp.student.studentViewModel.StudentRegisterViewModel
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.student.studentModel.Student
import com.app.examschedulerapp.databinding.FragmentStudentRegisterBinding
import com.app.examschedulerapp.repository.UserRepository
import com.app.examschedulerapp.repository.UserRepositoryInterface
import java.util.*

const val PASSWORD_LENGTH_ERROR = "password must at least 6 characters long"
const val INVALID_DATA = "Please enter valid data"

class StudentRegisterFragment : Fragment() {

    private lateinit var binding: FragmentStudentRegisterBinding
    private lateinit var viewModel: StudentRegisterViewModel
    private lateinit var userRepository: UserRepositoryInterface

    private val cityList = arrayOf("Select City", "Bangalore", "Hyderabad", "Chennai")
    private val techList = arrayOf("Select Technology Choice", "Kotlin", "Java", "Dart")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRepository = UserRepository()
        viewModel = StudentRegisterViewModel(userRepository)
        setupLocationSpinner()
        setupTechnologySpinner()
        setupDatePickers()
        setupSignUpButton()
    }

    private fun setupLocationSpinner() {
        val locationAdapter = ArrayAdapter(requireContext(), R.layout.spinnerlayout, cityList)
        binding.etStudLoc.adapter = locationAdapter
    }

    private fun setupTechnologySpinner() {
        val technologyAdapter = ArrayAdapter(requireContext(), R.layout.spinnerlayout, techList)
        binding.etStudTechchoice.adapter = technologyAdapter
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.etStudDob.setOnClickListener {
            showDatePickerDialog(year, month, day) { mYear, mMonth, mDay ->
                binding.etStudDob.setText("$mDay/$mMonth/$mYear")
            }
        }

        binding.etStudExamdate.setOnClickListener {
            showDatePickerDialogforexam(year, month, day) { mYear, mMonth, mDay ->
                binding.etStudExamdate.setText("$mDay/$mMonth/$mYear")
            }
        }
    }

    private fun showDatePickerDialog(year: Int, month: Int, day: Int, onDateSet: (Int, Int, Int) -> Unit) {
        val datePickerDialog = DatePickerDialog(requireContext(), { _, mYear, mMonth, mDay ->
            onDateSet(mYear, mMonth, mDay)
        }, year, month, day)

        // Set maximum date
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    private fun showDatePickerDialogforexam(year: Int, month: Int, day: Int, onDateSet: (Int, Int, Int) -> Unit) {
        val datePickerDialog = DatePickerDialog(requireContext(), { _, mYear, mMonth, mDay ->
            onDateSet(mYear, mMonth, mDay)
        }, year, month, day)

        // Set minimum date
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    private fun setupSignUpButton() {
        binding.btnSignin.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val name = binding.etStudName.text.toString().trim()
        val email = binding.etStudEmail.text.toString().trim()
        val dob = binding.etStudDob.text.toString().trim()
        val education = binding.etStudEducation.text.toString().trim()
        val technology = binding.etStudTechnology.text.toString().trim()
        val exp = binding.etStudWorkexp.text.toString().trim()
        val techChoice = binding.etStudTechchoice.selectedItem.toString().trim()
        val loc = binding.etStudLoc.selectedItem.toString().trim()
        val date = binding.etStudExamdate.text.toString().trim()
        val password = binding.etStudPswd.text.toString().trim()
        val type = DBConstants.STUDENT

        if (TextUtils.isEmpty(name)) {
            binding.etStudName.error = INVALID_DATA
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etStudEmail.error = INVALID_DATA
        } else if (password.length < 6) {
            binding.etStudPswd.error = PASSWORD_LENGTH_ERROR
        } else if (dob.isEmpty() || education.isEmpty() || technology.isEmpty() ||
            exp.isEmpty() || date.isEmpty()
        ) {
            Toast.makeText(requireContext(), INVALID_DATA, Toast.LENGTH_SHORT).show()
        } else {
            val studentData = Student(
                name = name,
                email = email,
                dob = dob,
                education = education,
                technology = technology,
                exp = exp,
                techchoice = techChoice,
                loc = loc,
                date = date,
                password = password,
                type = type,
                uid = ""
            )

            viewModel.createUserWithEmailAndPassword(email, password) { isSuccess ->
                if (isSuccess) {
                    viewModel.saveStudentData(studentData) { isSaved ->
                        if (isSaved) {
                            Toast.makeText(
                                requireContext(),
                                "Account created successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(activity, StudentMainActivity::class.java))
                            activity?.finish()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to save student data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}