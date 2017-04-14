package org.example.baseproyect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;


public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, LoaderCallbackInterface{

    private static final int SOLICITUD_PERMISO_CAMERA = 0;
    private View view;
    private static final String TAG = "MainActivity";
    private CameraBridgeViewBase cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.content_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(view, "Se necesita permiso para usar la cámara.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, SOLICITUD_PERMISO_CAMERA);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, SOLICITUD_PERMISO_CAMERA);
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        cameraView = (CameraBridgeViewBase) findViewById(R.id.vista_camara);
        cameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode == SOLICITUD_PERMISO_CAMERA){
           if (grantResults.length == 1&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               //TODO call something
           }else{
               Snackbar.make(view,"Sin acceso a la cámara, no funciona la aplicación",Snackbar.LENGTH_SHORT).show();
           }
       }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null)
            cameraView.disableView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraView != null){
            cameraView.disableView();
        }
    }

    @Override
    public void onManagerConnected(int status) {
        switch (status) {
            case LoaderCallbackInterface.SUCCESS:
                Log.i(TAG, "Ok");
                cameraView.enableView();
                break;
            default:
                Log.i(TAG, "OpenCV no se cargo");
                Toast.makeText(MainActivity.this, "OpenCV no se cargo",
                        Toast.LENGTH_LONG).show();
                finish();
                break;
        }

    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }


}
