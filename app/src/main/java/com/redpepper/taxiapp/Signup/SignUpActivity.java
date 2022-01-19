package com.redpepper.taxiapp.Signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintSet;

import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.databinding.ActivitySignUpActvityBinding;
import com.redpepper.taxiapp.Login.LoginActivity;
import com.redpepper.taxiapp.Root.App;
import com.redpepper.taxiapp.Validate_password.ValidatePasswordActivity;
import com.redpepper.taxiapp.validators.NameValidator;
import com.redpepper.taxiapp.validators.PhoneNumberValidator;

import javax.inject.Inject;

public class SignUpActivity extends Activity implements SignUpActivityMVP.View {

    @Inject
    SignUpActivityMVP.Presenter presenter;

    private ActivitySignUpActvityBinding binding;

    ConstraintSet activityDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpActvityBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        ((App) getApplication()).getComponent().inject(this);

        handleButtonsClick();

        activityDefault = new ConstraintSet();

        activityDefault.clone(binding.signUpParent);
    }

    private void handleButtonsClick() {

        binding.signUpCTAButton.setOnClickListener(v -> {

            NameValidator nameValidator = new NameValidator();

            PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();

            if (binding.signUpName.getText().toString().equalsIgnoreCase("") ||
                    binding.signUpSurname.getText().toString().equalsIgnoreCase("") ||
                    binding.signUpPhoneNumber.getText().toString().equalsIgnoreCase("")) {

                showMessageDialog(getResources().getString(R.string.signup_warning_empty_field));

            } else if (!nameValidator.isNameValid(binding.signUpName.getText().toString()) ||
                    !nameValidator.isNameValid(binding.signUpSurname.getText().toString())) {

                showMessageDialog(getResources().getString(R.string.signup_invalid_name_field));

            } else if (!phoneNumberValidator.isPhoneNumberValid(binding.signUpPhoneNumber.getText().toString())) {

                showMessageDialog(getResources().getString(R.string.signup_invalid_phone_field));

            } else {
                presenter.registerUser(
                        binding.signUpName.getText().toString(),
                        binding.signUpSurname.getText().toString(),
                        binding.signUpPhoneNumber.getText().toString()
                );
            }

        });

        binding.signUpDialogButton.setOnClickListener(v -> hideMessageDialog());

        binding.signUpLoginBtn.setOnClickListener(v-> {

            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);

            this.finish();

        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);

        Intent intent = getIntent();

        if (intent.getStringExtra("phone") != null &&
                !intent.getStringExtra("phone").equalsIgnoreCase("")) {

            binding.signUpPhoneNumber.setText(intent.getStringExtra("phone"));

        }

    }

    @Override
    public void proceedToValidatePasswordActivity() {
        Intent intent = new Intent(this, ValidatePasswordActivity.class);

        intent.putExtra("phone", binding.signUpPhoneNumber.getText().toString());

        startActivity(intent);

        this.finish();
    }

    @Override
    public void showCreateUserErrorMessage(String errorMessage) {

        showMessageDialog(errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.rxUnsubscribe();
    }


    private void showMessageDialog(String message) {

        binding.signUpDialogMessageTxt.setText(message);

        TransitionManager.beginDelayedTransition(binding.signUpParent);

        activityDefault.setVisibility(binding.signUpDialogLayout.getId(), ConstraintSet.VISIBLE);

        activityDefault.applyTo(binding.signUpParent);

    }

    private void hideMessageDialog() {

        TransitionManager.beginDelayedTransition(binding.signUpParent);

        activityDefault.setVisibility(binding.signUpDialogLayout.getId(), ConstraintSet.GONE);

        activityDefault.applyTo(binding.signUpParent);

        binding.signUpDialogMessageTxt.setText("");
    }
}

