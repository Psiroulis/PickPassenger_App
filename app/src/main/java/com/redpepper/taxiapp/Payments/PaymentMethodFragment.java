package com.redpepper.taxiapp.Payments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.redpepper.taxiapp.BuildConfig;
import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.databinding.PaymentFragmentListBinding;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripePaymentMethod;
import com.redpepper.taxiapp.Payments.adapter.PaymentMethodAdapter;
import com.redpepper.taxiapp.Root.App;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.SetupIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmSetupIntentParams;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.SetupIntent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@SuppressWarnings("deprecation")
public class PaymentMethodFragment extends Fragment implements PaymentMethodFragmentMVP.View,
        PaymentMethodAdapter.PaymentsListItemClickListener {

    private PaymentFragmentListBinding binding;

    private ConstraintSet addCardBgLayConstrainSet;

    private PaymentFragmentListener mListener;

    private Stripe stripe;

    private boolean isStripeCustomerCreated;

    private String stripeClientSecret = null;

    private List<StripePaymentMethod> paymentsList;

    private boolean isCardNumberCompleted = false;
    private boolean isCardHolderCompleted = false;
    private boolean isCardExpireDateCompleted = false;
    private boolean isCardCvcCompleted = false;
    private String lastPaymentMethodSelected;
    private SharedPreferences prefs;

    @Inject
    PaymentMethodFragmentMVP.Presenter presenter;

    public PaymentMethodFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((App) context.getApplicationContext()).getComponent().inject(this);

        if(context instanceof PaymentFragmentListener){

            mListener = (PaymentFragmentListener) context;

        }else{

            throw new RuntimeException(context.toString() +
                    "must implement PaymentFragmentListener");
        }

        prefs = requireActivity().getSharedPreferences("app",Context.MODE_PRIVATE);

        isStripeCustomerCreated = prefs.getBoolean("isStripeCustomerCreated", false);

        lastPaymentMethodSelected = prefs.getString("lastPaymentMethodSelected","");

        presenter.getUsersPaymentMethods();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.setView(this);

        stripe = new Stripe(requireActivity(), BuildConfig.STRIPE_API_KEY);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = PaymentFragmentListBinding.inflate(inflater,container,false);

        if(lastPaymentMethodSelected.equalsIgnoreCase("cash")){
            binding.payfragCashButton.setBackgroundResource(R.drawable.bg_yellow_selected_payment_type);
        }

        setNewCardFieldsRules();

        binding.payfragBackButton.setOnClickListener(v->{
            if(mListener != null){
                mListener.onPaymentFragmentBackButtonClick();
            }
        });

        binding.payfragCashButton.setOnClickListener(v->{
            if(mListener != null){
                mListener.onPaymentFragmentCashButtonClick();
            }

            prefs.edit().putString("lastPaymentMethodSelected","cash").apply();
        });

        binding.payfragAddNewButton.setOnClickListener(v->{

            presenter.getClientSecret();

            makeUiChangesToShowAddNewCardLayout();
        });

        binding.playfragCloseAddNewLayButton.setOnClickListener(v->{

            stripeClientSecret = null;

            makeUiChangesToHideAddNewCardLayout();
        });

        return binding.getRoot();
    }

    @Override
    public void fillSavedPaymentMethodsList(List<StripePaymentMethod> paymentMethodsList) {

        paymentsList = new ArrayList<>();

        paymentsList.addAll(paymentMethodsList);

        PaymentMethodAdapter adapter = new PaymentMethodAdapter(getContext(),paymentMethodsList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        binding.payfragCardList.setLayoutManager(layoutManager);

        binding.payfragCardList.setItemAnimator(new DefaultItemAnimator());

        binding.payfragCardList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void setStripeClientSecretString(String stripeClientSecretString) {
        this.stripeClientSecret = stripeClientSecretString;
    }

    @Override
    public void onPaymentItemClick(View view, int position) {
        if(mListener!=null){
            mListener.onPaymentFragmentCardItemClick(paymentsList.get(position));
        }


        prefs.edit().putString("lastPaymentMethodSelected",paymentsList.get(position).getId()).apply();
        prefs.edit().putString("card_last4",paymentsList.get(position).getLast4()).apply();
        prefs.edit().putString("card_brand",paymentsList.get(position).getBrand()).apply();
    }

    @Override
    public void onDetach() {
        binding = null;
        mListener = null;
        presenter.rxUnsubscribe();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        binding = null;
        mListener = null;
        presenter.rxUnsubscribe();
        super.onDestroy();
    }

    private void makeUiChangesToShowAddNewCardLayout(){

        addCardBgLayConstrainSet = new ConstraintSet();

        addCardBgLayConstrainSet.clone(binding.payfragParent);

        addCardBgLayConstrainSet.setVisibility(binding.payfragBlurBlackBg.getId(),ConstraintSet.VISIBLE);

        binding.payfragAddNewCardContentLay.setVisibility(View.VISIBLE);

        addCardBgLayConstrainSet.constrainHeight(binding.payfragAddNewCardBgLay.getId(),ConstraintSet.WRAP_CONTENT);

        addCardBgLayConstrainSet.applyTo(binding.payfragParent);

        TransitionManager.beginDelayedTransition(binding.payfragParent);
    }

    private void makeUiChangesToHideAddNewCardLayout(){

        addCardBgLayConstrainSet.setVisibility(binding.payfragBlurBlackBg.getId(),ConstraintSet.GONE);

        addCardBgLayConstrainSet.constrainHeight(binding.payfragAddNewCardBgLay.getId(),0);

        addCardBgLayConstrainSet.applyTo(binding.payfragParent);

        TransitionManager.beginDelayedTransition(binding.payfragParent);

        binding.payfragAddNewCardContentLay.setVisibility(View.GONE);

        clearNewCardData();

    }

    private void setNewCardFieldsRules(){

        binding.payfragAddNewCardCardNumberEdt.addTextChangedListener(new TextWatcher() {

            private static final char space = ' ';

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > 0){
                    if(s.charAt(s.length() -1) == ' '){
                        s.delete(s.length() - 1, s.length());
                    }
                }

                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }

                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

                isCardNumberCompleted = s.length() == 19;

                checkToActivateAddCardButton();
            }

        });
        binding.payfragAddNewCardCardholderEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                isCardHolderCompleted = s.length() > 0;

                checkToActivateAddCardButton();
            }
        });
        binding.payfragAddNewCardDateEdt.addTextChangedListener(new TextWatcher() {

            private static final char space = '/';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > 0){
                    if(s.charAt(s.length() -1) == ' ' || s.charAt(s.length() -1) == '/'){
                        s.delete(s.length() - 1, s.length());
                    }
                }

                if (s.length() > 0 && (s.length() % 3) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }

                if (s.length() > 0 && (s.length() % 3) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 1) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

                isCardExpireDateCompleted = s.length() == 5;

                checkToActivateAddCardButton();
            }
        });
        binding.payfragAddNewCardCVCEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                isCardCvcCompleted = s.length() == 3;

                checkToActivateAddCardButton();
            }
        });
    }

    private boolean isNewCardInfoInputValid(){

        if(binding.payfragAddNewCardCardNumberEdt.getText().toString().length() < 19){
            binding.payfragAddNewCardCardNumberEdt.setText("");
            binding.payfragAddNewCardCardNumberEdt.requestFocus();
            return false;
        }

        int month = Integer.parseInt(binding.payfragAddNewCardDateEdt.getText().toString().split("/")[0]);

        int year = Integer.parseInt(binding.payfragAddNewCardDateEdt.getText().toString().split("/")[1]);

        if(binding.payfragAddNewCardDateEdt.length() < 5 || month < 1 || month > 12 || year < 21){

            binding.payfragAddNewCardDateEdt.setText("");
            binding.payfragAddNewCardDateEdt.requestFocus();
            return false;
        }

        return binding.payfragAddNewCardCVCEdt.getText().toString().length() >= 3;
    }

    private void checkToActivateAddCardButton(){
        if(isCardNumberCompleted && isCardHolderCompleted && isCardExpireDateCompleted && isCardCvcCompleted){

            binding.payfragCtaAddTheNewCardBtn.setOnClickListener(v->{

                if(isStripeCustomerCreated){

                    addNewCardToStripeCustomer();

                }else{

                    presenter.createStripeCustomer();

                }

            });

        }else{
            binding.payfragCtaAddTheNewCardBtn.setOnClickListener(null);
        }
    }

    private void clearNewCardData(){
        binding.payfragAddNewCardCardNumberEdt.setText("");
        binding.payfragAddNewCardCardholderEdt.setText("");
        binding.payfragAddNewCardDateEdt.setText("");
        binding.payfragAddNewCardCVCEdt.setText("");
    }

    @Override
    public void addNewCardToStripeCustomer() {

        if(stripeClientSecret != null){
            if(isNewCardInfoInputValid()){

                PaymentMethodCreateParams.Card card =
                        new PaymentMethodCreateParams.Card.Builder()
                                .setNumber(binding.payfragAddNewCardCardNumberEdt.getText().toString())
                                .setExpiryMonth(Integer.parseInt(binding.payfragAddNewCardDateEdt.getText().toString().split("/")[0]))
                                .setExpiryYear(Integer.parseInt(binding.payfragAddNewCardDateEdt.getText().toString().split("/")[1]))
                                .setCvc(binding.payfragAddNewCardCVCEdt.getText().toString())
                                .build();

                PaymentMethod.BillingDetails details =
                        new PaymentMethod.BillingDetails.Builder()
                                .setEmail("test@test.com")
                                .build();


                PaymentMethodCreateParams params = PaymentMethodCreateParams.create(card,
                        details);

                ConfirmSetupIntentParams confirmParams =
                        ConfirmSetupIntentParams.create(params,stripeClientSecret);

                stripe.confirmSetupIntent(this,confirmParams);



            }
        }

    }

    @Override
    public void completeAddNewCardProcedure() {

        makeUiChangesToHideAddNewCardLayout();

        presenter.getUsersPaymentMethods();
    }

    public interface PaymentFragmentListener{
        void onPaymentFragmentBackButtonClick();
        void onPaymentFragmentCashButtonClick();
        void onPaymentFragmentCardItemClick(StripePaymentMethod paymentMethod);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        stripe.onSetupResult(requestCode, data, new ApiResultCallback<SetupIntentResult>() {
            @Override
            public void onSuccess(@NotNull SetupIntentResult setupIntentResult) {
                SetupIntent setupIntent = setupIntentResult.getIntent();
                SetupIntent.Status status = setupIntent.getStatus();

                if(status == SetupIntent.Status.Succeeded){

                    Log.d("blepo","paymen id -> : "+ setupIntent.getPaymentMethodId());
                    presenter.saveUserNewPaymentMethod(setupIntent.getPaymentMethodId());

                }else if(status == SetupIntent.Status.RequiresPaymentMethod){
                    //allow retry
                    Log.d("blepo","ReqirePaymnmetMethod");
                }
            }

            @Override
            public void onError(@NotNull Exception e) {
                Log.e("blepo", "Error Sti Karta");
                e.printStackTrace();
            }
        });
    }
}

  //  PaymentMethodCreateParams.Card.Builder paymentBuilder = new PaymentMethodCreateParams.Card.Builder();
//            paymentBuilder.setNumber("4242 4242 4242 4242");
//                    paymentBuilder.setExpiryMonth(03);
//                    paymentBuilder.setExpiryYear(23);
//                    paymentBuilder.setCvc("123");
//
//                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(PaymentMethodCreateParams.create(paymentBuilder.build()), paymentIntentClientSecret);
//
