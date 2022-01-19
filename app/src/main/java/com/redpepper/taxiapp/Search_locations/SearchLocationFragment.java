package com.redpepper.taxiapp.Search_locations;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.redpepper.taxiapp.databinding.FragmentSearchLocationListBinding;
import com.redpepper.taxiapp.Root.App;
import com.redpepper.taxiapp.Search_locations.Adapters.FoursquareSearchAdapter;
import com.redpepper.taxiapp.Search_locations.Adapters.GoogleSearchAdapter;
import com.redpepper.taxiapp.Search_locations.Models.FoursquarePlace;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

public class SearchLocationFragment extends Fragment implements SearchLocationsMVP.View,
        GoogleSearchAdapter.ItemClickListener,
        FoursquareSearchAdapter.ItemClickListener {

    private FragmentSearchLocationListBinding binding;

    private SearchLocationFragmentActionsListener listener;

    private ArrayList<AutocompletePrediction> predictions;

    private ArrayList<FoursquarePlace> foursquarePlacesList;

    private String titleText;

    private LatLng pickUpLocation;

    @Inject
    SearchLocationsMVP.Presenter presenter;


    public SearchLocationFragment() {
    }

    public static SearchLocationFragment newInstance(int columnCount) {
        SearchLocationFragment fragment = new SearchLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("blepo","SearchFragment: OnCreate()");

        if (getArguments() != null) {

           titleText = getArguments().getString("title");

           pickUpLocation = new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        }

        presenter.setView(this);

        predictions = new ArrayList<>();



    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("blepo","SearchFragment: OnCreateView()");

        binding = FragmentSearchLocationListBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        binding.serFragTitleTxt.setText(titleText);

        binding.serFragAddressToSearchEditText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                presenter.performFoursquareSearch(pickUpLocation);

                return true;
            }

            return false;
        });

        binding.serFragAddressToSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int charLength = binding.serFragAddressToSearchEditText.length();

                if(charLength >= 5){

                    presenter.performSearch(binding.serFragAddressToSearchEditText.getText().toString(),new LatLng(38.036870,23.670870));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.serFragClearTextButton.setOnClickListener(v-> binding.serFragAddressToSearchEditText.setText(""));

        binding.serFragFavoriteRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                binding.serFragHomeRadio.setChecked(false);
                binding.serFragWorkRadio.setChecked(false);

                binding.serFragListTitleTextview.setText("Favorite");

                showList();
            }


        });

        binding.serFragHomeRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                binding.serFragFavoriteRadio.setChecked(false);
                binding.serFragWorkRadio.setChecked(false);
            }

            binding.serFragListTitleTextview.setText("Home");

            showList();

        });

        binding.serFragWorkRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                binding.serFragHomeRadio.setChecked(false);
                binding.serFragFavoriteRadio.setChecked(false);
            }

            binding.serFragListTitleTextview.setText("Job");

            showList();



        });

        binding.serFragBackButton.setOnClickListener(v -> listener.onSearchFragmentBackButtonPressed());



        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d("blepo","SearchFragment: OnAttach()");

        ((App) context.getApplicationContext()).getComponent().inject(this);

        if(context instanceof SearchLocationFragmentActionsListener){
            listener = (SearchLocationFragmentActionsListener) context;
        }else{
            throw new RuntimeException(context.toString()
            + " must implement SearchLocationFragmentActionsListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("blepo","SearchFragment: OnDetach()");
        listener=null;
        presenter.rxUnsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("blepo","SearchFragment: OnDestroyView()");

        InputMethodManager lInputMethodManager =
                (InputMethodManager) binding.serFragAddressToSearchEditText.getContext()
                .getSystemService( Context.INPUT_METHOD_SERVICE );

        lInputMethodManager.hideSoftInputFromWindow(
                binding.serFragAddressToSearchEditText.getWindowToken() , 0 );

        binding = null;
    }

    private void showList(){
        TransitionManager.beginDelayedTransition(binding.serFragparent);

        ConstraintSet constraintSet = new ConstraintSet();

        constraintSet.clone(binding.serFragparent);

        constraintSet.setVisibility(binding.serFragListContainer.getId(),ConstraintSet.VISIBLE);

        constraintSet.applyTo(binding.serFragparent);
    }

    private void hideList(){
        TransitionManager.beginDelayedTransition(binding.serFragparent);

        ConstraintSet constraintSet = new ConstraintSet();

        constraintSet.clone(binding.serFragparent);

        constraintSet.setVisibility(binding.serFragListContainer.getId(),ConstraintSet.GONE);

        constraintSet.applyTo(binding.serFragparent);
    }

    @Override
    public void populateSearchListWithGoogleResults(ArrayList<AutocompletePrediction> list) {

        hideList();

        predictions = list;

        GoogleSearchAdapter adapter = new GoogleSearchAdapter(list,this);

        RecyclerView.LayoutManager recViewManager = new LinearLayoutManager(getActivity());

        binding.serFragRecyclerView.setLayoutManager(recViewManager);

        binding.serFragRecyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.serFragRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        binding.serFragRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        binding.serFragListTitleTextview.setText("Search Results");

        showList();

    }

    @Override
    public void populateSearchListWithFoursquareResults(ArrayList<FoursquarePlace> foursquarePlacesList) {

        hideList();

        this.foursquarePlacesList = foursquarePlacesList;

        FoursquareSearchAdapter adapter = new FoursquareSearchAdapter(foursquarePlacesList,this);

        RecyclerView.LayoutManager recViewManager = new LinearLayoutManager(getActivity());

        binding.serFragRecyclerView.setLayoutManager(recViewManager);

        binding.serFragRecyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.serFragRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        binding.serFragRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        binding.serFragListTitleTextview.setText("Search Results");

        showList();

    }

    @Override
    public void onGoogleItemClick(View v, int position) {
        presenter.getGooglePlace(predictions.get(position).getPlaceId());
    }

    @Override
    public void onFoursquareItemClick(View v, int position) {
        if(listener != null){
            listener.onFoursquareResultItemClick(foursquarePlacesList.get(position));
        }
    }

    @Override
    public void googlePlaceWasRetrieved(Place place) {
        if(listener != null){
            listener.onGoogleResultItemClick(place);
        }
    }

    public interface SearchLocationFragmentActionsListener{
        void onSearchFragmentBackButtonPressed();
        void onGoogleResultItemClick(Place place);
        void onFoursquareResultItemClick(FoursquarePlace place);
    }
}