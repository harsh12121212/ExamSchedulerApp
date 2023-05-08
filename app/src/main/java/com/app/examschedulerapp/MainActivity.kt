package com.app.examschedulerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.examschedulerapp.data.examdata
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

private lateinit var navController: NavController
//FirebaseAuth
private lateinit var firebaseAuth: FirebaseAuth

//for current user direct entry
var user = FirebaseAuth.getInstance().currentUser


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}