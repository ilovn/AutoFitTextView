package com.ilovn.libs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;


/**
 * 自适应Textview
 */
public class AutoFitTextView extends TextView {
    private final static String TAG = "AutoFitTextView";

    public AutoFitTextView(Context context) {
        super(context);
        clacAutoFitValue();
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        clacAutoFitValue();
    }

    public AutoFitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        clacAutoFitValue();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoFitTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        clacAutoFitValue();
    }

    /**
     * 计算相关参数
     */
    private void clacAutoFitValue() {
        // Log.d(TAG, "Content: " + getText().toString());
        // Log.d(TAG, "Width: " + getWidth() + " Height: " + getHeight());
        TextPaint textPaint = getPaint();
        final float textPaintWidth = textPaint.measureText(getText().toString());
        // Log.d(TAG, "TextPaontWidth: " + textPaintWidth);
//        Log.d(TAG, "\" \"Width: " + textPaint.measureText(" "));
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // Log.d(TAG, "onGlobalLayout Width: " + getWidth() + " Height: " + getHeight());
                if (getText() != null && getText().toString().length() > 1) {
                    applyAutoFit(textPaintWidth / getText().toString().length(), getText().toString(), getWidth());
                }
            }
        });
    }

    /**
     * 应用计算后的间距补全
     * @param singleCharWidth
     * @param str
     * @param viewWidth
     */
    private void applyAutoFit(final float singleCharWidth, final String str, final int viewWidth) {
//        Log.d(TAG, "SingleCharWidth: " + singleCharWidth + " StringContent: " + str + " ViewWidth: " + viewWidth);
        TextPaint textPaint = getPaint();
        final float spacePaintWidth = textPaint.measureText(" ");
//        Log.d(TAG, "\" \"Width: " + spacePaintWidth);
        int singleLineCharCount = (int) (viewWidth / singleCharWidth);
//        Log.d(TAG, "SingleLineCharCount: " + singleLineCharCount);
        int needAppendChars = singleLineCharCount - str.length();
//        Log.d(TAG, "NeedAppendChars: " + needAppendChars);
        int needAppendSpaces = needAppendChars * ((int) (singleCharWidth / spacePaintWidth));
//        Log.d(TAG, "NeedAppendSpaces: " + needAppendSpaces);
        int stage = str.length() - 1;
        int needSpaceOfStage = needAppendSpaces / stage;
        int needSpaceOfStage2 = needAppendSpaces % stage;
//        Log.d(TAG, "NeedSpaceOfStage: " + needSpaceOfStage + " NeedSpaceOfStage2: " + needSpaceOfStage2);

        int spaces = (int)(viewWidth - singleCharWidth * str.length());
//        Log.d(TAG, "**: " + spaces);
//        Log.d(TAG, "***: " + spaces/spacePaintWidth + " ***: " + spaces%spacePaintWidth);
//        Log.d(TAG, "getLetterSpacing: "+getLetterSpacing());
//        if (spaces%spacePaintWidth!=0) {
//            float letterSpacing = (spaces%spacePaintWidth)/singleLineCharCount;
//            Log.d(TAG, "SetLetterSpacing: " + letterSpacing);
//            setLetterSpacing(DisplayUtil.px2sp(getContext(), letterSpacing));
//        }

        StringBuffer stringBuffer = new StringBuffer(str.substring(0,1));
//        if (str.length() == 2) {
//            for (int i = needSpaceOfStage; i > 0; i--) {
//                stringBuffer.append(" ");
//            }
//        } else {
        int n = 0;
            for (int i = 0; i < stage; i ++) {
                for (int j = needSpaceOfStage; j > 0; j--) {
                    stringBuffer.append(" ");
                }
                if (needSpaceOfStage2!=0&&n < needSpaceOfStage2){
                    n++;
                    stringBuffer.append(" ");
                }
                if (i < str.length()) {
//                    if ((i == str.length() - 1) && needSpaceOfStage2!=0) {
//                        for (int k = 0; k < needSpaceOfStage2; k++){
//                            stringBuffer.append(" ");
//                        }
//                    }
                    stringBuffer.append(str.substring(i + 1, i + 2));
                }
            }
//        }
//        stringBuffer.append(str.substring(str.length()-1));
//        Log.d(TAG, "Content: " + stringBuffer.toString());
        setText(stringBuffer.toString());

    }


}
