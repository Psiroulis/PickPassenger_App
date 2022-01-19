package com.redpepper.taxiapp.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintSet;

import com.redpepper.taxiapp.databinding.ActivityLoginBinding;
import com.redpepper.taxiapp.Root.App;
import com.redpepper.taxiapp.Signup.SignUpActivity;
import com.redpepper.taxiapp.Validate_password.ValidatePasswordActivity;
import com.redpepper.taxiapp.validators.PhoneNumberValidator;

import javax.inject.Inject;

public class LoginActivity extends Activity implements LoginActivityMVP.View{

    private PhoneNumberValidator phoneNumberValidator;

    private ActivityLoginBinding binding;

    @Inject
    LoginActivityMVP.Presenter presenter;

    ConstraintSet activityDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        ((App) getApplication()).getComponent().inject(this);

        handleButtonsClicks();

        activityDefault = new ConstraintSet();
        activityDefault.clone(binding.parent);


    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);

        phoneNumberValidator = new PhoneNumberValidator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.rxUnsubscribe();
    }

    @Override
    public String getPhone() {
        return binding.phoneEditText.getText().toString();

    }

    @Override
    public void showUserNotExistsMessage() {

        TransitionManager.beginDelayedTransition(binding.parent);

        activityDefault.setVisibility(binding.dialogLayout.getId(),ConstraintSet.VISIBLE);

        activityDefault.applyTo(binding.parent);

    }

    @Override
    public void moveToPasswordValidationScreen() {

        Intent intent = new Intent(this, ValidatePasswordActivity.class);

        intent.putExtra("phone", getPhone());

        startActivity(intent);

        this.finish();

    }

    @Override
    public void showWrongPhoneMessage() {

        Toast.makeText(this,"Invalid number", Toast.LENGTH_LONG).show();

    }


    private void hideNewUserDialog(){

        TransitionManager.beginDelayedTransition(binding.parent);

        activityDefault.setVisibility(binding.dialogLayout.getId(),ConstraintSet.INVISIBLE);

        activityDefault.applyTo(binding.parent);

    }

    private void handleButtonsClicks(){

        /* Login Button */
        binding.loginButton.setOnClickListener(v->{

            if( phoneNumberValidator.isPhoneNumberValid(getPhone())){

                presenter.checkIfUserExists(getPhone());

            }else{

                showWrongPhoneMessage();
            }

        });

        //Facebook Button
        binding.fbButon.setOnClickListener(v->{

        });

        //Google Button
        binding.googleButton.setOnClickListener(v->{

        });

        //Terms of use text click
        binding.termsText.setOnClickListener(v->{

        });

        //New user dialog retry Button
        binding.dialogRetryButton.setOnClickListener(v-> hideNewUserDialog());

        //New user dialog SignUp Button
        binding.dialogSignupButton.setOnClickListener(v->{

           hideNewUserDialog();

           Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

           intent.putExtra("phone", getPhone());

           startActivity(intent);

           LoginActivity.this.finish();

        });

        binding.loginSignupButton.setOnClickListener(v->{

            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

            intent.putExtra("phone", getPhone());

            startActivity(intent);

            LoginActivity.this.finish();

        });

    }


}
