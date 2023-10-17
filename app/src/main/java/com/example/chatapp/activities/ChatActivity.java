package com.example.chatapp.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.adapters.ChatAdapter;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;
import com.example.chatapp.network.ApiClient;
import com.example.chatapp.network.ApiService;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {

    private ActivityChatBinding binding;
    private User receiveUser;

    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId =null;

    private Boolean isReceiverAvailable = false;

    private Translator englishGermanTranslator;
    private Translator frenchGermanTranslator;
    private Translator arabicGermanTranslator;
    private Translator germanGermanTranslator;


    private Translator germanEnglishTranslator;
    private Translator frenchEnglishTranslator;
    private Translator arabicEnglishTranslator;
    private Translator englishEnglishTranslator;

    private Translator translator;


    private Translator germanFrenchTranslator;
    private Translator englishFrenchTranslator;
    private Translator arabicFrenchTranslator;
    private Translator frenchFrenchTranslator;


    private Translator frenchArabicTranslator;
    private Translator englishArabicTranslator;
    private Translator germanArabicTranslator;
    private Translator arabicArabicTranslator;

    private static String detectedLanguage;




    private LanguageIdentifier languageIdentifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedString(receiveUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();


        initTranslators();



    }

    private void initTranslators(){
        // Initialize the translator
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.GERMAN)
                .build();
        englishGermanTranslator = Translation.getClient(options);


        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        languageIdentifier = LanguageIdentification.getClient();


        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.FRENCH)
                .setTargetLanguage(TranslateLanguage.GERMAN)
                .build();
        frenchGermanTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        frenchGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.FRENCH)
                .setTargetLanguage(TranslateLanguage.ARABIC)
                .build();
        frenchArabicTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        frenchArabicTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.ARABIC)
                .build();
        englishArabicTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishArabicTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.ARABIC)
                .build();
        germanArabicTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        germanArabicTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ARABIC)
                .setTargetLanguage(TranslateLanguage.GERMAN)
                .build();
        arabicGermanTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        arabicGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ARABIC)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build();
        arabicFrenchTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        arabicFrenchTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ARABIC)
                .setTargetLanguage(TranslateLanguage.ARABIC)
                .build();
        arabicArabicTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        arabicArabicTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ARABIC)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        arabicEnglishTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        arabicEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.FRENCH)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build();
        frenchFrenchTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        frenchFrenchTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.GERMAN)
                .build();
        germanGermanTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        germanGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });

        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        englishEnglishTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });



        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build();
        englishFrenchTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishFrenchTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });



        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build();
        germanFrenchTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        germanFrenchTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });



        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.FRENCH)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        frenchEnglishTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        frenchEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });



        options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        germanEnglishTranslator = Translation.getClient(options);


        conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        germanEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        // You can add your code here to handle the successful model download.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        // You can add your code here to handle the failure.
                    }
                });
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiveUser.id);
//        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());

        String messageText = binding.inputMessage.getText().toString();



        languageIdentifier.identifyLanguage(messageText)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode.equals("und")) {
                                    Log.i(TAG, "Can't identify language.");
                                } else {
                                    Log.i(TAG, "Language: " + languageCode);
//                                    System.out.println(receiveUser.language);
//                                    System.out.println(receiveUser.name);
//                                    System.out.println(receiveUser.id);

                                    detectedLanguage = languageCode;

//                                    System.out.println(detectedLanguage);

                                    translateMessage(messageText, translatedText -> {

                                        // Send the translated message
                                        message.put(Constants.KEY_MESSAGE, messageText);
                                        message.put(Constants.KEY_TRANSLATED_MESSAGE, translatedText);
                                        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
                                        if (conversionId != null) {
                                            updateConversion(messageText);
                                        } else {
                                            HashMap<String, Object> conversion = new HashMap<>();
                                            conversion.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                            conversion.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                            conversion.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                                            conversion.put(Constants.KEY_RECEIVER_ID, receiveUser.id);
                                            conversion.put(Constants.KEY_RECEIVER_LANGUAGE, receiveUser.language);
                                            conversion.put(Constants.KEY_RECEIVER_NAME, receiveUser.name);
                                            conversion.put(Constants.KEY_RECEIVER_IMAGE, receiveUser.image);
                                            conversion.put(Constants.KEY_LAST_MESSAGE, messageText);
                                            conversion.put(Constants.KEY_TIMESTAMP, new Date());
                                            addConversion(conversion);
                                        }
                                        if (!isReceiverAvailable){
                                            try {
                                                JSONArray tokens = new JSONArray();
                                                tokens.put(receiveUser.token);

                                                JSONObject data = new JSONObject();
                                                data.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                                data.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                                data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                                                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

                                                JSONObject body = new JSONObject();
                                                body.put(Constants.REMOTE_MSG_DATA, data);
                                                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                sendNotification(body.toString());
                                            }catch (Exception exception){
                                                showToast(exception.getMessage());
                                            }
                                        }
                                        binding.inputMessage.setText(null);
                                    });

                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be loaded or other internal error.
                                // ...
                            }
                        });




    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()){
                    try {
                        if (response.body() !=null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1){
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully");
                } else {
                    showToast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void translateMessage(String messageText ,OnSuccessListener<String> successListener) {
//        System.out.println(detectedLanguage);
//        System.out.println(receiveUser.language);
        if (detectedLanguage.equals("fr") && receiveUser.language.equals("English")) {
            translator = frenchEnglishTranslator;
        } else if (detectedLanguage.equals("fr") && receiveUser.language.equals("German")) {
            translator = frenchGermanTranslator;
        } else if (detectedLanguage.equals("fr") && receiveUser.language.equals("French")) {
            translator = frenchFrenchTranslator;
        } else if (detectedLanguage.equals("en") && receiveUser.language.equals("German")) {
            translator = englishGermanTranslator;
        } else if (detectedLanguage.equals("en") && receiveUser.language.equals("English")) {
            translator = englishEnglishTranslator;
        } else if (detectedLanguage.equals("en") && receiveUser.language.equals("French")) {
            translator = englishFrenchTranslator;
        } else if (detectedLanguage.equals("de") && receiveUser.language.equals("English")) {
            translator = germanEnglishTranslator;
        } else if (detectedLanguage.equals("de") && receiveUser.language.equals("French")) {
            translator = germanFrenchTranslator;
        } else if (detectedLanguage.equals("de") && receiveUser.language.equals("Arabic")) {
            translator = germanArabicTranslator;
        } else if (detectedLanguage.equals("ar") && receiveUser.language.equals("Arabic")) {
            translator = arabicArabicTranslator;
        } else if (detectedLanguage.equals("ar") && receiveUser.language.equals("German")) {
            translator = arabicGermanTranslator;
        } else if (detectedLanguage.equals("ar") && receiveUser.language.equals("French")) {
            translator = arabicFrenchTranslator;
        } else if (detectedLanguage.equals("ar") && receiveUser.language.equals("English")) {
            translator = arabicEnglishTranslator;
        } else if (detectedLanguage.equals("en") && receiveUser.language.equals("Arabic")) {
            translator = englishArabicTranslator;
        } else if (detectedLanguage.equals("fr") && receiveUser.language.equals("Arabic")) {
            translator = frenchArabicTranslator;
        } else if (detectedLanguage.equals("de") && receiveUser.language.equals("German")) {
            translator = germanGermanTranslator;
        } else {
            translator = englishEnglishTranslator;
        }
        translator.translate(messageText)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(e -> {
                    // Handle translation failure
                    // ...
                });
    }


    private void listenAvailabilityOfReceiver(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(
                receiveUser.id
        ).addSnapshotListener(ChatActivity.this, (value, error)-> {
            if (error != null){
                return;
            }
            if (value != null){
                if (value.getLong(Constants.KEY_AVAILABILITY) != null){
                    int availability = Objects.requireNonNull(
                            value.getLong(Constants.KEY_AVAILABILITY)
                    ).intValue();
                    isReceiverAvailable = availability == 1;
                }
                receiveUser.token = value.getString(Constants.KEY_FCM_TOKEN);
                if (receiveUser.image == null){
                    receiveUser.image = value.getString(Constants.KEY_IMAGE);
                    chatAdapter.setReceiverProfileImage(getBitmapFromEncodedString(receiveUser.image));
                    chatAdapter.notifyItemRangeChanged(0, chatMessages.size());
                }
            }
            if (isReceiverAvailable){
                binding.textAvailability.setVisibility(View.VISIBLE);
            } else {
                binding.textAvailability.setVisibility(View.GONE);
            }

        });
    }

    private void listenLanguage(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(
                receiveUser.id
        ).addSnapshotListener(ChatActivity.this, (value, error)-> {
            if (error != null){
                return;
            }
            if (value != null){
                if (value.getString(Constants.KEY_LANGUAGE) != null){
                    String language = Objects.requireNonNull(
                            value.getString(Constants.KEY_LANGUAGE)
                    );
                    receiveUser.language = language;
                    System.out.println(language);
                    System.out.println(receiveUser.language);
                }
            }
            if (isReceiverAvailable){
                binding.textAvailability.setVisibility(View.VISIBLE);
            } else {
                binding.textAvailability.setVisibility(View.GONE);
            }
        });
    }
    private void listenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiveUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiveUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        }
        if (value != null){
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.translatedMessage = documentChange.getDocument().getString(Constants.KEY_TRANSLATED_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversionId == null){
            checkForConversion();
        }
    };

    private Bitmap getBitmapFromEncodedString(String encodedImage){
        if (encodedImage != null){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
        } else {
            return null;
        }
    }

    private void loadReceiverDetails(){
        receiveUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiveUser.name);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> {
            sendMessage();
            Log.d("ChatActivity", "sendMessage() called"); // Add this log statement
        });
        binding.imageInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            intent.putExtra("receiverProfile", receiveUser);
            startActivity(intent);
        });
    }


    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String , Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message){
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversion(){
        if (chatMessages.size() != 0){
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiveUser.id
            );
            checkForConversionRemotely(
                    receiveUser.id,
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
        listenLanguage();
    }
}