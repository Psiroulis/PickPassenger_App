package com.redpepper.taxiapp.Splash;

public interface SplashActivityMVP {

    interface Model {

        boolean isUserLoggedIn();


    }

    interface View {

        void showNoAvailableNetworkMessage();

        void moveToLoginScreen();

        void moveToMainScreen();

    }

    interface Presenter {

        void setView(SplashActivityMVP.View view);

        void getLogedInUser();



        void rxUnsubscribe();

    }

}
