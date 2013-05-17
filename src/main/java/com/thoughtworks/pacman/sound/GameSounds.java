package com.thoughtworks.pacman.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/* This class controls all sound effects*/
public class GameSounds implements Sounds {

    private final Clip nomNom;
    private final Clip newGame;
    private final Clip death;
    private boolean stopped;

    /* Initialize audio files */
    public GameSounds() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        stopped = true;
        nomNom = AudioSystem.getClip();
        nomNom.open(AudioSystem.getAudioInputStream(this.getClass().getResource("nomnom.wav")));

        newGame = AudioSystem.getClip();
        newGame.open(AudioSystem.getAudioInputStream(this.getClass().getResource("newGame.wav")));

        death = AudioSystem.getClip();
        death.open(AudioSystem.getAudioInputStream(this.getClass().getResource("death.wav")));
    }

    /* Play pacman eating sound */
    public void nomNom() {
        /* If it's already playing, don't start it playing again!*/
        if (!stopped)
            return;

        stopped = false;
        nomNom.stop();
        nomNom.setFramePosition(0);
        nomNom.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /* Stop pacman eating sound */
    public void nomNomStop() {
        stopped = true;
        nomNom.stop();
        nomNom.setFramePosition(0);
    }

    /* Play new game sound */
    public void newGame() {
        newGame.stop();
        newGame.setFramePosition(0);
        newGame.start();
    }

    /* Play pacman death sound */
    public void death() {
        death.stop();
        death.setFramePosition(0);
        death.start();
    }
}
