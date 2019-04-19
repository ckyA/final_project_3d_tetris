package com.cky.a3dtetris.rank;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RankManager {

    private final String filePath;
    private ArrayList<RankItem> rankList;

    public static final String FILE_RANK = "FILE_RANK";
    private static RankManager manager;

    private RankManager(String filePath) {
        this.filePath = filePath;
        loadList();
    }

    public static RankManager create(String filePath) {
        manager = new RankManager(filePath);
        return manager;
    }

    public static void destroy() {
        manager.saveList();
        manager = null;
    }

    public static RankManager getManager() {
        return manager;
    }

    public ArrayList<RankItem> getRankList() {
        if (rankList == null) {
            rankList = new ArrayList<>();
        }
        return rankList;
    }

    public void rank() {
        if (rankList == null) {
            rankList = new ArrayList<>();
        } else {
            // rank
            for (int i = 0; i < rankList.size(); i++) {
                int maxIndex = i;
                for (int j = i; j < rankList.size(); j++) {
                    if (rankList.get(j).getScore() > rankList.get(maxIndex).getScore()) {
                        maxIndex = j;
                    }
                }
                RankItem temp = rankList.get(i);
                rankList.set(i, rankList.get(maxIndex));
                rankList.set(maxIndex, temp);
            }
        }
    }

    public void saveList() {
        if (rankList == null) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(rankList);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadList() {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object res = objectInputStream.readObject();
            if (res instanceof ArrayList) {
                rankList = (ArrayList<RankItem>) res;
            }
            fileInputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
