package com.hyeok.m16_chat_pt.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyeok.m16_chat_pt.app.R;

import java.util.ArrayList;

/**
 * Created by GwonHyeok on 2014. 6. 14..
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChatViewpager extends Fragment {
    private static final String ARG_POSITION = "position";
    public static ScrollView CHAT_SCROLL = null;
    public static AnsiTextView CHAT_TEXTVIEW = null;
    public static ListView CHAT_FRIEND_LISTVIEW = null, CHAT_CLAN_LISTVIEW = null;
    public static ArrayList<FRCLListViewData> CHAT_FRIEND_LIST = null, CHAT_CLAN_LIST = null;
    private static FrameLayout chatfl;
    private static FrameLayout friendfl;
    private static FrameLayout clanfl;
    private int position;

    public static ChatViewpager newInstance(int position) {
        ChatViewpager f = new ChatViewpager();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    public static void ViewReset() {
        clanfl = null;
        chatfl = null;
        friendfl = null;
        CHAT_SCROLL = null;
        CHAT_TEXTVIEW = null;
        CHAT_FRIEND_LISTVIEW = null;
        CHAT_CLAN_LISTVIEW = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        if(chatfl == null) {
            chatfl = new FrameLayout(getActivity());
            friendfl = new FrameLayout(getActivity());
            clanfl = new FrameLayout(getActivity());
            chatfl.setLayoutParams(params);
            friendfl.setLayoutParams(params);
            clanfl.setLayoutParams(params);
        }
        switch (position) {
            case 0:
                if(CHAT_SCROLL == null) {
                    CHAT_SCROLL = new ScrollView(getActivity());
                    CHAT_TEXTVIEW = new AnsiTextView(getActivity());
                    CHAT_SCROLL.addView(CHAT_TEXTVIEW);
                    chatfl.addView(CHAT_SCROLL);
                }
                if(chatfl.getParent() != null)
                    ((ViewGroup)chatfl.getParent()).removeAllViews();
                return chatfl;
            case 1:
                if(CHAT_FRIEND_LISTVIEW == null) {
                    CHAT_FRIEND_LISTVIEW = new ListView(getActivity());
                    CHAT_FRIEND_LIST = new ArrayList<FRCLListViewData>();
                    CHAT_FRIEND_LISTVIEW.setAdapter(new FRCLListAdapter(getActivity(), R.layout.custom_frcl_listview, CHAT_FRIEND_LIST));
                    friendfl.addView(CHAT_FRIEND_LISTVIEW);
                }
                if(friendfl.getParent() != null)
                    ((ViewGroup)friendfl.getParent()).removeAllViews();
                return friendfl;
            case 2:
                if(CHAT_CLAN_LISTVIEW == null) {
                    CHAT_CLAN_LISTVIEW = new ListView(getActivity());
                    CHAT_CLAN_LIST = new ArrayList<FRCLListViewData>();
                    CHAT_CLAN_LISTVIEW.setAdapter(new FRCLListAdapter(getActivity(), R.layout.custom_frcl_listview, CHAT_CLAN_LIST));
                    clanfl.addView(CHAT_CLAN_LISTVIEW);
                }
                if(clanfl.getParent() != null)
                    ((ViewGroup)clanfl.getParent()).removeAllViews();
                return clanfl;
        }
        return null;
    }

    class FRCLListAdapter extends ArrayAdapter<FRCLListViewData> {
        private ArrayList<FRCLListViewData> objects;

        public FRCLListAdapter(Context context, int textViewResourceId, ArrayList<FRCLListViewData> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.custom_frcl_listview, null);
            }
            FRCLListViewData frclListViewData = objects.get(position);
            if (frclListViewData != null) {
                String name = frclListViewData.getname();
                String status = frclListViewData.getstatus();
                boolean is_online = frclListViewData.getis_online();
                TextView nameView = (TextView) v.findViewById(R.id.custom_listview_name_text);
                TextView infoView = (TextView) v.findViewById(R.id.custom_listview_info_text);
                ImageView onlineView = (ImageView) v.findViewById(R.id.custom_listview_online_image);
                nameView.setText(name);
                infoView.setText(status);
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getContext().getResources().getDisplayMetrics());
                int x = (int) px;
                int y = (int) px;
                int r = (int) px / 2;
                Paint mPaint = new Paint();
                Bitmap bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
                if (is_online) {
                    mPaint.setColor(Color.rgb(101, 167, 48));
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawCircle(x / 2, y / 2, r / 2, mPaint);
                } else {
                    mPaint.setColor(Color.GRAY);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawCircle(x / 2, y / 2, r / 2, mPaint);
                }
                onlineView.setImageBitmap(bitmap);
            }
            return v;
        }
    }
}
