package com.mf.skiphand.config;

import net.minecraft.network.chat.Component;

public enum Location {
    TOP_LEFT(0,"topLeft"),
    TOP_CENTER(1,"topCenter"),
    TOP_RIGHT(2,"topRight"),
    BOTTOM_LEFT(3,"bottomLeft"),
    BOTTOM_RIGHT(4,"bottomRight");

    private final String hudLocationName;
    private final int idNum;


    private Location(int id,String hudLocation){
        this.idNum = id;
        this.hudLocationName = hudLocation;
    }


    public int getId(){
        return this.idNum;
    }

    public String getLocation(){
        return this.hudLocationName;
    }


}
