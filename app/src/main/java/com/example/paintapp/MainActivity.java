package com.example.paintapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.paintapp.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    int defaultColor;
    private static String fileName;

    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myPaintings");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        askPermisson();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        String date = simpleDateFormat.format(new Date());
        fileName = path + "/" + date + ".png";

        if (!path.exists()){

            path.mkdirs();

        }

        defaultColor = ContextCompat.getColor(MainActivity.this,R.color.black);

        binding.btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        binding.penSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.txtPenSize.setText(i+"dp");
                binding.signatureView.setPenSize(i);

                binding.penSize.setMax(50);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveImage();
                    Toast.makeText(MainActivity.this,"Success Download", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.signatureView.clearCanvas();
            }
        });

    }

    private void saveImage() throws IOException {

        File file = new File(fileName);
        Bitmap bitmap = binding.signatureView.getSignatureBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        FileOutputStream  fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bitmapData);
        fileOutputStream.flush();
        fileOutputStream.close();

        Toast.makeText(MainActivity.this,"Success Download", Toast.LENGTH_LONG).show();

    }

    private void openColorPicker() {


        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                defaultColor = color;
                binding.signatureView.setPenColor(color);
            }
        });
        ambilWarnaDialog.show();


    }

    private void askPermisson() {
        Dexter.withActivity(this)  // change this
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE) //change this
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

    }
}