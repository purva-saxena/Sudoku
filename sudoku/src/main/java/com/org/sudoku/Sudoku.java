package com.org.sudoku;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

/**
 *
 * @author Purva Saxena
 */
public class Sudoku implements ActionListener, FocusListener {

    JFrame frame = new JFrame("sudoku");
    JTextField[][] number = new JTextField[9][9];
    JLabel label = new JLabel("Sudoku");
    JButton play, close, restart;
    int rowValidation[][] = new int[9][10];
    int columnValidation[][] = new int[9][10];
    int gridValidation[][] = new int[9][10];
    int filledCell = 0;

    public Sudoku() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(null);

        // Headline of the game
        label.setBounds(120, 70, 300, 200);
        label.setFont(new Font("Arial", Font.BOLD, 65));
        frame.add(label);

        //button to play game or exit the game
        play = new JButton("PLAY");
        play.setBounds(100, 250, 130, 40);
        play.addActionListener(this);
        play.setActionCommand("play");
        close = new JButton("EXIT");
        close.setBounds(250, 250, 130, 40);
        close.addActionListener(this);
        close.setActionCommand("exit");
        frame.add(play);
        frame.add(close);

        frame.setVisible(true);
    }

    void emptyGrid() {
        
        int i, j;
        for(i=0;i<9;i++){
            for(j=0;j<10;j++){
                rowValidation[i][j]=0;
                columnValidation[i][j]=0;
                gridValidation[i][j]=0;
            }
        }
        
        for (i = 0; i < 9; i++) {

            for (j = 0; j < 9; j++) {
                number[i][j].setText("");
                number[i][j].setEditable(true);
                number[i][j].setForeground(Color.BLACK);
            }
        }

    }

    int gridIndex(int i, int j) {

        int gi = 0;
        if (i >= 0 && i < 3) {
            if (j >= 0 && j < 3) {
                gi = 0;
            } else if (j >= 3 && j < 6) {
                gi = 1;
            } else if (j >= 6 && j < 9){
                gi = 2;
            }
        } else if (i >= 3 && i < 6) {
            if (j >= 0 && j < 3) {
                gi = 3;
            } else if (j >= 3 && j < 6) {
                gi = 4;
            } else if (j >= 6 && j < 9) {
                gi = 5;
            }
        } else if (i >= 6 && i < 9) {
            if (j >= 0 && j < 3) {
                gi = 6;
            } else if (j >= 3 && j < 6) {
                gi = 7;
            } else if (j >= 6 && j < 9){
                gi = 8;
            }
        }

        return gi;
    }

    int validate(int i, int j, int gridIndex, int val) {

        //condition for the case when user try to input number more than 9
        if (val > 9 || val <= 0) {
            return -1;
        }
        if (rowValidation[i][val] == 1 || columnValidation[j][val] == 1 || gridValidation[gridIndex][val] == 1) {
            return 0;
        }
        
        rowValidation[i][val] = 1;
        columnValidation[j][val] = 1;
        gridValidation[gridIndex][val] = 1;
        return 1;
    }

    void generateGrid() {
        // code for making all text fields empty
        emptyGrid();

        // code for genrating puzzle
        Random random = new Random();

        int i, j, val, n, gi;
        n = 0;
        while (n < 20) {
            i = random.nextInt(9);
            j = random.nextInt(9);
            val = random.nextInt(8) + 1;
            gi = gridIndex(i, j);
            int x = validate(i, j, gi, val);
            if (number[i][j].getText().equals("") && x == 1) {
                number[i][j].setText(Integer.toString(val));
                number[i][j].setEditable(false);
                number[i][j].setFocusable(false);
                n++;
                filledCell++;
            }
        }
    }

    void GenerateFrameForPlaying() {

        label.setVisible(false);
        play.setVisible(false);
        close.setBounds(250, 400, 130, 40);
        restart = new JButton("RESTART");
        restart.setBounds(100, 400, 130, 40);
        restart.addActionListener(this);
        restart.setActionCommand("restart");
        frame.add(restart);

        int x, y = 40, gi;
        for (int i = 0; i < number.length; i++) {
            x = 115;
            for (int j = 0; j < number.length; j++) {
                gi = gridIndex(i, j);
                number[i][j] = new JTextField();
                number[i][j].setHorizontalAlignment(JTextField.CENTER);
                number[i][j].setBounds(x, y, 30, 30);
                number[i][j].addFocusListener(this);
                number[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                number[i][j].putClientProperty("firstIndex", i);
                number[i][j].putClientProperty("secondIndex", j);
                number[i][j].putClientProperty("gridIndex", gi);
                frame.add(number[i][j]);
                x += 30;
            }
            y += 30;
        }

        generateGrid();
    }

    public static void main(String args[]) {

        Sudoku sukodu = new Sudoku();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("play")) {
            GenerateFrameForPlaying();
        } else if (action.equals("restart")) {
            generateGrid();
        } else if (action.equals("exit")) {
            frame.dispose();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        JTextField temp = (JTextField) e.getSource();
        int i = (Integer) temp.getClientProperty("firstIndex");
        int j = (Integer) temp.getClientProperty("secondIndex");
        int gi = (Integer) temp.getClientProperty("gridIndex");

        int val;
        String s = temp.getText();
        if (s.equals("")) {
            return;
        }
        try {
            val = Integer.parseInt(temp.getText());
        } catch (NumberFormatException ex) {
            System.out.println("INVALID INPUT");
            temp.setText("");
            return;
        }

        int x = validate(i, j, gi, val);
        switch (x) {
            case -1:
                System.out.println("INVALID INPUT");
                temp.setText("");
                break;
            case 0:
                temp.setForeground(Color.red);
                break;
            case 1:
                temp.setForeground(Color.green);
                filledCell++;
                break;
        }

        if (filledCell == 81) {
            System.out.println("YOU WON");
        }

    }

    @Override
    public void focusGained(FocusEvent e) {
        JTextField temp = (JTextField) e.getSource();
        if (temp.getText().equals("")) {
            return;
        }
        int i = (Integer) temp.getClientProperty("firstIndex");
        int j = (Integer) temp.getClientProperty("secondIndex");

        if (temp.getForeground() == Color.GREEN) {

            int gi = (Integer) temp.getClientProperty("gridIndex");
            int val = Integer.parseInt(temp.getText());
            rowValidation[i][val] = 0;
            columnValidation[j][val] = 0;
            gridValidation[gi][val] = 0;
            filledCell--;
        }
        number[i][j].setForeground(Color.black);

    }

}
