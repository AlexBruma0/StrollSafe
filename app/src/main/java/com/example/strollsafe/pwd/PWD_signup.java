package com.example.strollsafe.pwd;

import android.os.Bundle;

import com.example.strollsafe.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.strollsafe.databinding.ActivityPwdSignupBinding;

public class PWD_signup extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPwdSignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_signup);

    } // end of onCreate()
}