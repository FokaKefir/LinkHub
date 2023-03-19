package com.fokakefir.linkhub.gui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.model.FilterOps;

import java.util.ArrayList;

public class FilterDialog extends AppCompatDialogFragment implements View.OnClickListener {

    TextView txtAccomodation;
    Switch accomodationSwitch;

    TextView txtAdult;
    Switch adultSwitch;

    TextView txtAmusement;
    Switch amusementSwitch;

    TextView txtarchitecture;
    Switch architectureSwitch;

    TextView txtCultural;
    Switch culturalSwitch;

    TextView txtFoods;
    Switch foodsSwitch;

    TextView txtHistorical;
    Switch historicalSwitch;

    TextView txtNatural;
    Switch natuarlSwitch;



    TextView txtReligion;
    Switch religionSwitch;

    TextView txtShops;
    Switch shopSwitch;

    TextView txtSport;
    Switch sportSwitch;

    Button btnBack;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_filter, null);

        // region find by id

        this.txtAccomodation = view.findViewById(R.id.txt_filter_Accomodation);
        this.accomodationSwitch = view.findViewById(R.id.switch_filter_Accomodation);

        this.txtAdult = view.findViewById(R.id.txt_filterAdult);
        this.adultSwitch = view.findViewById(R.id.switch_filter_Adult);

        this.txtAmusement = view.findViewById(R.id.txt_filterAmusements);
        this.amusementSwitch = view.findViewById(R.id.switch_filter_Amusements);

        this.txtarchitecture = view.findViewById(R.id.txt_filterArchitecture);
        this.architectureSwitch = view.findViewById(R.id.switch_filter_Architecture);

        this.txtCultural = view.findViewById(R.id.txt_filter_Cultural);
        this.culturalSwitch = view.findViewById(R.id.switch_filter_cultural);

        this.txtFoods = view.findViewById(R.id.txt_filterFoods);
        this.foodsSwitch = view.findViewById(R.id.switch_filter_Foods);

        this.txtHistorical = view.findViewById(R.id.txt_filterHistorical);
        this.historicalSwitch = view.findViewById(R.id.switch_filter_historical);

        this.txtNatural = view.findViewById(R.id.txt_filterNatural);
        this.natuarlSwitch = view.findViewById(R.id.switch_filter_Natural);

        this.txtReligion = view.findViewById(R.id.txt_filterReligion);
        this.religionSwitch = view.findViewById(R.id.switch_filter_Religion);

        this.txtShops = view.findViewById(R.id.txt_filterShops);
        this.shopSwitch = view.findViewById(R.id.switch_filter_Shops);

        this.txtSport = view.findViewById(R.id.txt_filterSport);
        this.sportSwitch = view.findViewById(R.id.switch_filter_Sport);

        // endregion


        this.btnBack = view.findViewById(R.id.btn_filter_back);

        this.btnBack.setOnClickListener(this);

        this.accomodationSwitch.setChecked(true);
        this.adultSwitch.setChecked(true);
        this.amusementSwitch.setChecked(true);
        this.architectureSwitch.setChecked(true);
        this.culturalSwitch.setChecked(true);
        this.foodsSwitch.setChecked(true);
        this.historicalSwitch.setChecked(true);
        this.natuarlSwitch.setChecked(true);
        this.religionSwitch.setChecked(true);
        this.shopSwitch.setChecked(true);
        this.sportSwitch.setChecked(true);

        loadAtributes();


        builder.setView(view);

        return builder.create();


    }

    @Override
    public void onClick(View view) {
            if(view.getId() == R.id.btn_filter_back){

                verifAtributes();
                dismiss();
            }

    }
    private void verifAtributes(){

        FilterOps.getInstance().getKinds().clear();
        FilterOps.getInstance().getExclude().clear();
        FilterOps.getInstance().getRatings().clear();

        if(accomodationSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("accomodations");
        }else{
            FilterOps.getInstance().getExclude().add("accomodations");
        }

        if(adultSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("adult");
        }
        else{
            FilterOps.getInstance().getExclude().add("adult");
        }

        if(amusementSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("amusements");
        }
        else {
            FilterOps.getInstance().getExclude().add("amusements");
        }
        if(architectureSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("architecture");
        }
        else{
            FilterOps.getInstance().getExclude().add("architecture");
        }

        if(culturalSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("cultural");
        }
        else{
            FilterOps.getInstance().getExclude().add("cultural");
        }

        if(foodsSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("foods");
        }
        else{
            FilterOps.getInstance().getExclude().add("foods");
        }

        if(historicalSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("historic");
        }
        else {
            FilterOps.getInstance().getExclude().add("historic");
        }
        if(natuarlSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("natural");
        }
        else{
            FilterOps.getInstance().getExclude().add("natural");
        }
        if(religionSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("religion");
        }
        else{
            FilterOps.getInstance().getExclude().add("religion");
        }
        if(sportSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("sport");
        }
        else{
            FilterOps.getInstance().getExclude().add("sport");
        }
        if(shopSwitch.isChecked()){
            FilterOps.getInstance().getKinds().add("shops");
        }
        else{
            FilterOps.getInstance().getExclude().add("shops");
        }



    }


    private Switch  getIdByName(String name){
        switch (name){
            case "accomodations": return accomodationSwitch;
            case "adult" : return adultSwitch;
            case "amusements" : return  amusementSwitch;
            case "architecture" : return architectureSwitch;
            case "cultural" : return culturalSwitch;
            case "foods" : return foodsSwitch;
            case "historic" : return historicalSwitch;
            case "natural" : return natuarlSwitch;
            case "religion" : return religionSwitch;
            case "sport"  : return sportSwitch;
            case "shops" : return shopSwitch;
            default: return null;

        }
    }
    private void loadAtributes(){

        for (int i = 0; i < FilterOps.getInstance().getKinds().size(); i++) {

            getIdByName(FilterOps.getInstance().getKinds().get(i)).setChecked(true);
        }
        for (int i = 0; i < FilterOps.getInstance().getExclude().size(); i++) {
            getIdByName(FilterOps.getInstance().getExclude().get(i)).setChecked(false);
        }
    }

}
