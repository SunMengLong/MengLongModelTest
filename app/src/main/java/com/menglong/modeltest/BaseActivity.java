package com.menglong.modeltest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

public class BaseActivity extends Activity {

    private static PermissioLitener mLitener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
    }

    public void requestRuntimePermision(String[] permission, PermissioLitener permissioLitener, Context context) {
        mLitener = permissioLitener;
        ArrayList<String> list = new ArrayList<>();
        for (String per : permission) {
            //如果当前的这个权限没有被申请
            if (ContextCompat.checkSelfPermission(context, per) != PackageManager.PERMISSION_GRANTED) {
                //添加到list集合当中
                list.add(per);
            }
        }

        //判断list集合是否为空，不为空则申请权限
        if (!list.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, list.toArray(new String[list.size()]), 1);
        } else {
            Toast.makeText(context, "所有的权限都已经通过了！", Toast.LENGTH_SHORT).show();
            //权限通过的回调方法
            mLitener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //此处判断的是上方的标记“1”
            case 1:
                if (grantResults.length > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            list.add(permission);
                        }
                    }
                    //判断所有的权限是否都通过了
                    if (!list.isEmpty()) {
                        mLitener.onDenied(list);
                    } else {
                        mLitener.onGranted();
                    }
                }
                break;
            default:
                break;
        }
    }
}
