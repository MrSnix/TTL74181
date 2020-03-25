package com.ttl.alu.gui.utils;

import javafx.scene.image.Image;

import java.io.IOException;

public enum Diagram {

    LOGIC("/img/svg/views/standard/74181.png"), LAYERS("/img/svg/views/layers/74181.png");

    public static final double SCALE_RATIO = 1.375;

    private final String pathname;
    private Image img;

    Diagram(String pathname) {
        this.pathname = pathname;
    }

    public String pathname() {
        return pathname;
    }

    public void setIMG(Diagram e) throws IOException {
        this.img = new Image(getClass().getResource(e.pathname).openStream());
    }

    public Image getIMG() {
        return img;
    }
}
