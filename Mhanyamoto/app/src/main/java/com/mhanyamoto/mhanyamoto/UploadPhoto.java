package com.mhanyamoto.mhanyamoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mhanyamoto.mhanyamoto.service.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UploadPhoto extends Activity {

	int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	Button btnSelect;
	ImageView ivImage;
	Service mService;
	TypedFile typedFile;
	String selectedImagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		ivImage = (ImageView) findViewById(R.id.ivImage);


		Button btn_upload = (Button) findViewById(R.id.btn_upload);

		initAPI();

		TextView tvImage = (TextView) findViewById(R.id.tvImage);
		tvImage.setText("" + selectedImagePath);
		btn_upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(selectedImagePath != null) {
					mService.upload(typedFile, new Callback<String>() {
						@Override
						public void success(String s, Response response) {
							Log.e("Upload", "success");
						}

						@Override
						public void failure(RetrofitError error) {
							Log.e("Upload", "error");
						}
					});
				}else{
					Toast.makeText(UploadPhoto.this, "Photo not found", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhoto.this);
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
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
				null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
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
		TextView tvImage = (TextView) findViewById(R.id.tvImage);
		tvImage.setText("" + selectedImagePath);
		File photo = new File(selectedImagePath);
		typedFile = new TypedFile("multipart/form-data", photo);


		ivImage.setImageBitmap(bm);
	}


	private void initAPI() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setEndpoint(getResources().getString(R.string.host))
				.build();

		mService = restAdapter.create(Service.class);
	}
}
