package com.smartmaster.highlight;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhy.highlight.R;

/**
 * Created by ychen on 2019/3/26.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn = (Button) findViewById(R.id.btn);
        LinearLayout ll_root = (LinearLayout) findViewById(R.id.ll_root);

        new HighlightUtil(this,ll_root,btn).show();

    }
}
