package com.fokakefir.linkhub.gui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.logic.database.ReviewDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class ReviewActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, View.OnClickListener, ReviewDatabaseManager.OnResponseListener {

    // region 0. Constants

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int STORAGE_PERMISSION_CODE = 123;

    // endregion

    // region 1. Declaration

    private NumberPicker picker;

    private TextInputLayout txtInputContent;

    private FloatingActionButton fabDone;
    private FloatingActionButton fabChooseImage;

    private ImageView imageView;

    private Uri imageUri;

    private int number = 1;

    private String placeId;

    private ReviewDatabaseManager databaseManager;

    // endregion

    // region 2. Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle bundle = getIntent().getExtras();
        this.placeId = bundle.getString(MainActivity.PLACE_ID);

        this.txtInputContent = findViewById(R.id.txt_input_review_content);

        this.picker = findViewById(R.id.number_picker);
        this.picker.setMinValue(1);
        this.picker.setMaxValue(10);

        this.picker.setOnValueChangedListener(this);

        this.fabDone = findViewById(R.id.fab_done_review);
        this.fabDone.setOnClickListener(this);

        this.fabChooseImage = findViewById(R.id.fab_upload_review_image);
        this.fabChooseImage.setOnClickListener(this);

        this.imageView = findViewById(R.id.img_new_review);

        this.databaseManager = new ReviewDatabaseManager(this, this, this.placeId);
        this.databaseManager.getUserData();
    }

    // endregion

    // region 3. Image chooser

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                openFileChooser();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            this.imageUri = data.getData();

            Picasso.with(this).load(this.imageUri).into(this.imageView);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // endregion

    // region 4. NumberPicker listener

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        this.number = numberPicker.getValue();
    }

    // endregion

    // region 5. Button listeners

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_done_review) {
            String content = txtInputContent.getEditText().getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                this.databaseManager.uploadReviewWithImage(content, this.number, this.imageUri);
            }
        } else if (view.getId() == R.id.fab_upload_review_image) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                requestStoragePermission();
            }
        }
    }

    // endregion

    // region 6. Database listener

    @Override
    public void onUploaded() {
        finish();
    }

    @Override
    public void onFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // endregion



}