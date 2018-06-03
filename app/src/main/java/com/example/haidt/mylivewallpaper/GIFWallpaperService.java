package com.example.haidt.mylivewallpaper;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GIFWallpaperService extends WallpaperService {
    String URLgif;
    String nameGif;
    GIFWallpaperEngine gifWallpaperEngine;
    SharedPreferences preferences;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    @Override
    public Engine onCreateEngine() {
        try {
            preferences=getApplicationContext().getSharedPreferences(LiveImageView.MYPREF,MODE_PRIVATE);
            URLgif=preferences.getString(LiveImageView.URLIMAGELINK,"");
            nameGif=preferences.getString(LiveImageView.NAMEGIFIMAGE,"");
            File f= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+  "/LiveWallPaper/"+nameGif+".gif");
            if(f.exists()){
                Movie movie=Movie.decodeStream( new FileInputStream(f));
                return gifWallpaperEngine=new GIFWallpaperEngine(movie);
            }
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(URLgif);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(conn.getContentLength());
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            int nRead;
            byte[] data= new byte[1094720];
            while((nRead=bis.read(data,0,data.length))!=-1){
                byteArrayOutputStream.write(data,0,nRead);
            }
            byteArrayOutputStream.flush();
            byte[] bytes= byteArrayOutputStream.toByteArray();
            Movie movie= Movie.decodeByteArray(bytes,0, bytes.length);
            return gifWallpaperEngine=new GIFWallpaperEngine(movie);
        }catch(IOException e){
            Log.d("GIF", "Could not load asset");
            return null;
        }
    }

    public class GIFWallpaperEngine extends   WallpaperService.Engine {
        private final int frameDuration = 20;
        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;

        public GIFWallpaperEngine(Movie movie) {
            //A Handler allows you to send and process Message and Runnable objects associated
            //with a thread's MessageQueue
            this.movie = movie;
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            /** Abstract interface to someone holding a display surface. Allows you to control
             the surface size and format, edit the pixels in the surface, and monitor changes
             to the surface. This interface is typically available through the SurfaceView class.
             **/
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
            setTouchEventsEnabled(true);
        }
        //The Runnable interface should be implemented by any class whose instances are intended to
        // be executed by a thread. The class must define a method of no arguments called run.
        private Runnable drawGIF = new Runnable() {
            public void run() {
                draw();
            }
        };
        public void draw() {
            holder=getSurfaceHolder();
            if (visible) {
                Canvas canvas = holder.lockCanvas();
                canvas.save();
                float scale_width= (float) getScreenWidth()/(float) movie.width();
                float scale_height= (float) getScreenHeight()/(float) movie.height();
                canvas.scale(scale_width, scale_height);
                movie.draw(canvas, 0, 0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
                handler.removeCallbacks(drawGIF);
                handler.postDelayed(drawGIF, frameDuration);
            }
        }
        @Override
        public void onVisibilityChanged(boolean visible) {
            //Called when the visibility of the view or an ancestor of the view has changed.
            this.visible = visible;
            if (visible) {
                handler.post(drawGIF);
            } else {
                handler.removeCallbacks(drawGIF);
            }
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
           if(isVisible()){
               LiveImageView.IS_SET=true;
               }
           else {
               LiveImageView.IS_SET=false;
           }
        }

    }
}
