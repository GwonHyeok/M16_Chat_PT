package com.hyeok.m16_chat_pt.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by GwonHyeok on 2014. 6. 11..
 */
public class AnsiTextView extends TextView {
    /**
     * ANSI TextColor for Terminal.app
     */
    private final int BLACK = Color.rgb(0,0,0); // [0m, [30m
    private final int RED = Color.rgb(194,54,33); // [31m
    private final int GREEN = Color.rgb(37,188,36); // [32m
    private final int BROWN = Color.rgb(173,173,39);// [33m
    private final int BLUE = Color.rgb(73,46,225); // [34m
    private final int MAGENTA = Color.rgb(211,56,211); // [35m
    private final int CYAN = Color.rgb(51,187,200); // [36m
    private final int GRAY = Color.rgb(203,204,205); // [37m

    public AnsiTextView(Context context) {
        super(context);
    }

    public AnsiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnsiTextView(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
    }

    @Override
    public void append(CharSequence charSequence, int start, int end) {
        SpannableStringBuilder sb = new SpannableStringBuilder(charSequence);
        if(charSequence.toString().contains("[31m")) {
            sb.setSpan(new ForegroundColorSpan(RED), 0, charSequence.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if(charSequence.toString().contains("[32m")) {
            sb.setSpan(new ForegroundColorSpan(GREEN), 0, charSequence.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.append(sb, start, end);
    }
}
