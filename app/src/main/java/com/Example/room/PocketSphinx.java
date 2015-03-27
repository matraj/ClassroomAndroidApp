package com.Example.room;

/**
 * Created by Mat on 3/22/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.plugins.unity.UnityCardboardActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class PocketSphinx implements RecognitionListener{
    private static final String TAG = "MyActivity";

    private static final String KWS_SEARCH = "wakeup";
    private static final String KWS_STOP = "sleep";
    private static final String FORECAST_SEARCH = "forecast";
    private static final String STOPPHRASE = "stop";
    private static final String DIGITS_SEARCH = "presentation";
    private static final String MENU_SEARCH = "menu";
    private static final String KEYPHRASE = "start";

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    private HashMap<String, Integer> wordCount = new HashMap<String, Integer>();

    public static final String MyPREFS = "MyPref";
    public static final String myWords = "Words";
    SharedPreferences sharedpreferences;

    Context activityContext;
    MainActivity mainClass;

    public PocketSphinx(MainActivity mainActivity)
    {
        activityContext = mainActivity;
        mainClass = mainActivity;
    }

    public void prepareForSpeech() {

        // Prepare the data for UI
        captions = new HashMap<String, Integer>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(KWS_STOP, R.string.kws_stop_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(DIGITS_SEARCH, R.string.digits_caption);
        captions.put(FORECAST_SEARCH, R.string.forecast_caption);
        makeText(activityContext, "Preparing the recognizer", Toast.LENGTH_SHORT).show();

        sharedpreferences = activityContext.getSharedPreferences(MyPREFS, Context.MODE_PRIVATE);
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(activityContext);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    makeText(activityContext, "Failed to init recognizer " + result, Toast.LENGTH_SHORT).show();
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        String text = hypothesis.getHypstr();
        String[] words = text.split(" ");
        for (int i=0; i < words.length; i++) {
            Log.d(TAG, "onPartialResult");
            if (text.equals(KEYPHRASE))
                switchSearch(MENU_SEARCH);
            else if (text.equals(STOPPHRASE)) {
                endPresentation();
            }
            else {
                switchSearch(DIGITS_SEARCH);
//            ((TextView) findViewById(R.id.result_text)).setText(text);
                makeText(activityContext, text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void endPresentation() {
        makeText(activityContext, "STOPPPP Pls", Toast.LENGTH_SHORT).show();
//        ((MyApplication) mainClass.getApplication()).setCrutchWordCount(wordCount);
        saveMap(wordCount);
//        switchSearch(KWS_SEARCH);
        recognizer.stop();
        Toast.makeText(activityContext, "Audio recorded successfully",
                Toast.LENGTH_LONG).show();

        UnityCardboardActivity.getActivity().onBackPressed();
        mainClass.stopPresentation();
//        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
//        MainActivity.this.startActivity(mainIntent);
    }

    private void saveMap(HashMap<String,Integer> inputMap){
        if (sharedpreferences != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Editor editor = sharedpreferences.edit();
            editor.remove(myWords).commit();
            editor.putString(myWords, jsonString);
            editor.commit();
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
//        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();

            String[] words = text.split(" ");
            for (int i=0; i < words.length; i++) {
                Integer count = wordCount.get(words[i]);
                if (count != null) {
                    wordCount.put(words[i], count + 1);
                } else {
                    wordCount.put(words[i], 1);
                }
            }
            makeText(activityContext, text, Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onResult");
        Log.d(TAG, "WORD COUNT SON: " + wordCount);
    }

    @Override
    public void onBeginningOfSpeech() { Log.d(TAG, "onBegginingof Speech");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndofSpeech");
//        if (DIGITS_SEARCH.equals(recognizer.getSearchName()))
            switchSearch(DIGITS_SEARCH);
    }

    private void switchSearch(String searchName) {
        Log.d(TAG, "onSWITCHSEARCh");
        recognizer.stop();
        recognizer.startListening(searchName);
        String caption = activityContext.getResources().getString(captions.get(searchName));
//        ((TextView) findViewById(R.id.caption_text)).setText(caption);
        makeText(activityContext, caption, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onBufferReceived(byte[] buffer) {
//        // TODO Auto-generated method stub
//
//    }

    private void setupRecognizer(File assetsDir) {
        File modelsDir = new File(assetsDir, "models");
        recognizer = defaultSetup()
                .setAcousticModel(new File(modelsDir, "hmm/en-us-semi"))
                .setDictionary(new File(modelsDir, "dict/cmu07a.dic"))
                .setRawLogDir(assetsDir).setKeywordThreshold(1e-0f)
                .getRecognizer();
        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        recognizer.addKeyphraseSearch(KWS_STOP, STOPPHRASE);
        // Create grammar-based searches.
        File menuGrammar = new File(modelsDir, "grammar/menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);
        File digitsGrammar = new File(modelsDir, "grammar/digits.gram");
        recognizer.addKeywordSearch(DIGITS_SEARCH, digitsGrammar);
        // Create language model search.
//        File languageModel = new File(modelsDir, "lm/weather.dmp");
//        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
    }
}
