package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    private boolean startGame = false;

    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
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

    private JButton playButton;
    private JButton pauseButton;
    private JButton continueButton;
    private JButton TextButton;


    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();


        frame.add(board,BorderLayout.CENTER);

        JPanel actionsPausePabel=new JPanel();
        actionsPausePabel.setLayout(new FlowLayout());

        playButton = new JButton("Play ");
        pauseButton = new JButton("Pause ");
        continueButton = new JButton("Continue ");
        TextButton = new JButton(" ");

        prepareActionGame();


        actionsPausePabel.add(playButton);
        actionsPausePabel.add(pauseButton);
        actionsPausePabel.add(continueButton);
        actionsPausePabel.add(TextButton);

        frame.add(actionsPausePabel,BorderLayout.SOUTH);


    }

    public void prepareActionGame() {
        playButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playGame();
            }
        });

        pauseButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pausedGame();
            }
        });
//
        continueButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                continueGame();
            }
        });


    }



    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {



        for (int i = 0; i != MAX_THREADS; i++) {

            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
//            thread[i].start();
        }

        frame.setVisible(true);

        for (int i=0; i < snakes.length; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    public void pausedGame() {

        int snakeBodyGreat = 0;
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes[i].pauseGame();
            if (snakes[i].getBody().size() > snakeBodyGreat) snakeBodyGreat = snakes[i].getBody().size();
        }
        System.out.println("SERPIENTE MAS GRANDE: " + snakeBodyGreat);
        System.out.println(Snake.snakeDead);
        TextButton.setText("SERPIENTE MAS GRANDE: " + snakeBodyGreat + " / PEOR SERPIENTE: " + Snake.snakeDead );

    }

    private void playGame() {
        if (!startGame) {
            for (int i = 0; i != MAX_THREADS; i++) {
                thread[i].start();
            }
            this.startGame = true;
        }
    }


    public void continueGame(){
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes[i].continueSnake();
        }
    }


}