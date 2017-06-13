package com.example.imanh.rend10;

/**
 * Created by ImanH on 6/13/2017.
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.content.SyncAdapterType;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import java.util.Random;

/**
 *  OpenGL Custom renderer used with GLSurfaceView
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    Context context;   // Application's context
    static int Ng=8;
    static int N=Ng*Ng;
    Square[] quad=new Square[N];
    int W,H;
    boolean dimset=false;
   // Square t;
    World w;


    // Constructor with global application context
    public MyGLRenderer(Context context) {
        this.context = context;
        //quad = new Square();         // ( NEW )
        for(int i=0;i<N;i++){
            quad[i]=new Square();
        }
        w=new World(Ng);
    }

    // Call back when the surface is first created or re-created
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        for(int i=0;i<N;i++){
            quad[i].loadTexture(gl,context);
        }
       // t.loadTexture(gl,context);
        //quad.loadTexture(gl, context);    // Load image into Texture (NEW)
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)
        // You OpenGL|ES initialization code here
        // ......
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;
        if(!dimset){
            this.W=width;
            this.H=height;
            Log.d("myTag", "W is"+W+" H is "+H);
            dimset=true;
        }

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {

        // Clear color and depth buffers using clear-values set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();                 // Reset model-view matrix ( NEW )
    //    gl.glTranslatef(0.0f,0.0f,-7.0f);
        //t.scale(1.001f);
      w.tick();
        gl.glTranslatef(-2.4f,-3.0f,-10.0f);
        for(int i=0;i<N;i++){
            int jx=i/Ng;
            int ix=i%Ng;
            int lt=jx*(Ng+1)+ix;
            int rt=jx*(Ng+1)+ix+1;
            int lb=(jx+1)*(Ng+1)+ix;
            int rb=(jx+1)*(Ng+1)+ix+1;
         //   Log.d("myTag", "fuck "+lb+" "+rb+" "+lt+" "+rt);

            quad[i].updatesq(w.nodes[lb],w.nodes[rb],w.nodes[lt],w.nodes[rt]);

            quad[i].draw(gl);
        }


//        gl.glTranslatef(-1.5f, 0.0f, -6.0f); // Translate left and into the screen ( NEW )


        // Translate right, relative to the previous translation ( NEW )
//        gl.glTranslatef(3.0f, 0.0f, 0.0f);
//        for(int i=0;i<N;i++){
//            gl.glTranslatef(-0.4f,0.0f,0.0f);
//            float value = random.nextFloat() * 0.3f;
//            quad[i].vertices[0]+=value;
//            quad[i].RelV();
//            quad[i].draw(gl);
//
//            //gl.glLoadIdentity();
//        }
    }
}
