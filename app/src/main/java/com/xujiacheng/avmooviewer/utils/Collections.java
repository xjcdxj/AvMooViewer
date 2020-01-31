package com.xujiacheng.avmooviewer.utils;

import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.itembean.Av;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Collections {
    private static final String TAG = "Collections";
    public static void addToCollection(Av av) {
        try {
            String[] strings = av.url.split("/");
            File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(av);
            objectOutputStream.close();
        } catch (IOException ignored) {
        } finally {
            getExistingCollections();
        }
    }

    public static ArrayList<Av> getExistingCollections() {
        ArrayList<Av> avs = new ArrayList<>();
        File[] files = MainActivity.CollectionDir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    Av av = (Av) objectInputStream.readObject();
                    objectInputStream.close();
                    avs.add(av);
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
        }
        return avs;

    }


    public Collections() {

    }

    public static void removeCollection(String url) {
        String[] strings = url.split("/");
        File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
        boolean delete = file.delete();
        getExistingCollections();
    }

    public static Av checkCollections(String url) {
        String[] strings = url.split("/");
        File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                Av av = (Av) objectInputStream.readObject();
                objectInputStream.close();
                return av;
            } catch (IOException | ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }
}
