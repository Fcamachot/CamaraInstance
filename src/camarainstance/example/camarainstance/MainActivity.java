package camarainstance.example.camarainstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;



public class MainActivity extends Activity {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final String IMAGE_DIRECTORY_NAME = "directory_img_instance";
	
	private Camera mCamera;
	private Uri mFileUri;
	private CamaraInstance mCamaraObj;
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_camara);
		mContext = this;
		Button captureButton = (Button) findViewById(R.id.button_capture);
		if (detectarCamara(this)){
			int numCameras = Camera.getNumberOfCameras(); //obtengo el numero de camaras que tiene el dispositivo
			mCamera = getCameraInstance(this);
			Camera.Parameters mParameters = mCamera.getParameters();
			mParameters.setPictureFormat(ImageFormat.JPEG);
			mCamera.setParameters(mParameters);
			mCamaraObj = new CamaraInstance(this,mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mCamaraObj);
			
			
		}else{
			Toast.makeText(this, "No hay una camara disponible", Toast.LENGTH_SHORT);
		}
		
		captureButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// get an image from the camera
						Log.d("Camara", "Esta antes de capturar la imagen");
						mCamera.takePicture(null, null, mPicture);
						
					}
				}
				);
		
	} 
	
	@Override
	protected void onStop(){
		super.onStop();
		mCamera.release();
	}
	
	private void detectarParametros(){
		Camera camara = getCameraInstance(this); 
		if (camara != null){
			Camera.Parameters param = camara.getParameters();
		}
	}
	
	private boolean detectarCamara(Context context){
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			return true;
		}else{
			return false;
		}		
	}

	public static Camera getCameraInstance(Context context){
	    Camera c = null;
	    
	    try {
	        c = Camera.open(0); // Selecciona el tipo de camara (delantera, trasera)
	    }
	    catch (Exception e){
	        // Camara no disponible
	    	Toast.makeText(context, "Error al intentar crear objeto tipo camara", Toast.LENGTH_SHORT);
	    	
	    }
	    return c; // retorna null si la camara no esta disponible
	}
	
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			mFileUri = Uri.fromFile(pictureFile);
			
			if (pictureFile == null){
				Log.d("Camara", "Error creating media file, check storage permissions: " );
				return;
			}

			try {
				Log.d("Camara","la ruta donde su guarda es :" + pictureFile.toString());
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d("Camara", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("Camara", "Error accessing file: " + e.getMessage());
			}
		}
	};
	
	public Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}
    
    
	private static File getOutputMediaFile(int type) {
		 
	    // External sdcard location
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    File mediaStorageDir = new File(path, IMAGE_DIRECTORY_NAME); //Nombre del directorio donde se va a guardar
	    //Toast.makeText(mContext, " Ruta donde se debe guardar la imagen :" + path.toString(), Toast.LENGTH_SHORT);
	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists()) {
	        if (!mediaStorageDir.mkdirs()) {
	            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
	                    + IMAGE_DIRECTORY_NAME + " directory");
	            return null;
	        }
	    }
	 
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	            Locale.getDefault()).format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "IMG_" + timeStamp + ".jpg");
	    } else if (type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "VID_" + timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	 
	    return mediaFile;
	}

	

}
