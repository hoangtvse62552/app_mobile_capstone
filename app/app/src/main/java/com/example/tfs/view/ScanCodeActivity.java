package com.example.tfs.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanCodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String token;
    private int premisesId;
    private int userID;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        surfaceView = findViewById(R.id.camera_view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("token", "");
        if (token.contains("Veterinary")) {
            role = "Veterinary";
        } else if(token.contains("Distributor")) {
            role = "Distributor";
        }
        premisesId = sharedPreferences.getInt("premisesId", 0);
        userID = sharedPreferences.getInt("userID", 0);
    }

    private void initialiseDetectorsAndSources() {

//        Toast.makeText(getApplicationContext(), "Bắt đầu quét mã", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanCodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "Quét mã thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    String barcode = barcodes.valueAt(0).displayValue;
                    String[] code = barcode.split("-");
//                    if(code[0].equals("Food") && token.contains("Distributor")) {
////                        if(Integer.parseInt(code[3]) != distributorId) {
////                            Toast.makeText(ScanCodeActivity.this, "Mã code không hợp lệ", Toast.LENGTH_LONG).show();
////                        } else {
//                            it = new Intent(getApplicationContext(), ScanFoodResultActivity.class);
//                            it.putExtra("foodId", code[1]);
//                            it.putExtra("disId", code[3]);
////                            startActivity(it);
////                        }
//
//                    } else if(!code[0].equals("Food") && token.contains("Veterinary")){
//                        it = new Intent(getApplicationContext(), ScanResultActivity.class);
//                        it.putExtra("transactionId", barcodes.valueAt(0).displayValue);
//
//                    } else {
//                        return;
//                    }
//                    startActivity(it);
//                    if(code[0].equals("Trans") && token.contains("Distributor")) {
//                        it = new Intent(getApplicationContext(), ScanFoodResultActivity.class);
//                        it.putExtra("foodId", code[1]);
//                        it.putExtra("providerId", code[2]);
//                        it.putExtra("premisesId", code[3]);
//
//                    } else if(!code[0].equals("Food") && token.contains("Veterinary")){
//                        it = new Intent(getApplicationContext(), ScanResultActivity.class);
//                        it.putExtra("transactionId", code[1]);
//                    } else {
//                        return;
//                    }
                    final int transId = Integer.parseInt(code[1]);
                    final int providerId = Integer.parseInt(code[2]);
                    if (code[0].equals("Trans")) {
                        TransactionService.getInstance().getTransaction(ScanCodeActivity.this, transId,role, new VolleyCallBack() {
                            @Override
                            public void onSuccess(Object response) {
                                try {
                                    String json = response.toString();
                                    ObjectMapper mapper = new ObjectMapper();
                                    JsonNode jsonNode = mapper.readTree(json);
                                    int receiverId = jsonNode.get("data").get("Receiver").get("PremisesId").asInt();
                                    int veterinaryId = jsonNode.get("data").get("VeterinaryId").asInt();
                                    System.out.println(providerId + "     aaaaaaaaaaaaaaaaa");
                                    if((role.equals("Distributor") && premisesId == receiverId) ||
                                            (role.equals("Veterinary")
//                                                    && veterinaryId == userID
                                            ) ){
                                        goToResult(transId, providerId);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Object ex) {

                            }
                        });
                    } else {
                        Toast.makeText(ScanCodeActivity.this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void goToResult(int transId, int providerId) {

        Intent intent = new Intent(getApplicationContext(), ScanResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("transactionId", "" + transId);
        intent.putExtra("providerId", "" + providerId);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }
}
