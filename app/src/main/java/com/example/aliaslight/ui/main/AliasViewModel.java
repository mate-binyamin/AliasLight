package com.example.aliaslight.ui.main;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aliaslight.AliasModel;

public class AliasViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    AliasModel model;

    MutableLiveData<String>
            alias = new MutableLiveData<>()
            , explanations = new MutableLiveData<>();

    public AliasViewModel(Application app) {
        super(app);
        model = new AliasModel(app);
    }
}



