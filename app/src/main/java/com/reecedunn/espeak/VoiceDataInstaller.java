package com.reecedunn.espeak;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.*;

public class VoiceDataInstaller {
    public static String extract(Context context) {
        File targetDir = new File(context.getFilesDir(), "espeak-ng-data");
        if (!targetDir.exists()) {
            copy(context.getAssets(), "espeak-ng-data", targetDir);
        }
        return targetDir.getAbsolutePath();
    }

    private static void copy(AssetManager assets, String src, File dst) {
        try {
            String[] files = assets.list(src);
            if (files == null || files.length == 0) {
                try (InputStream in = assets.open(src);
                     OutputStream out = new FileOutputStream(dst)) {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                }
            } else {
                dst.mkdirs();
                for (String f : files) {
                    copy(assets, src + "/" + f, new File(dst, f));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
