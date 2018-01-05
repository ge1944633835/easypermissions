package me.sunlight.easypermissions;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import me.sunligh.easypermissions.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final static int TAKE_CODE = 123;

    private Button mBtnTakePhoto;
    private ImageView mIvPhoto;
    // 权限回调的标示
    private static final int RC = 0x0100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnTakePhoto = findViewById(R.id.button);
        mIvPhoto = findViewById(R.id.img);

        mBtnTakePhoto.setOnClickListener(view -> {
            String[] perms = new String[]{Manifest.permission.CAMERA};
            boolean hasPermissions = EasyPermissions.hasPermissions(this, perms);
            if (hasPermissions) takePhoto();
            else requestPerm();
        });

    }


    private void takePhoto() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, TAKE_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TAKE_CODE) {

            if (data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //拿到bitmap，做喜欢做的事情把  ---> 显示 or上传？

                mIvPhoto.setImageBitmap(bitmap);
            }

        }

    }

    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(this, perms)) {
            // 已经授予权限了
            takePhoto();
        } else {
            EasyPermissions.requestPermissions(this, "授予权限",
                    RC, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // 申请成功
        // 已经授予权限了
        takePhoto();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }


}
