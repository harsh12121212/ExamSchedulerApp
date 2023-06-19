package com.app.examschedulerapp.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * The ViewModel class for the HomeFragment.
 * Responsible for handling the business logic related to the home screen.
 */
class HomeViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    /**
     * Signs out the current user if there is any.
     */
    fun signOutCurrentUser() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
        }
    }
}