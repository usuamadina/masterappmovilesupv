package org.example.baseproyect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.hardware.Camera;


public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, LoaderCallbackInterface {

    private static final int SOLICITUD_PERMISO_CAMERA = 1;
    private static final String TAG = "MainActivity";
    private CameraBridgeViewBase cameraView;

    private int cameraIndex; // 0--> back camera; 1--> front camera
    private int cam_width = 320;// image resolution
    private int cam_height = 240;
    private static final String STATE_CAMERA_INDEX = "cameraIndex";
    private int inputType = 0; // 0 -->camera 1--> file1 2-->file2
    private Mat imageResource_;
    private boolean reloadResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("onCreate","oncreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        cameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        cameraView =(CameraBridgeViewBase)findViewById(R.id.camera_view);
        if (savedInstanceState != null) {
            cameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX, 0);
        } else {
            cameraIndex = CameraBridgeViewBase.CAMERA_ID_BACK;
        }
        cameraView.setCameraIndex(cameraIndex);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Log.e("onCreate","tengo permisos");
            cameraView =(CameraBridgeViewBase)findViewById(R.id.camera_view);
            cameraView.setCvCameraViewListener(this);
        }
        else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, SOLICITUD_PERMISO_CAMERA);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_CAMERA) {
            Log.e("onRequest","he pedido permiso");
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraView =(CameraBridgeViewBase)findViewById(R.id.camera_view);
                cameraView.setCvCameraViewListener(this);
                Log.e("onRequest","permisosCondecidos");

            } else {
                Log.e("onRequestPermission","no tengo");
               // Toast.makeText(this, "sin cacceso a la cámara",Toast.LENGTH_SHORT).show();
                Snackbar.make(cameraView, "Sin acceso a la cámara, no funciona la aplicación", Snackbar.LENGTH_INDEFINITE).show();
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
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    public void onSavedInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_CAMERA_INDEX, cameraIndex);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        cam_height = height;
        cam_width = width;

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat input;
        if (inputType == 0) {
            input = inputFrame.rgba();
        } else {
            if(reloadResource == true) {
                imageResource_ = new Mat();
                int RECURSOS_FICHEROS[] = {0, R.raw.img1, R.raw.img2};
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        RECURSOS_FICHEROS[inputType]);
                //Convert the resource to an OpenCV Mat
                Utils.bitmapToMat(bitmap, imageResource_);
                Imgproc.resize(imageResource_, imageResource_, new Size(cam_width, cam_height));
                reloadResource = false;
            }
            input = imageResource_;
        }
        Mat output = input.clone();
        return output;
    }


   @Override
    public void onManagerConnected(int status) {
        switch(status){
            case LoaderCallbackInterface.SUCCESS:
                Log.e(TAG,"OpenCV se cargó correctamente");
                cameraView.setMaxFrameSize(cam_width,cam_height);
                cameraView.enableView();
                break;
            default:
                Log.e(TAG,"OpenCv no se cargó");
                Toast.makeText(this,"OpenCV no se cargó",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {

    }


    public boolean onTouchEvent(MotionEvent event) {
        openOptionsMenu();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeCamera:
                if (cameraIndex == CameraBridgeViewBase.CAMERA_ID_BACK) {
                    cameraIndex = CameraBridgeViewBase.CAMERA_ID_FRONT;
                } else
                    cameraIndex = CameraBridgeViewBase.CAMERA_ID_BACK;
                restartResolution();
                break;
            case R.id.resolution_800x600:
                cam_width = 800;
                cam_height = 600;
                restartResolution();
                break;
            case R.id.resolution_640x480:
                cam_width = 640;
                cam_height = 480;
                restartResolution();
                break;
            case R.id.resolution_320x240:
                cam_width = 320;
                cam_height = 240;
                restartResolution();
                break;
            case R.id.camera_Input:
                inputType = 0;
                break;
            case R.id.file1_Input:
                inputType = 1;
                reloadResource = true;
                break;
            case R.id.file2_Input:
                inputType = 2;
                reloadResource = true;
                break;
        }
        String msg = "W=" + Integer.toString(cam_width) + " H= " +
                Integer.toString(cam_height) + " Cam= " +
                Integer.toBinaryString(cameraIndex);
        Toast.makeText(MainActivity.this, msg,
                Toast.LENGTH_LONG).show();
        return true;
    }

    public void restartResolution() {
        cameraView.disableView();
        cameraView.setMaxFrameSize(cam_width, cam_height);
        cameraView.enableView();
    }


}
