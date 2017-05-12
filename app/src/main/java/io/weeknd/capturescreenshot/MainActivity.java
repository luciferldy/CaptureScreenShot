package io.weeknd.capturescreenshot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.weeknd.capturescreenshot.utils.ScreenShotUtils;

public class MainActivity extends AppCompatActivity {

    private Button captureScreenShot;
    private ImageView captureResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captureScreenShot = (Button) findViewById(R.id.screen_shot);
        captureResult = (ImageView) findViewById(R.id.capture_result);
        if (!checkPermissions()) {
            captureScreenShot.setClickable(false);
        }
        captureScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ScreenShotUtils.captureScreenShot(MainActivity.this);
                if (bitmap != null) {
                    captureResult.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    captureResult.setImageBitmap(bitmap);
                    Log.d("MainActivity", Environment.getExternalStorageDirectory() + "/CaptureScreenShot");
                    String path = getExternalCacheDir() + "/CaptureScreenShot/00.png";
                    Log.d("MainActivity", "path is " + path);
                    boolean ok = ScreenShotUtils.savePic(bitmap, getExternalCacheDir() + "/CaptureScreenShot/", "00.png");
                    if (!ok) {
                        Toast.makeText(getBaseContext(), "保存图片失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                String[] tmp = new String[permissions.size()];
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = permissions.get(i);
                }
                requestPermissions(tmp, 0);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int granted = 0;
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted++;
                }
            }
            if (granted == grantResults.length) {
                // 全部获取到
                captureScreenShot.setClickable(true);
            }
        }
    }


}
