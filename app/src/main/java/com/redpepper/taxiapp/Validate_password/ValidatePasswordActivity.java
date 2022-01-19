package com.redpepper.taxiapp.Validate_password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintSet;

import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.databinding.ActivityValidatePasswordBinding;
import com.redpepper.taxiapp.Login.LoginActivity;
import com.redpepper.taxiapp.Main.MainActivity;
import com.redpepper.taxiapp.Root.App;

import javax.inject.Inject;

public class ValidatePasswordActivity extends Activity implements ValidatePasswordActivityMVP.View {


    @Inject
    ValidatePasswordActivityMVP.Presenter presenter;

    private String phone;

    private String password;

    private ActivityValidatePasswordBinding binding;

    private StringBuilder passBuilder;

    private ConstraintSet activityDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityValidatePasswordBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        ((App) getApplication()).getComponent().inject(this);

        passBuilder = new StringBuilder();

        handleButtonsClick();

        activityDefault = new ConstraintSet();

        activityDefault.clone(binding.valParent);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }



    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);

        Intent intent = getIntent();

        phone = intent.getStringExtra("phone");

        binding.valPhoneTextview.setText(phone);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.rxUnsubscribe();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);

        this.finish();

    }

    @Override
    public void showWrongCredentialsMessage() {

        showMyDialog(getResources().getString(R.string.wrong_password));

    }

    @Override
    public void proceedToMainActivityWithLoggedInUser() {

        if (phone != null) {
            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("phone", phone);

            intent.putExtra("password", password);

            startActivity(intent);

            this.finish();
        }
    }

    @Override
    public void showResentSmsSuccessMessage() {
        showMyDialog(getResources().getString(R.string.resent_sms_message));
    }

    @Override
    public void showServerErrorMessage() {
        showMyDialog(getResources().getString(R.string.server_error));
    }

    private void handleButtonsClick() {

        binding.valSubmitButton.setOnClickListener(v->{

            Log.d("blepo","length"+ passBuilder.length());

            if(phone != null && passBuilder.length() == 6){

                presenter.loginUser(phone,password);

            }else{

                showMyDialog(getResources().getString(R.string.no_valid_password));

            }

        });

        binding.valPadN0.setOnClickListener(v-> createPasswordString("0"));
        binding.valPadN1.setOnClickListener(v-> createPasswordString("1"));
        binding.valPadN2.setOnClickListener(v-> createPasswordString("2"));
        binding.valPadN3.setOnClickListener(v-> createPasswordString("3"));
        binding.valPadN4.setOnClickListener(v-> createPasswordString("4"));
        binding.valPadN5.setOnClickListener(v-> createPasswordString("5"));
        binding.valPadN6.setOnClickListener(v-> createPasswordString("6"));
        binding.valPadN7.setOnClickListener(v-> createPasswordString("7"));
        binding.valPadN8.setOnClickListener(v-> createPasswordString("8"));
        binding.valPadN9.setOnClickListener(v-> createPasswordString("9"));
        binding.valPadNDelete.setOnClickListener(v->{
            if(passBuilder.length() > 0){

                switch (passBuilder.length()){
                    case 1: binding.passNumEdt1.setText("");
                        break;
                    case 2: binding.passNumEdt2.setText("");
                        break;
                    case 3: binding.passNumEdt3.setText("");
                        break;
                    case 4: binding.passNumEdt4.setText("");
                        break;
                    case 5: binding.passNumEdt5.setText("");
                        break;
                    case 6: binding.passNumEdt6.setText("");
                        break;
                    default: binding.passNumEdt1.setText("E");
                }

                passBuilder.setLength(passBuilder.length() - 1);
            }

            password = passBuilder.toString();

        });

        binding.valResentPassButton.setOnClickListener(v->presenter.resentSms(phone));

        binding.valDialogOkButton.setOnClickListener(v->hideMyDialog());

    }

    private void createPasswordString(String numberPressed){
        if(passBuilder.length() >= 0 && passBuilder.length() < 6){

            passBuilder.append(numberPressed);

            password = passBuilder.toString();

            fillNumberEditText(passBuilder.length(),numberPressed);

        }
    }

    private void fillNumberEditText(int length , String number){
        switch (length){
            case 1: binding.passNumEdt1.setText(number);
                break;
            case 2: binding.passNumEdt2.setText(number);
                break;
            case 3: binding.passNumEdt3.setText(number);
                break;
            case 4: binding.passNumEdt4.setText(number);
                break;
            case 5: binding.passNumEdt5.setText(number);
                break;
            case 6: binding.passNumEdt6.setText(number);
                break;
            default:binding.passNumEdt1.setText("E");
        }
    }

    private void showMyDialog(String message){
        binding.valDialogMessageText.setText(message);

        TransitionManager.beginDelayedTransition(binding.valParent);

        activityDefault.setVisibility(binding.valDialogLayout.getId(),ConstraintSet.VISIBLE);

        activityDefault.applyTo(binding.valParent);
    }

    private void hideMyDialog(){

        TransitionManager.beginDelayedTransition(binding.valParent);

        activityDefault.setVisibility(binding.valDialogLayout.getId(),ConstraintSet.INVISIBLE);

        activityDefault.applyTo(binding.valParent);
    }




}
