package com.example.downloadsong;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 0;
	private static String file_url = " http://android.programmerguru.com/wp-content/uploads/2013/04/hosannatelugu.mp3";
	// ImageView my_song;
	Button btnShowProgress;
	

	//private Uri fileUri; // file url to store image/video

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// my_song = (ImageView) findViewById(R.id.my_song);
		btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
		btnShowProgress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new DownloadFileFromURL().execute(file_url);

			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
			super.onPreExecute();
		}

		@SuppressLint("SdCardPath")
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				
				File f=new File("/sdcard/Music");
				if(!f.exists()){
					f.mkdir();
				}

				String[] parts=file_url.split("/");
				String f_name=parts[parts.length-1];
				// Output stream to write file
				OutputStream output = new FileOutputStream(
						"/sdcard/Nzlha/"+f_name);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			pDialog.setProgress(Integer.parseInt(progress[0]));
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			Toast.makeText(getApplicationContext(),
					"Download Completed Successfully ..", Toast.LENGTH_SHORT)
					.show();

			// Displaying downloaded image into image view
			// Reading image path from sdcard
			/*
			 * String imagePath = Environment.getExternalStorageDirectory()
			 * .toString() + "/downloadedfile.mp3";
			 */
			// my_song.setImageDrawable(Drawable.createFromPath(imagePath));

		}

	}

}
