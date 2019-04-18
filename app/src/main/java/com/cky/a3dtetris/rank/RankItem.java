package com.cky.a3dtetris.rank;

import java.io.Serializable;

public class RankItem implements Serializable {
    private String name;
    private int score;

    public RankItem(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
