package roy.me.thumbnailviewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int READ_EXTERNAL_STORAGE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkReadExternalStoragePermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: 2017/2/10 call cursor loader
                    Toast.makeText(this, "已获得权限", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // TODO: 2017/2/10 读取内容
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "需要读取相册的权限", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            }
        } else {
            // TODO: 2017/2/10 直接读取内容
        }
    }
}
