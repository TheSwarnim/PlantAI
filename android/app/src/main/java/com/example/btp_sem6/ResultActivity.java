package com.example.btp_sem6;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    StorageReference storageReference;
    private String firebaseImgUri;
    ImageView imageView;
    TextView disease, score;
    Button googleSearch;
    String words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Init();
        googleSearch.setOnClickListener(v -> {
            if(words.length() != 0){
                searchNet(words);
            } else {
                Toast.makeText(this, "Please enter query to search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchNet(String words) {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, words);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            searchGoogle(words);
        }
    }

    private void searchGoogle(String words) {
        try {
            Uri uri = Uri.parse("http://www.google.com/#q=" + words);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void Init() {
        storageReference = FirebaseStorage.getInstance().getReference();
        imageView = findViewById(R.id.image);
        Uri myUri = getIntent().getData();
        uploadPic(myUri);
        imageView.setImageURI(myUri);

        disease = findViewById(R.id.disease_name);
        score = findViewById(R.id.score);

        googleSearch = findViewById(R.id.search_button);
    }

    private void uploadPic(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final String randomKey = UUID.randomUUID().toString();

        StorageReference ref = storageReference.child("images/" + randomKey);
        UploadTask uploadTask = ref.putFile(imageUri);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseImgUri = Objects.requireNonNull(task.getResult()).toString();
                predict();
            } else {
                Toast.makeText(ResultActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        });
    }

    private void predict(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Fetching Values");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        String Url = "http://ec2-13-233-125-177.ap-south-1.compute.amazonaws.com/predict";
        post(Url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    System.out.println(e.getMessage());
                    Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String str = Objects.requireNonNull(response.body()).string();
                final String substring = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
                try {
                    JSONObject jsonObject = new JSONObject(substring);
                    words = jsonObject.getString("disease").trim();
                    String sc = jsonObject.getString("confidence").trim();
                    runOnUiThread(() -> {
                        disease.setText(words);
                        score.setText(sc);
                    });
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        });
    }

    Call post(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"img\": \"" + firebaseImgUri +"\"\r\n}");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}