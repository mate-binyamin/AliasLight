package com.example.aliaslight;

import android.content.Context;

public class AliasModel {
    GeminiManager gm;

    public AliasModel(Context context) {
        gm = GeminiManager.getInstance(context);

        // TODO talk to gemini
    }

    public void nextAlias(GeminiCallback callback) {
        // TODO fetch new alias from gemini
    }
}
