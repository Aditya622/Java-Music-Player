package com.java;
import javazoom.jl.player.Player;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MusicPlayer implements ActionListener {

    JFrame frame;

    JLabel songName;

    JButton select;

    JPanel playerPanel, controlPanel;

    Icon iconPlay, iconPause, iconResume, iconStop;

    JButton play, pause, resume, stop;

    JFileChooser fileChooser;
    FileInputStream fileInputStream;
    BufferedInputStream bufferedInputStream;
    File myFile = null;
    String filename, filePath;
    long totalLength, pauseLength;
    Player player;
    Thread playThread, resumeThread;

    public MusicPlayer() {

        initUI();
        addActionEvents();

        playThread = new Thread(runnablePlay);
        resumeThread = new Thread(runnableResume);

    }

    public void initUI() {

        songName = new JLabel("", SwingConstants.CENTER);

        select = new JButton("Select Mp3");

        playerPanel = new JPanel();
        controlPanel = new JPanel(); 

        iconPlay = new ImageIcon("D:\\Actual Content\\Study\\College\\4th Sem\\Java\\Project\\Project22\\MusicPlayer\\music-player-icons\\play-button.png");
        iconPause = new ImageIcon("D:\\Actual Content\\Study\\College\\4th Sem\\Java\\Project\\Project22\\MusicPlayer\\music-player-icons\\pause-button.png");
        iconResume = new ImageIcon("D:\\Actual Content\\Study\\College\\4th Sem\\Java\\Project\\Project22\\MusicPlayer\\music-player-icons\\resume-button.png");
        iconStop = new ImageIcon("D:\\Actual Content\\Study\\College\\4th Sem\\Java\\Project\\Project22\\MusicPlayer\\music-player-icons\\stop-button.png");

        play = new JButton(iconPlay);
        pause = new JButton(iconPause);
        resume = new JButton(iconResume);
        stop = new JButton(iconStop);
        
        playerPanel.setLayout(new GridLayout(2, 1));

        playerPanel.add(select);
        playerPanel.add(songName);

        controlPanel.setLayout(new GridLayout(1, 4));

        controlPanel.add(play);
        controlPanel.add(pause);
        controlPanel.add(resume);
        controlPanel.add(stop);

        play.setBackground(Color.WHITE);
        pause.setBackground(Color.WHITE);
        resume.setBackground(Color.WHITE);
        stop.setBackground(Color.WHITE);

        frame = new JFrame();

        frame.setTitle("Java Music Player");

        frame.add(playerPanel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setBackground(Color.white);
        frame.setSize(400, 200);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void addActionEvents() {
       
        select.addActionListener(this);
        play.addActionListener(this);
        pause.addActionListener(this);
        resume.addActionListener(this);
        stop.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(select)) {
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:\\Users"));
            fileChooser.setDialogTitle("Select Mp3");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files", "mp3"));
            if (fileChooser.showOpenDialog(select) == JFileChooser.APPROVE_OPTION) {
                myFile = fileChooser.getSelectedFile();
                filename = fileChooser.getSelectedFile().getName();
                filePath = fileChooser.getSelectedFile().getPath();
                songName.setText("File Selected : " + filename);
            }
        }
        
        if (e.getSource().equals(play)) {
            if (filename != null) {
                playThread.start();
                songName.setText("Now playing : " + filename);
            } else {
                songName.setText("No File was selected!");
            }
        }
        
        if (e.getSource().equals(pause)) {
            if (player != null && filename != null) {
                try {
                    pauseLength = fileInputStream.available();
                    player.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (e.getSource().equals(resume)) {
            if (filename != null) {
                resumeThread.start();
            } else {
                songName.setText("No File was selected!");
            }
        }
        
        if (e.getSource().equals(stop)) {
            if (player != null) {
                player.close();
                songName.setText("");
            }

        }

    }

    Runnable runnablePlay = new Runnable() {
        @Override
        public void run() {
            try {
                fileInputStream = new FileInputStream(myFile);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                player = new Player(bufferedInputStream);
                totalLength = fileInputStream.available();
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnableResume = new Runnable() {
        @Override
        public void run() {
            try {
                fileInputStream = new FileInputStream(myFile);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                player = new Player(bufferedInputStream);
                fileInputStream.skip(totalLength - pauseLength);
                player.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        MusicPlayer mp = new MusicPlayer();
    }
}