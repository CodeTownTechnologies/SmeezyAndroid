package com.app.smeezy.interfacess;

/**
 * Created by GAURAV on 10/30/2017.
 */

public interface DrawableClickListener {

    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}