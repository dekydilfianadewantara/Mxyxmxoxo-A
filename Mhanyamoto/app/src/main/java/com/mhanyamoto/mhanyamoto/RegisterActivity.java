package com.mhanyamoto.mhanyamoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mhanyamoto.mhanyamoto.entity.Login;
import com.mhanyamoto.mhanyamoto.entity.Payment;
import com.mhanyamoto.mhanyamoto.entity.Vehicles;
import com.mhanyamoto.mhanyamoto.service.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by stark on 4/19/2015.
 */
public class RegisterActivity extends Activity {

    //TIME
    Calendar clock = Calendar.getInstance();
    SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
    ImageButton b_register;
    Button b_refresh;
    EditText et_username, et_password, et_email, et_phone, et_address, et_vehicles, et_charge, et_payment, et_time_start, et_time_end;
    Service mService;
    Payment paymentList;
    LinearLayout error_layout;
    ScrollView container;
    Vehicles vehiclesList;
    //Progress Bar
    boolean loadPayment , loadVehicles ;

    //Photo Upload
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView ivImage;
    TypedFile typedFile;
    String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        b_refresh = (Button) findViewById(R.id.b_refresh);
        b_register = (ImageButton) findViewById(R.id.registerBtn2);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        //et_vehicles = (EditText) findViewById(R.id.et_vehicles);
        et_charge = (EditText) findViewById(R.id.et_charge);
        //et_payment = (EditText) findViewById(R.id.et_payment);
        et_time_start = (EditText) findViewById(R.id.et_time_start);
        et_time_end = (EditText) findViewById(R.id.et_time_end);

        error_layout = (LinearLayout) findViewById(R.id.error_layout);
        container = (ScrollView) findViewById(R.id.container);

        recall();

        b_refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               recall();
           }
       });

        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);


        initAPI();


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(getResources().getString(R.string.host))
                .build();

        mService = restAdapter.create(Service.class);
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        typedFile = new TypedFile("multipart/form-data", destination);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        File photo = new File(selectedImagePath);
        typedFile = new TypedFile("multipart/form-data", photo);


        ivImage.setImageBitmap(bm);
    }

    private void recall(){

        //Progress Bar
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        loadPayment = true;
        loadVehicles = true;
        initAPI();
        mService.getPayments(new Callback<List<Payment>>() {
            @Override
            public void success(List<Payment> payments, Response response) {
                //Spinner Payment
                final Spinner spin_payment = (Spinner) findViewById(R.id.spin_payment);
                final SpinnerAdapterPayment adapterPayment = new SpinnerAdapterPayment(RegisterActivity.this,
                        android.R.layout.simple_spinner_item,
                        payments);
                spin_payment.setAdapter(adapterPayment);
                spin_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position
                        paymentList = adapterPayment.getItem(position);
                        // Here you can do the action you want to...
                       /* Toast.makeText(RegisterActivity.this, "ID: " + paymentList.getId() + "\nName: " + paymentList.getName(),
                                Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {
                    }
                });

                loadPayment = false;
                if (!loadPayment && !loadVehicles) {
                    mProgressDialog.dismiss();
                    error_layout.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error_layout.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        mProgressDialog.cancel();
                        error_layout.setVisibility(View.VISIBLE);
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);
            }
        });


        mService.getVehicles(new Callback<List<Vehicles>>() {
            @Override
            public void success(List<Vehicles> vehicles, Response response) {
                //Spinner Payment
                final Spinner spin_vehicles = (Spinner) findViewById(R.id.spin_vehicles);

                final SpinnerAdapterVehicles adapterVehicles = new SpinnerAdapterVehicles(RegisterActivity.this,
                        android.R.layout.simple_spinner_item,
                        vehicles);
                spin_vehicles.setAdapter(adapterVehicles);

                spin_vehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position
                        vehiclesList = adapterVehicles.getItem(position);
                        // Here you can do the action you want to...
                      /*  Toast.makeText(RegisterActivity.this, "ID: " + vehiclesList.getId() + "\nName: " + vehiclesList.getName(),
                                Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {
                    }
                });

                loadVehicles = false;
                if (!loadPayment && !loadVehicles) {
                    mProgressDialog.dismiss();
                    error_layout.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error_layout.setVisibility(View.VISIBLE);
                container.setVisibility(View.GONE);
                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        mProgressDialog.cancel();
                        error_layout.setVisibility(View.VISIBLE);
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);
            }
        });

        //Spinner Vehicles


        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(RegisterActivity.this, "ID: " + paymentList.getId() + "\nName: " + paymentList.getName(),
                        Toast.LENGTH_SHORT).show();

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String email = et_email.getText().toString();
                String phone = et_phone.getText().toString();
                String address = et_address.getText().toString();
                String vehicles = vehiclesList.getId();
                String charge = et_charge.getText().toString();
                String payment = paymentList.getId();
                String time_start = et_time_start.getText().toString();
                String time_end = et_time_end.getText().toString();
                String longitude = "105";
                String latitude = "125";

                if (typedFile == null || username.contentEquals("") || password.contentEquals("") || email.contentEquals("") || phone.contentEquals("") || address.contentEquals("") || vehicles.contentEquals("") || charge.contentEquals("") || time_start.contentEquals("") || time_end.contentEquals("")) {
                    // custom dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                    alert.setTitle("Empty data");
                    StringBuffer responseText = new StringBuffer();
                    responseText.append("Fill Empty Data\n");

                    alert.setMessage(responseText);
                    alert.setPositiveButton("OK",null);
                    alert.show();
                } else {
                    initAPI();

                    mService.register(username, email, password, phone, address, vehicles, charge, payment, time_start, time_end, longitude, latitude,typedFile, new Callback<Login>() {
                        @Override
                        public void success(Login login, Response response) {

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
        });

    }
}
