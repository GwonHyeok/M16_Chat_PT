package com.hyeok.m16.chat_pt.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by GwonHyeok on 2014. 6. 6..
 */

public class BinaryUtil {
    private final String TAG = "M16_CHAT";
    private final String BIN_FILE = "bnchat";
    public static BinaryUtil instance;

    private BinaryUtil() {}

    public static BinaryUtil getInstance() {
        if (instance == null) {
            instance = new BinaryUtil();
        }
        return instance;
    }

    public String GetBinaryPath(Context mContext) {
        return mContext.getFilesDir().getAbsoluteFile().toString()+"/"+BIN_FILE;
    }
    public void CheckBinaryFile(Context mContext) {
        AssetManager assetManager = mContext.getAssets();
        String FILE_ROOT_DIR = mContext.getFilesDir().getAbsoluteFile().toString();
        String DATA_FOLDER_BNCHAT = FILE_ROOT_DIR+"/"+BIN_FILE;

        if (new File(DATA_FOLDER_BNCHAT).isFile()) {
            Log.i(TAG, BIN_FILE+" File Alerady Exist");
            new File(DATA_FOLDER_BNCHAT).setExecutable(true);
            return;
        }
        try {
            InputStream in = null;
            OutputStream op = null;
            in = assetManager.open(BIN_FILE);
            new File(DATA_FOLDER_BNCHAT).createNewFile();
            op = new FileOutputStream(DATA_FOLDER_BNCHAT);
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1) {
                op.write(buffer, 0, read);
            }
            new File(DATA_FOLDER_BNCHAT).setExecutable(true);
            Log.i(TAG, mContext.getFilesDir().getAbsoluteFile().toString());
            Log.i(TAG, "File Copy Success");
        } catch (Exception e) {

        }
    }
}
