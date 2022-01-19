package com.example.btp_sem6;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_IMAGE = 100;
    private static final int REQUEST_UPLOAD_IMAGE = 200;

    private static final int REQUEST_READ_STORAGE = 300;
    private static final int REQUEST_CAMERA_STORAGE = 400;

    ImageView selectedImg;
    Button takePhoto, uploadPhoto;
    FirebaseStorage storage;
    StorageReference storageReference;
    String firebaseImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

        takePhoto.setOnClickListener(v -> takePhoto());
        uploadPhoto.setOnClickListener(v -> chooseImg());
    }

    private void takePhoto() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_STORAGE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    private void Init() {
        selectedImg = findViewById(R.id.selectedImg);
        takePhoto = findViewById(R.id.takePhoto);
        uploadPhoto = findViewById(R.id.uploadPhoto);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void chooseImg() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_UPLOAD_IMAGE);
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_UPLOAD_IMAGE);
            }
        }
        if(requestCode == REQUEST_CAMERA_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPLOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            selectedImg.setImageURI(imageUri);
            uploadPic(imageUri);
        }

        if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                File f = new File(currentPhotoPath);
                selectedImg.setImageURI(Uri.fromFile(f));
                uploadPic(Uri.fromFile(f));
            }
        }
    }

    String currentPhotoPath;

    private File createImgFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String fName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImgFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.btp_sem6.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE);
            }
        }
    }

    private void uploadPic(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();

        StorageReference ref = storageReference.child("images/" + randomKey);

        ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
            result.addOnSuccessListener(uri -> firebaseImgUri = uri.toString());
            System.out.println(firebaseImgUri);
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
            double percent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            pd.setMessage(percent + "");
        });
    }
}