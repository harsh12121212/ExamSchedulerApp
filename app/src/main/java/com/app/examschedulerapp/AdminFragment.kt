package com.app.examschedulerapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.databinding.FragmentAdminBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private lateinit var user: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile ->{
            }
            R.id.logout -> {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                user.signOut();
                                showSnackBar( "Successfully Logging out! ")
                                findNavController().navigate(R.id.action_firstFragment_to_loginFragment)
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                dialog.dismiss()
                            }
                        }
                    }
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show()
            }
        }
        return true
    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}