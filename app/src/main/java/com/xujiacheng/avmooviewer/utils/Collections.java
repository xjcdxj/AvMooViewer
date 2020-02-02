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
import java.util.Arrays;
import java.util.Comparator;

public class Collections {
    private static ArrayList<Av> collections;
//    private static final String TAG = "Collections";

    public static ArrayList<Av> getCollections() {
        if (collections == null) {
            collections = getExistingCollections();
        }
        return collections;
    }

    public static void addToCollection(Av av) {
        try {
            String[] strings = av.url.split("/");
            File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(av);
            objectOutputStream.close();
        } catch (IOException ignored) {
        } finally {
            collections = getExistingCollections();
        }
    }

    private static ArrayList<Av> getExistingCollections() {
        ArrayList<Av> avs = new ArrayList<>();
        File[] files = MainActivity.CollectionDir.listFiles();
        if (files != null && files.length > 0) {
            ArrayList<File> collectionFiles = new ArrayList<>(Arrays.asList(files));
            java.util.Collections.sort(collectionFiles, new Comparator<File>() {
                @Override
                public int compare(File file, File newFile) {
                    return Long.compare(newFile.lastModified(), file.lastModified());
                }
            });
            for (File file : collectionFiles) {
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


    private Collections() {

    }

    public static boolean removeCollection(String url) {
        String[] strings = url.split("/");
        File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
        boolean delete = file.delete();
        collections = getExistingCollections();
        return delete;
    }

    public static boolean checkCollections(String url) {
        for (Av av : getCollections()) {
            if (av.url.equals(url)) {
                return true;
            }
        }
        return false;
//        File file = new File(MainActivity.CollectionDir, strings[strings.length - 1]);
//        if (file.exists()) {
//            try {
//                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
//                Av av = (Av) objectInputStream.readObject();
//                objectInputStream.close();
//                return av;
//            } catch (IOException | ClassNotFoundException e) {
//                return null;
//            }
//        }
//        return null;
    }
}
