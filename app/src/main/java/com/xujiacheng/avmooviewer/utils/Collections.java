package com.xujiacheng.avmooviewer.utils;

import android.util.Log;

import com.xujiacheng.avmooviewer.MainActivity;
import com.xujiacheng.avmooviewer.itembean.Av;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Collections {
    private static final String TAG = "Collections";


    public static boolean addToCollection(Av av) {
        try {
            String[] strings = av.url.split("/");
            File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(av);
            objectOutputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            getExistingCollections();
        }
    }

    public static ArrayList<Av> getExistingCollections() {
        ArrayList<Av> avs = new ArrayList<>();
        File[] files = MainActivity.CollectionDir.listFiles();
        if (files != null) {
            Log.d(TAG, "getExistingCollections: collection size = " + files.length);
            for (File file : files) {
                Log.d(TAG, "getExistingCollections: collection = " + file.toString());
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    Av av = (Av) objectInputStream.readObject();
                    avs.add(av);

                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
        }
        return avs;

    }


    public Collections() {

    }

    public static boolean removeCollection(String url) {
        String[] strings = url.split("/");
        File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
        boolean delete = file.delete();
        if (delete) {
            getExistingCollections();
            return true;
        } else {
            return false;
        }
    }

    public static Av checkCollections(String url) {
        String[] strings = url.split("/");
        File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                return (Av) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }
}