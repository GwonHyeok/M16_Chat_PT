package com.hyeok.m16_chat_pt.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        int INDEX = 1, length = 0;
        Pattern pattern = Pattern.compile("\\[[0-9]*m");
        String result[] = pattern.split(charSequence);
        Matcher matcher = pattern.matcher(charSequence);
        SpannableStringBuilder spannableStringBuilder;
        if(!matcher.find()) {
            spannableStringBuilder = new SpannableStringBuilder(charSequence);
        } else {
            if (result.length == 1) return;
            spannableStringBuilder = new SpannableStringBuilder();
        }
        matcher.reset();
        while(matcher.find()) {
            String ANSI_CODE = matcher.group();
            if(ANSI_CODE.equals("[0m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                try {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(BLACK), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IndexOutOfBoundsException e) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(BLACK), 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if(ANSI_CODE.equals("[31m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(RED), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(ANSI_CODE.equals("[32m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(GREEN), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(ANSI_CODE.equals("[33m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(BROWN), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(ANSI_CODE.equals("[34m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(BLUE), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(ANSI_CODE.equals("[35m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(MAGENTA), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(ANSI_CODE.equals("[36m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(CYAN), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(ANSI_CODE.equals("[37m")) {
                if(INDEX > 1) length = spannableStringBuilder.length();
                spannableStringBuilder.append(result[INDEX]);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(GRAY), INDEX > 1 ? length : 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            length = 0;
            INDEX++;
        }
        super.append(spannableStringBuilder, 0,spannableStringBuilder.length());
    }
}
