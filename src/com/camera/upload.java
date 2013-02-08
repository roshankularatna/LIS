package com.camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.map.GoogleMaps;
import com.search.Gsearch;
import com.session.Sessions;



public class upload extends Activity implements SurfaceHolder.Callback{
    /** Called when the activity is first created. */
	
	private SoapObject request;
	private SoapSerializationEnvelope envelope;
	boolean previewing = false;
	Camera camera;
	SurfaceHolder surfaceHolder;
	SurfaceView surfaceView;
	static byte[] imageArr;
	
	Button btnSearch;
	Button btnLocation;
	
	ImageView imgvw;
	TextView txDescription;
	
	private double longitude;
	private double latitude;
	private String search_item;
	private String description;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnSearch = (Button) findViewById(R.id.btnGsearch);
        btnLocation = (Button) findViewById(R.id.btnLocation);
    	
    	imgvw =  (ImageView) findViewById(R.id.imageView1);
    	txDescription = (TextView) findViewById(R.id.textDescription);
    	Bitmap btmapImage = null;
    	String description = null;
    	
    	if(((btmapImage=((Sessions)getApplication()).getBmImage())!=null)&&((description=((Sessions)getApplication()).getDescription())!=null))
    	{
    		imgvw.setImageBitmap(btmapImage);
    		txDescription.setText(description);
    	}
        
        getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
		Button captureButton = (Button) findViewById(R.id.button1);
	    captureButton.setOnClickListener(
	        new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {	
	            	
	            	imgvw.setImageBitmap(null);
	            	txDescription.setText("");
	                // get an image from the camera          	
	            	camera.takePicture(null, null, mPicture);
	            }
	        }
	    ); 
	    
		btnSearch = (Button) findViewById(R.id.btnGsearch);
		btnSearch.setOnClickListener(
	        new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {	
	            	//if(search_item!=null){
	            		gotoSearch();	
	            	//}
	            }
	        }
	    ); 
  
		btnLocation = (Button) findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(
	        new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {	
	            	//if(search_item!=null){
	            		gotoMap();	
	            	//}
	            }
	        }
	    ); 
  
        
    }
    
    public void gotoSearch(){
    	
    	Intent in = new Intent();
    	//in.putExtra("longitude",(int)(longitude*1e6));
    	//in.putExtra("latitude",(int)(latitude*1e6));
    	//in.putExtra("search_item",search_item);
    	in.setClass(this,Gsearch.class);
    	startActivity(in);
    	
    }
    
    public void gotoMap(){
    	
    	Intent in = new Intent();
    	//in.putExtra("longitude",Integer.toString((int)(longitude*1e6)));
    	//in.putExtra("latitude",Integer.toString((int)(latitude*1e6)));
    	//in.putExtra("search_item",search_item);
    	in.setClass(this,GoogleMaps.class);
    	startActivity(in);
    	
    }
    
    
    public void setVisible(){
    	
    	//btnSearch.setVisibility(View.VISIBLE);
    	//btnLocation.setVisibility(View.VISIBLE);
    	
    	imgvw.setVisibility(View.VISIBLE);
    	txDescription.setVisibility(View.VISIBLE);
    }

    
    private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {

	    	try{
              	Webservice wb = new Webservice();
              	wb.METHOD_NAME = "uploadImage";
              	wb.SOAP_ACTION = "http://calculator.me.org/"+wb.METHOD_NAME;
              	
              	
              	Bitmap	bm = BitmapFactory.decodeByteArray(data,0,data.length);
              	
              	int width = bm.getWidth();
              	int height = bm.getHeight();
              	float scaleWidth = ((float) 256) / width;
              	float scaleHeight = ((float) 256) / height;
              	Matrix matrix = new Matrix();
              	matrix.postRotate(90);
              	matrix.postScale(scaleWidth, scaleHeight);
              	Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
              	              	
              	ByteArrayOutputStream stream = new ByteArrayOutputStream();
              	resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
              	byte[] byteArray = stream.toByteArray();
                
                String ba1=Base64.encodeToString(byteArray, Base64.DEFAULT);
     
              	request =wb.getRequest();
              	request.addProperty("uploadImage",ba1);
              	
              	showPrograss("Processing..."); 
              	envelope = wb.setRequestAndGetEnvilop(request); 
              	SoapObject  soap = wb.getAll(envelope);    
              	
              	if(soap.getPropertyCount()>0)
              	{
	              	for(int i=0;i<soap.getPropertyCount();i++)
	              	{
	                	SoapObject soap2 = (SoapObject) soap.getProperty(i);
	                	String image  =  soap2.getProperty("image").toString();	
	                	
	                	byte[] newImage = Base64.decode(image,Base64.DEFAULT );
	                	Bitmap	bmnew = BitmapFactory.decodeByteArray(newImage,0,newImage.length);
	                	
	                	imgvw = (ImageView) findViewById(R.id.imageView1);
	                	imgvw.setImageBitmap(bmnew);
	                  	
	                  	search_item = soap2.getProperty("imKeywords").toString();
	                  	longitude = Double.parseDouble(soap2.getProperty("imLongitude").toString());
	                  	latitude = Double.parseDouble(soap2.getProperty("imLatitude").toString());
	                  	description = soap2.getProperty("imDescription").toString();
	                  	
	                  	Log.d("upload",search_item);
	                  	txDescription.setText(description);
	                  	
	                  	((Sessions)getApplication()).setBmImage(bmnew); //set session
	                  	((Sessions)getApplication()).setSearch_item(search_item);
	                  	((Sessions)getApplication()).setLongitude(longitude);
	                  	((Sessions)getApplication()).setLatitude(latitude);
	                  	((Sessions)getApplication()).setDescription(description);
	                  	
	                  	Log.d("upload......",((Sessions)getApplication()).getSearch_item());
	                  	
	                  	setVisible();
	                }
              	}else{
              		showDialog("No matching places");
              	}
              }catch(Exception ex){
            	  
            	  Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
            	  Log.d("exptn", ex.getMessage());
            	  
              }
	    }
	};
	
	
	public void showDialog(String msg){
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("Message");
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(msg);
		dialog.show();
		
		new Thread() 
		{
			public void run() 
			{
				try{
					sleep(2000);
				} catch (Exception e){}
				
				dialog.dismiss();
			}
			
		}.start();
		
	}

	public void showPrograss(String msg){
		
	  final ProgressDialog dialog = new ProgressDialog(this);  
      dialog.setCancelable(true);
      dialog.setMessage(msg);
      dialog.show();
      
      new Thread() {
    	  public void run() {
    		  try{
    			  sleep(1000);
    		  } catch (Exception e) {}
    		  dialog.dismiss();
    	  }
  	  }.start();
  	  
	}


	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!previewing) {
			camera = Camera.open();
			if (camera != null) {
				try {
		    		setDisplayOrientation(camera, 90);
		    		camera.setPreviewDisplay(surfaceHolder);
		    		camera.startPreview();
					
					previewing = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	protected void setDisplayOrientation(Camera camera, int angle){
	    Method downPolymorphic;
	    try
	    {
	        downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
	        if (downPolymorphic != null)
	            downPolymorphic.invoke(camera, new Object[] { angle });
	    }
	    catch (Exception e1)
	    {
	    }
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (camera != null && previewing) {
			camera.stopPreview();
			camera.release();
			camera = null;

			previewing = false;
		}
		
	}
}