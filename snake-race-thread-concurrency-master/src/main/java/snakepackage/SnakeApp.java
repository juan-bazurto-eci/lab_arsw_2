package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jd-
 *
 */
public class SnakeApp {
    Snake first_death = null;
    JLabel largestLabel;
    JLabel firstDeathLabel;
    JButton startButton;
    JButton pauseButton;
    JButton continueButton;
    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    CopyOnWriteArrayList<Snake> snakes = new CopyOnWriteArrayList();
    //Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    public SnakeApp() {
        for (int i = 0; i < 8; i++) {
            snakes.add(null);
        }
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 60);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();


        frame.add(board, BorderLayout.CENTER);

        largestLabel = new JLabel("Largest Snake: ");
        firstDeathLabel = new JLabel("First Death: ");

        JPanel actionsBPabel=new JPanel();
        JPanel resultsPanel=new JPanel();

        startButton = new JButton("Start ");
        pauseButton = new JButton("Pause ");
        continueButton = new JButton("Continue ");
        resultsPanel.setLayout(new FlowLayout());
        actionsBPabel.setLayout(new FlowLayout());

        resultsPanel.add(largestLabel);
        resultsPanel.add(firstDeathLabel);
        actionsBPabel.add(startButton);
        actionsBPabel.add(pauseButton);
        actionsBPabel.add(continueButton);

        frame.add(actionsBPabel,BorderLayout.NORTH);
        frame.add(resultsPanel,BorderLayout.SOUTH);
        prepareListeners();
    }


    private void prepareListeners(){
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continue_game();
            }
        });
    }

    private void pause() {
        int biggest = 0;
        Snake biggest_snake = null;
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes.get(i).hold();
            if (snakes.get(i).getLarge() > biggest ) {
                biggest = snakes.get(i).getLarge();
                biggest_snake = snakes.get(i);
            }
        }
        largestLabel.setText("Largest Snake: " + biggest);
        if (first_death == null) {
            firstDeathLabel.setText("First Snake to die: No snake has died yet.");
        }
        else {
            firstDeathLabel.setText("First Snake to die: " + first_death.getIdt());
        }
    }

    private void continue_game() {
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes.get(i).restart();
        }
    }

    public void start() {
        for (int i = 0; i != MAX_THREADS; i++) {

            thread[i].start();
        }

    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes.set(i, new Snake(i + 1, spawn[i], i + 1));
            snakes.get(i).addObserver(board);
            thread[i] = new Thread(snakes.get(i));

            //thread[i].start();
        }

        frame.setVisible(true);

        while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes.get(i).isSnakeEnd() == true) {
                    x++;
                    if (first_death == null) {
                        first_death = snakes.get(i);
                    }
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }

}
