package com.smartmaster.highlight;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhy.highlight.R;

/**
 * Created by ychen on 2019/3/26.
 */
public class TestActivity extends Activity {

    private Button btn;
    private LinearLayout ll_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.btn);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGuide();
            }
        });

        showGuide();



    }

    public void onClickBt2(View view){
        Toast.makeText(getApplicationContext(),"clickBt2",Toast.LENGTH_SHORT).show();
    }

    private void showGuide(){
        final HighlightUtil highlightUtil = new HighlightUtil(this,ll_root,btn);
        highlightUtil.show();
        highlightUtil.mGuideView.post(new Runnable() {
            @Override
            public void run() {
                highlightUtil.mGuideView.addTipView(R.layout.view_init_data_tip);
                highlightUtil.mGuideView.addBottomView(R.layout.guide_tip_view_complete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        highlightUtil.remove();
                    }
                });


            }
        });
    }

}
