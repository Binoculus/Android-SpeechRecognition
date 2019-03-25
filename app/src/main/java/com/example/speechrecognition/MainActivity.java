package com.example.speechrecognition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button speechBut;
    private TextView result;
    private SpeechRecognizer mSpeechRecognizer;
    private String string = "what's your name";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speechBut:
                doSpeechRecognition(v);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        OpenJurisdiction为权限开启
        final OpenJurisdiction openJurisdiction = new OpenJurisdiction();
        openJurisdiction.open(MainActivity.this);

        this.speechBut= findViewById(R.id.speechBut);
        this.speechBut.setOnClickListener(this);
        this.result= findViewById(R.id.result);
        this.mSpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        this.mSpeechRecognizer.setRecognitionListener(new MyRecognitionListener());
    }

    public void doSpeechRecognition(View view){
        view.setEnabled(false);
        Intent recognitionIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"zh-CN");
        this.mSpeechRecognizer.startListening(recognitionIntent);
    }

    private class MyRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i("onBeginningOfSpeech","onBeginningOfSpeech");
            result.setText("");
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Log.i("识别结束","语音识别结束标志");
            speechBut.setEnabled(true);
        }

        @Override
        public void onError(int error) {
            Log.e("onError", String.valueOf(error));
            if (!isNetworkAvailable(MainActivity.this)){
                Toast.makeText(MainActivity.this,"未开启网络权限，无法进行语音识别",Toast.LENGTH_SHORT).show();
            }
            speechBut.setEnabled(true);
        }

        @Override
        public void onResults(Bundle results) {
            Log.i("onResults","onResults");
            ArrayList<String> partialResults=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(partialResults!=null && partialResults.size()>0){
                String bestResult=partialResults.get(0);
                bestResult = bestResult.substring(0,bestResult.length()-1);
                result.setText(bestResult+"");
                if (bestResult.equals(string)){
                    Toast.makeText(MainActivity.this,"正确",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.i("onPartialResults","onPartialResults");
            ArrayList<String> partialResults=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(partialResults!=null && partialResults.size()>0){
                String bestResult=partialResults.get(0);
                bestResult = bestResult.substring(0,bestResult.length()-1);
                result.setText(bestResult);
                if (bestResult.equals(string)){
                    Toast.makeText(MainActivity.this,"正确",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mSpeechRecognizer.destroy();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
