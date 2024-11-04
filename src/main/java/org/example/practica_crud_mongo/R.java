package org.example.practica_crud_mongo;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class R {
    public static URL getUI(String name) {
        return Thread.currentThread().getContextClassLoader().getResource("org" + File.separator + "example" + File.separator + "practica_crud_mongo" + File.separator + name);
    }
}
