package com.example.imanh.rend10;

import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * Created by ImanH on 6/13/2017.
 */

public class Particle {
    World w;
    public float pos[];
    public float acc[];
    public float vel[];
    public float npos[];
    int i,j;
    public Particle(World w,int i,int j){
        this.w=w;
        this.pos=new float[3];
        this.acc=new float[3];
        this.vel=new float[3];
        this.npos=new float[3];
        this.i=i;
        this.j=j;
    }
    public void upd1(){
        for(int i=0;i<3;i++){
            vel[i]+=acc[i]*World.dt*0.5f;
            npos[i]+=vel[i]*World.dt;
        }
        acc[0]=0.0f;acc[1]=0.0f;acc[2]=0.0f;
        Random rand=new Random();
        acc[2]=w.windBase+(rand.nextFloat()-0.5f)*World.windDom;
        acc[1]=w.grav;
        acc[0]-=w.gamma*vel[0];
                acc[1]-=w.gamma*vel[1];
                        acc[2]-=w.gamma*vel[2];
        if(i-1>-1) {
            addacc(w.nodes[j*w.Ng+(i-1)]);
        }
        if(i+1<w.Ng){
            addacc(w.nodes[j*w.Ng+i+1]);
        }
        if(j-1>-1){
            addacc(w.nodes[(j-1)*w.Ng+i]);
        }
        if(j+1<w.Ng){
            addacc(w.nodes[(j+1)*w.Ng+i]);
        }
        for(int i=0;i<3;i++){
            vel[i]+=acc[i]*World.dt*0.5f;
        }
    }
    public void upd2(){
        pos[0]=npos[0];
        pos[1]=npos[1];
        pos[2]=npos[2];
    }
    public void addacc(Particle p){
        float dx=p.pos[0]-npos[0];
        float dy=p.pos[1]-npos[1];
        float dz=p.pos[2]-npos[2];
        float dist2=dx*dx+dy*dy+dz*dz;
        float dist=(float)sqrt(dist2);
        float deltal=dist-World.freel;
        float force=World.krig*deltal;
        acc[0]+=force*dx/dist;
        acc[1]+=force*dy/dist;
        acc[2]+=force*dz/dist;
    }

}
