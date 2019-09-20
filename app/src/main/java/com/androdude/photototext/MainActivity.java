package com.androdude.photototext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private ImageView image;
    private Button bttn,takeNewImg;
    private String Text;
    private int numbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },100);
        }

        takeNewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });


        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTextFromImage();
            }
        });
    }

    private void setView()
    {
        text = (TextView)findViewById(R.id.textView);
        image = (ImageView) findViewById(R.id.imageView);
        bttn = (Button)findViewById(R.id.click);
        takeNewImg = (Button) findViewById(R.id.takeImg);
    }

    private void getTextFromImage()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.img);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational())
        {
            Toast.makeText(this, "Sorry Your Device Doesnt Support This App", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            for(int i=0;i< items.size();++i)
            {
                TextBlock myItems = items.valueAt(i);
                sb.append(myItems.getValue());
                sb.append("\n");
            }
            Text = sb.toString();
            text.setText(sb);
        }
    }

    private void launchCamera()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Bundle extras = data.getExtras();
            if(requestCode == 100)
            {
                Bitmap photo = (Bitmap) extras.get("data");
                image.setImageBitmap(photo);
            }


    }


}
