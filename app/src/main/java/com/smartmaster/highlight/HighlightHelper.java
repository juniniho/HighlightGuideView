package com.smartmaster.highlight;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by ychen on 2019/3/26.
 */
public class HighlightHelper {

    public HighlightGuideView mGuideView;
    private Context context;
    private ViewGroup mAnchor;
    private View target;

    public HighlightHelper(Context context, ViewGroup mAnchor, View target) {
        this.context = context;
        this.mAnchor = mAnchor;
        this.target = target;
        HighlightGuideView hightLightView = new HighlightGuideView(context,mAnchor,target);
        mGuideView = hightLightView;
    }

    public void show(){

        if (mAnchor instanceof FrameLayout) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) mAnchor).addView(mGuideView, ((ViewGroup) mAnchor).getChildCount(), lp);

        } else {
            FrameLayout frameLayout = new FrameLayout(context);
            ViewGroup parent = (ViewGroup) mAnchor.getParent();
            parent.removeView(mAnchor);
            parent.addView(frameLayout, mAnchor.getLayoutParams());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(mAnchor, lp);

            frameLayout.addView(mGuideView);

        }


    }

    public void remove() {
        if (mGuideView == null)
            return;
        ViewGroup parent = (ViewGroup) mGuideView.getParent();
        if (parent instanceof RelativeLayout || parent instanceof FrameLayout) {
            parent.removeView(mGuideView);
        } else {
            parent.removeView(mGuideView);
            View origin = parent.getChildAt(0);
            ViewGroup graParent = (ViewGroup) parent.getParent();
            graParent.removeView(parent);
            graParent.addView(origin, parent.getLayoutParams());
        }
        mGuideView = null;

//        sendRemoveMessage();
    }

}
