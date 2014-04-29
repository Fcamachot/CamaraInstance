package camarainstance.example.camarainstance;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CamaraInstance extends SurfaceView implements SurfaceHolder.Callback {
	
	private SurfaceHolder mHolder;
	private Camera mCamera;
	
	
	public CamaraInstance(Context context,Camera camera){
		super(context);		
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		//mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   // Esta configuraci—n se requiere si es una api igual o menor a la 3.0
	}
	
	
	@Override
	public void surfaceChanged (SurfaceHolder holder, int format, int width, int height){
		if(mHolder.getSurface() ==null ){
			return;
		}
		try{
			mCamera.stopPreview();
		}catch(Exception e){
			
		}
		
		try{
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		}catch(Exception e){
			Log.d("Camara", "Error al iniciar la camara " + e.getMessage());
		}
	}
	
	
	
	@Override 
	public void surfaceCreated (SurfaceHolder holder){
		try{
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		}catch(IOException e){
			Log.d("Camara","Error de configuraci—n de la camara " + e.getMessage());
		}
		
	}
	
	@Override
	public void surfaceDestroyed (SurfaceHolder holder){
		
	}

}
