package com.theodore.aero.core;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class Files {

    public Files() {

    }

    public File internal(String fileName) {
        return new File("res/" + fileName);
    }

    public File external(String fileName) {
        try {
            return new File(new URI("file:///" + fileName));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        System.err.println("Couldn't find the specific file: " + fileName);
        return null;
    }

    public File external(URI uri) {
        return new File(uri);
    }
}
