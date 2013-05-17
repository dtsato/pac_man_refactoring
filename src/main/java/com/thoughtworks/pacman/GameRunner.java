package com.thoughtworks.pacman;

import com.thoughtworks.pacman.ui.Pacman;

public class GameRunner {
    public static void main(String[] args) throws Exception {
        Pacman game = new Pacman();
        game.play();
    }
}
