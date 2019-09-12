package com.blueapple.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView im;
    Button btn_send,btn_retriv,btn_search;
    Bitmap bitmap;
    EditText editText;
    TextView textView;

    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im=findViewById(R.id.imageViewid);
        btn_send=findViewById(R.id.btn_send_id);
        btn_retriv=findViewById(R.id.btn_retrive_id);
        btn_search=findViewById(R.id.btn_serach_id);
        editText=findViewById(R.id.editTextid);
        textView=findViewById(R.id.textViewid);

        databaseHelper=new DatabaseHelper(this);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 0);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=editText.getText().toString();

               byte[] imagedata= DbBitmapUtility.getBytes(bitmap);

               for (int i=0;i<imagedata.length;i++)
               {
                   Log.d("image_data  ", String.valueOf(imagedata[i]));
               }

               databaseHelper.addEntry(name,imagedata);

               Toast.makeText(MainActivity.this, ""+imagedata, Toast.LENGTH_SHORT).show();


            }
        });

        btn_retriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String image_name=editText.getText().toString();

                if (image_name!=null)
                {
                    Image_Details details=  databaseHelper.getImage(image_name);

                    byte[] data=details.getImage();
                   Bitmap bitmap= DbBitmapUtility.getImage(data);

                   String name=details.getName();

                   im.setImageBitmap(bitmap);
                   textView.setText(name);
                   editText.getVisibility();
                   editText.setText("");

                   editText.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==0) {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);

                bitmap = BitmapFactory.decodeStream(inputStream);

                im.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
