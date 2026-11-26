package com.example.aliaslight;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.ai.client.generativeai.Chat;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.ImagePart;
import com.google.ai.client.generativeai.type.Part;
import com.google.ai.client.generativeai.type.RequestOptions;
import com.google.ai.client.generativeai.type.TextPart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class GeminiManager {
    private static final String MODEL_VERSION = "gemini-3.5-flash-lite";

    private static GeminiManager instance;
    private GenerativeModel gemini;
    private Chat chat;

    /**
     *
     * @param API_KEY the key to using ai services by google
     * @param systemPrompt a constant background for the AI to be accompanied by every prompt
     */
    private GeminiManager(String API_KEY, String systemPrompt) {
        List<Part> parts = new ArrayList<>();
        parts.add(new TextPart(systemPrompt));
        // init
        gemini = new GenerativeModel(
                MODEL_VERSION,
                API_KEY,
                null,
                null,
                new RequestOptions(),
                null,
                null,
                /* system prompt */ new Content(parts)
        );
    }

    public static GeminiManager getInstance(Context context) {
        if (instance == null) {
            try {
                Scanner sc = new Scanner(context.getAssets().open("api_key"));
                String key = sc.nextLine();
                sc.close();
//                Log.d("KEY", key);
                String systemPrompt = context.getString(R.string.ai_context);
                Log.d("SYS_PROMPT", systemPrompt);
                instance = new GeminiManager(key, systemPrompt);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        return instance;
    }

    public void sendMessage(String prompt, GeminiCallback gcb) {
        gemini.generateContent(prompt,
                new Continuation<GenerateContentResponse>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NonNull Object result) {
                        handleResult(result, gcb);
                    }
                });
    }

    public void sendMessageWithPhoto(String prompt, Bitmap photo, GeminiCallback callback) {
        List<Part> parts = new ArrayList<>();
        parts.add(new TextPart(prompt));
        parts.add(new ImagePart(photo));

        Content[] content = new Content[1];
        content[0] = new Content(parts);

        gemini.generateContent(content,
                new Continuation<GenerateContentResponse>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return  EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NonNull Object result) {
                        handleResult(result, callback);
                    }
                }
        );

    }

    private static void handleResult(Object result, GeminiCallback callback) {
        if (result instanceof Result.Failure) {
            callback.onError(((Result.Failure)result).exception);
        } else {
            callback.onSuccess(((GenerateContentResponse)result).getText());
        }
    }

    public void startChat() {
        chat = gemini.startChat(Collections.emptyList());
        Log.d("CHAT", chat.toString());

    }

    public void sendChatMessage(String prompt, CoroutineContext context, GeminiCallback callback) {
        chat.sendMessage(prompt, new Continuation<GenerateContentResponse>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return context;
            }

            @Override
            public void resumeWith(@NonNull Object result) {
                handleResult(result, callback);
            }
        });
    }

    public void sendChatMessage(String prompt, GeminiCallback callback) {
        sendChatMessage(
                prompt,
                EmptyCoroutineContext.INSTANCE,
                callback
        );
    }

//    // private constructor
//    private GeminiManager() {
//
//    }

}
