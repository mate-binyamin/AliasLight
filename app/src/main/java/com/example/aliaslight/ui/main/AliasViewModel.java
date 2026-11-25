package com.example.aliaslight.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aliaslight.GeminiCallback;
import com.example.aliaslight.GeminiManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AliasViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
//    AliasModel model;
    GeminiManager gm;

    private MutableLiveData<String>
            alias = new MutableLiveData<>();

    private MutableLiveData<List<String>>
            hints = new MutableLiveData<>();

    private MutableLiveData<Boolean> success = new MutableLiveData<>();

    private ArrayList<String> hintsFull;
    private int i = 0;


    public AliasViewModel(Application app) {
        super(app);
//        model = new AliasModel(app);
        gm = GeminiManager.getInstance(app);
        gm.startChat();
        nextAlias();
    }

    public void nextHint() {
//        if (i >= hintsFull.size()) { // no more hints!
//            moreHints();
//            return;
//        }

        if (i < hintsFull.size()) { // note - if there are hints on the way - this will not execute
            String nextHint = hintsFull.get(i++);
            hints.getValue().add(nextHint);

            if (i == hintsFull.size()) { // finished all hints!
                moreHints();
            }
        }
    }

    private void moreHints() {
        gm.sendChatMessage("עוד רמזים", new GeminiCallback() {
            @Override
            public void onSuccess(String result) {
                String[] lines = result.split(NL_REGEX);
                addHints(lines);
            }

            @Override
            public void onError(Throwable error) {
                Log.e("MORE FAILED", error.toString());
            }
        });
    }

    public void check(String alias) {
        if (alias.equals(this.alias.getValue())) { // SUCCESS
//            model.nextAlias(); TODO
            success.setValue(true);
            nextAlias();
        } else {
            success.setValue(false);
        }
    }

    private void nextAlias() {
        gm.sendChatMessage("הבא בתור", new GeminiCallback() {
            @Override
            public void onSuccess(String result) {
                success.setValue(null);
                Log.d("ALIAS AND HINTS", result);
                String[] lines = result.split(NL_REGEX);
                alias.setValue(lines[0]);
                setNewHints(Arrays.copyOfRange(lines, 1, lines.length));
            }

            @Override
            public void onError(Throwable error) {
                success.setValue(null);
                Log.e("NEXT FAILED", error.toString());
            }
        });
    }

    private void setNewHints(String[] hints) {
        hintsFull.clear();
        addHints(hints);
        nextHint();
    }

    private void addHints(String[] hints) {
        hintsFull.addAll(Arrays.asList(hints));
    }

    public LiveData<String> getAlias() {
        return alias;
    }

    public LiveData<List<String>> getHints() {
        return hints;
    }

    public LiveData<Boolean> getSuccess() {
        return success;
    }


    private static final String NL_REGEX = "\n";
}



