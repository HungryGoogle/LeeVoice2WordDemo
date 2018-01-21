package deepin.com.leevoice2worddemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
//import com.iflytek.sunflower.FlowerCollector;

public class Word2Voice extends Activity {

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if(error == null)
            {
            }
            else if(error != null)
            {
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer="xiaoyan";
    private EditText editTextUserInputWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_2_voice);

        SpeechUtility.createUtility(Word2Voice.this, SpeechConstant.APPID +"=5931728f");
        mTts = SpeechSynthesizer.createSynthesizer(Word2Voice.this, null);

        mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED,"50");
        mTts.setParameter(SpeechConstant.VOLUME,"80");
        editTextUserInputWord = findViewById(R.id.id_input_text);
        findViewById(R.id.id_syncronized).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 初始化合成对象
                String strInput = editTextUserInputWord.getText().toString();
                if(TextUtils.isEmpty(strInput)){
                    strInput = "请输入需要读的文字";
                }
                mTts.startSpeaking(strInput, mTtsListener);    //开始语音合成，现在合成的是“科大讯飞”
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTts.stopSpeaking();
    }
    @Override
    protected void onResume() {
        //移动数据统计分析
        FlowerCollector.onResume(this);
        FlowerCollector.onPageStart("TtsDemo");
        super.onResume();
    }
    @Override
    protected void onPause() {
        //移动数据统计分析
        FlowerCollector.onPageEnd("TtsDemo");
        FlowerCollector.onPause(this);
        super.onPause();
    }
}
