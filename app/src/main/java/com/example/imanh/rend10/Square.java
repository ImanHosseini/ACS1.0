package com.example.imanh.rend10;

/**
 * Created by ImanH on 6/13/2017.
 */


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

/*
 * A square drawn in 2 triangles (using TRIANGLE_STRIP). This square has one color.
 */
public class Square {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private FloatBuffer texBuffer;    // Buffer for texture-coords-array (NEW)
    int[] textureIDs = new int[1];   // Array for 1 texture-ID (NEW)
    float[] vertices = {  // Vertices for the square
            -1.0f, -1.0f,  0.0f,  // 0. left-bottom
            1.0f, -1.0f,  0.0f,  // 1. right-bottom
            -1.0f,  1.0f,  0.0f,  // 2. left-top
            1.0f,  1.0f,  0.0f   // 3. right-top
    };
    float[] texCoords = { // Texture coords for the above face (NEW)
            0.0f, 1.0f,  // A. left-bottom (NEW)
            1.0f, 1.0f,  // B. right-bottom (NEW)
            0.0f, 0.0f,  // C. left-top (NEW)
            1.0f, 0.0f   // D. right-top (NEW)
    };
    // Constructor - Setup the vertex buffer
    public Square() {
        // Setup vertex array buffer. Vertices in float. A float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind
        // Setup texture-coords-array buffer, in float. An float has 4 bytes (NEW)
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);
    }

    public void scale(float c){
        for(int i=0;i<12;i++){

            vertices[i]*=c;
        }
        RelV();
    }
    public void updatesq(Particle lb,Particle rb,Particle lt,Particle rt){
        vertices[0]=lb.pos[0];
        vertices[1]=lb.pos[1];
        vertices[2]=lb.pos[2];
        vertices[3]=rb.pos[0];
        vertices[4]=rb.pos[1];
        vertices[5]=rb.pos[2];
        vertices[6]=lt.pos[0];
        vertices[7]=lt.pos[1];
        vertices[8]=lt.pos[2];
        vertices[9]=rt.pos[0];
        vertices[10]=rt.pos[1];
        vertices[11]=rt.pos[2];
      //  Log.d("myTag", vertices[0]+" "+vertices[1]+" "+vertices[3]+" "+vertices[4]+" "+vertices[6]+" "+vertices[7]+" "+vertices[9]+" "+vertices[10]);
        RelV();
    }


    public void RelV(){

        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    // Render the shape
    public void draw(GL10 gl) {
        // Enable vertex-array and define its buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)
        // Draw the primitives from the vertex array directly
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    // Load an image into GL texture
    public void loadTexture(GL10 gl, Context context) {
        gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Construct an input stream to texture image "res\drawable\nehe.png"
        InputStream istream = context.getResources().openRawResource(R.drawable.nehe);
        Bitmap bitmap;
        try {
            // Read and decode input as bitmap
            bitmap = BitmapFactory.decodeStream(istream);
        } finally {
            try {
                istream.close();
            } catch(IOException e) { }
        }

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}
