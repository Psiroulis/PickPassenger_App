package com.redpepper.taxiapp.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.Utils.NetworkUtil;
import com.redpepper.taxiapp.Login.LoginActivity;
import com.redpepper.taxiapp.Main.MainActivity;
import com.redpepper.taxiapp.Root.App;

import javax.inject.Inject;

public class SplashActivity extends Activity implements SplashActivityMVP.View{

    @Inject
    SplashActivityMVP.Presenter presenter;

    @Inject
    NetworkUtil networkUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ((App) getApplication()).getComponent().inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.setView(this);

        if(networkUtil.isDeviceConnectedToNetwork()){

            presenter.getLogedInUser();

        }else{

            showNoAvailableNetworkMessage();
        }

    }

    @Override
    public void moveToLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);

        this.finish();
    }

    @Override
    public void moveToMainScreen() {

        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
    }

    @Override
    public void showNoAvailableNetworkMessage() {
        Toast.makeText(this,"No internet available",Toast.LENGTH_LONG).show();
    }
}
