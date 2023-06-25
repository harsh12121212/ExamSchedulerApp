package com.app.examschedulerapp.Admin.adminView

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.Admin.adminModel.admin
import com.app.examschedulerapp.Admin.adminViewModel.AdminRegisterViewModel
import com.app.examschedulerapp.R
import com.app.examschedulerapp.Student.studentView.INVALID_DATA
import com.app.examschedulerapp.Student.studentView.PASSWORD_LENGTH_ERROR
import com.app.examschedulerapp.Student.studentView.StudentMainActivity
import com.app.examschedulerapp.data.DBConstants.ADMIN
import com.app.examschedulerapp.databinding.FragmentAdminRegisterBinding
import com.app.examschedulerapp.repository.UserRepository
import com.app.examschedulerapp.repository.UserRepositoryInterface

class AdminRegisterFragment : Fragment() {
    private lateinit var binding: FragmentAdminRegisterBinding
    private val viewModel: AdminRegisterViewModel by viewModels()

    private var citylist = arrayOf("Select City", "Banglore", "Hyderabad", "Chennai")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityarrayAdapter = ArrayAdapter(requireActivity(), R.layout.spinnerlayout, citylist)
        binding.etAdminCity.setSelection(0)
        binding.etAdminCity.adapter = cityarrayAdapter
        binding.etAdminCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btnSignin.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val name = binding.etAdminName.text.toString().trim()
        val email = binding.etAdminEmail.text.toString().trim()
        val city = binding.etAdminCity.selectedItem.toString().trim()
        val centre = binding.etAdminCentre.text.toString().trim()
        val firstslot = binding.etAdminSlotone.text.toString().trim()
        val secondslot = binding.etAdminSlottwo.text.toString().trim()
        val password = binding.etAdminPswd.text.toString().trim()
        val type = ADMIN

        if (TextUtils.isEmpty(name)) {
            binding.etAdminName.error = INVALID_DATA
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etAdminEmail.error = INVALID_DATA
        } else if (password.length < 6) {
            binding.etAdminPswd.error = PASSWORD_LENGTH_ERROR
        } else if (centre.isEmpty()) {
            binding.etAdminCentre.error = "Please enter the data"
        } else if (firstslot.isEmpty()) {
            binding.etAdminSlotone.error = "Please enter the data"
        } else if (secondslot.isEmpty()) {
            binding.etAdminSlottwo.error = "Please enter the data"
        } else {
            // Data is valid, proceed with account creation
            viewModel.createUserAccount(email, password,
                onSuccess = {
                    Toast.makeText(activity, "LoggedIn as $email", Toast.LENGTH_LONG).show()
                    saveData(name, email, city, centre, firstslot, secondslot, password, type)
                },
                onFailure = { error ->
                    Toast.makeText(activity, "Login failed due to $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun saveData(
        name: String,
        email: String,
        city: String,
        centre: String,
        firstslot: String,
        secondslot: String,
        password: String,
        type: String
    ) {
        val uid = viewModel.firebaseAuth.currentUser?.uid

        binding.progressbar.visibility = View.VISIBLE

        viewModel.saveData(
            name, email, city, centre, firstslot, secondslot, password, type, uid,
            onSuccess = {
                makeText(activity, "Account created successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(activity, AdminMainActivity::class.java))
                activity?.finish()
            },
            onFailure = { error ->
                makeText(activity, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }
}