package com.n.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {

    TextView cameraTV, audioTV;
    ImageView selectedIV, selectedTwoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        init();
    }

    private void init() {
        cameraTV = findViewById(R.id.cameraTV);
        selectedIV = findViewById(R.id.selectedIV);
        selectedTwoIV = findViewById(R.id.selectedTwoIV);
        audioTV = findViewById(R.id.audioTV);
        cameraTV.setOnClickListener(this);
        audioTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cameraTV:
                Options options = Options.init()
                        .setRequestCode(REQUEST_CODE)                                           //Request code for activity results
                        .setCount(2)                                                   //Number of images to restict selection count
                        .setFrontfacing(false)           //Front Facing camera on start
                        .setSpanCount(4)//Span count for gallery min 1 & max 5
                        .setExcludeVideos(true)//Option to exclude videos
                        .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion;                                       //Custom Path For media Storage

                Pix.start(this, options);
                break;

            case R.id.audioTV:
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);
                break;
        }
    }

    int REQUEST_CODE = 100;
    ArrayList<String> stringImages;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            stringImages = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            setImages();
        }
        if(requestCode == 1){
            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
                String uriString = uri.toString();
                Uri playUri=Uri.parse(uriString);
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(this,playUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }
    }

    public void setImages() {
        for (int i = 0; i < stringImages.size(); i++) {
            File imgFile = new File(stringImages.get(i));
            if (imgFile.exists()) {
                if (i == 0) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    selectedIV.setImageBitmap(myBitmap);
                } else if (i == 1) {
                    selectedTwoIV.setVisibility(View.VISIBLE);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    selectedTwoIV.setImageBitmap(myBitmap);
                }
            }
        }
    }
}