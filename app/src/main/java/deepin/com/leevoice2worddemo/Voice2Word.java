package deepin.com.leevoice2worddemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import java.util.List;

public class Voice2Word extends Activity {
    Button mButton;
    TextView mResultShowTextView;
    private String dictationResultStr;
    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口(返回Json格式结果)；
        // 比如：{"sn":1,"ls":false,"bg":0,"ed":0,"ws":[{"bg":0,"cw":[{"sc":0.00,"w":"我"}]},{"bg":0,"cw":[{"sc":0.00,"w":"是"}]},{"bg":0,"cw":[{"sc":0.00,"w":"万里长城"}]}]}
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.i("leeTest", "RecognizerResult ---> " + results.getResultString());
            if (!isLast) {
                dictationResultStr += results.getResultString() + ",";
            } else {
                dictationResultStr += results.getResultString() + "]";
            }

            // 如果是最后一个，进行解析
            if (isLast) {
                // 解析Json列表字符串
                Gson gson = new Gson();
                List<Voide2WordsResult> voide2WordsResultList = gson
                        .fromJson(dictationResultStr,
                                new TypeToken<List<Voide2WordsResult>>() {
                                }.getType());

                String finalResult = "";
                for (int i = 0; i < voide2WordsResultList.size() - 1; i++) {
                    finalResult += voide2WordsResultList.get(i)
                            .toString();
                }
                Log.i("leeTest", "last result ---> " + finalResult);
                mResultShowTextView.setText(finalResult);
            }
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
            error.getPlainDescription(true); //获取错误码描述
        }

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音
        public void onBeginOfSpeech() {
        }

        //音量值0~30
        public void onVolumeChanged(int volume) {
        }

        //结束录音
        public void onEndOfSpeech() {
        }

        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
    private SpeechRecognizer mIat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speach2_word);
        SpeechUtility.createUtility(Voice2Word.this, SpeechConstant.APPID + "=5931728f");

        ButtonListener b = new ButtonListener();
        mButton = (Button) findViewById(R.id.button_voice_2_word);
        mButton.setOnClickListener(b);
        mButton.setOnTouchListener(b);
        mButton.setText("按住识别语音");
        mResultShowTextView = findViewById(R.id.id_show_voice_2_words_result);

        // 1 创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(Voice2Word.this, null);
        // 2 设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
    }

    class ButtonListener implements OnClickListener, View.OnTouchListener {

        public void onClick(View v) {
            if (v.getId() == R.id.button_voice_2_word) {
                Log.d("leeTest", "cansal button ---> click");
            }
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.button_voice_2_word) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("leeTest", "cansal button ---> ACTION_DOWN");
                    mButton.setText("录制并识别中...");

                    //3.开始听写
                    dictationResultStr = "[";
                    mIat.startListening(mRecoListener);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("leeTest", "cansal button ---> ACTION_UP");
                    mButton.setText("按住识别语音");


                    mIat.stopListening();
                }

            }
            return false;
        }
    }
}
