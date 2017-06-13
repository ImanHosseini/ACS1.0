package com.example.imanh.rend10;

import java.util.Random;

/**
 * Created by ImanH on 6/13/2017.
 */

public class World {

    public static float dt=0.1f;
    static float windDom=0.004f;
     float windBase=0.01f;
    float prob=0.02f;
    static float krig=1.0f;
    static float grav=0.004f;
    static float gamma=0.2f;
    static float l=5.0f;
    static float freel;
    int Ng;
    int Np;
    public Particle[] nodes;
    public World(int Ng){
        this.Ng=Ng+1;
        this.Np=this.Ng*this.Ng;
        nodes=new Particle[Np];
        for(int i=0;i<Np;i++){
            nodes[i]=new Particle(this,i%this.Ng,i/this.Ng);
        }
        freel=l/(float)this.Ng;
        int ind=0;
        for(int j=0;j<this.Ng;j++){
            for(int i=0;i<this.Ng;i++){
            nodes[ind].pos[0]=((float)i/(float)this.Ng)*l;
                nodes[ind].pos[1]=((float)j/(float)this.Ng)*l;
                nodes[ind].npos[0]=nodes[ind].pos[0];
                nodes[ind].npos[1]=nodes[ind].pos[1];
                ind++;
            }
        }
    }
    public void tick(){
        Random r=new Random();
        if(r.nextFloat()<prob){
            windBase*=(-1.0f);
        }
        if(r.nextFloat()<0.1){
            windBase+=0.009f;
        }
        if(r.nextFloat()>0.1){
            windBase-=0.001f;
        }
        for(int i=this.Ng;i<Np;i++){
            nodes[i].upd1();
        }
        for(int i=this.Ng;i<Np;i++){
            nodes[i].upd2();
        }
    }
}
