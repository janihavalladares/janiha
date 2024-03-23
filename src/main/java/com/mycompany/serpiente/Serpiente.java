
package com.mycompany.serpiente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Serpiente extends JPanel implements KeyListener {
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_SPEED = 100; // Milisegundos entre movimientos

    private ArrayList<Point> snake;
    private Point apple;
    private int score;
    private boolean gameOver;
    private int dx, dy; // Dirección de movimiento
    private Timer timer;

    public Serpiente() {
        setPreferredSize(new Dimension(BOARD_WIDTH * UNIT_SIZE, BOARD_HEIGHT * UNIT_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);

        snake = new ArrayList<>();
        snake.add(new Point(5, 5)); // Posición inicial de la serpiente
        apple = generateRandomPoint();
        score = 0;
        gameOver = false;
        dx = 1;
        dy = 0;

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");

        actionMap.put("up", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (dy == 0) {
                    dx = 0;
                    dy = -1;
                }
            }
        });
        actionMap.put("down", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (dy == 0) {
                    dx = 0;
                    dy = 1;
                }
            }
        });
        actionMap.put("left", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (dx == 0) {
                    dx = -1;
                    dy = 0;
                }
            }
        });
        actionMap.put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (dx == 0) {
                    dx = 1;
                    dy = 0;
                }
            }
        });
        actionMap.put("space", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    resetGame();
                }
            }
        });

        timer = new Timer(GAME_SPEED, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move();
                checkCollisions();
                repaint();
            }
        });
        timer.start();
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x + dx, head.y + dy);
        snake.add(0, newHead);
        if (!newHead.equals(apple)) {
            snake.remove(snake.size() - 1); // Si no comió la manzana, elimina la cola
        } else {
            score += 10; // Aumenta el puntaje
            apple = generateRandomPoint(); // Genera una nueva manzana
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);
        // Colisión con el borde del tablero
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver = true;
            timer.stop();
            return;
        }
        // Colisión con el cuerpo de la serpiente
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                timer.stop();
                return;
            }
        }
    }

    private Point generateRandomPoint() {
        Random random = new Random();
        int x = random.nextInt(BOARD_WIDTH);
        int y = random.nextInt(BOARD_HEIGHT);
        return new Point(x, y);
    }

    public void resetGame() {
        snake.clear();
        snake.add(new Point(5, 5)); // Posición inicial de la serpiente
        apple = generateRandomPoint();
        score = 0;
        gameOver = false;
        dx = 1;
        dy = 0;
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            drawGameOverScreen(g);
            return;
        }
        drawSnake(g);
        drawApple(g);
        drawScore(g);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * UNIT_SIZE, point.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(apple.x * UNIT_SIZE, apple.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, getWidth() - 80, 20);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Game Over!", getWidth() / 2 - 80, getHeight() / 2 - 12);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Press SPACE to play again", getWidth() / 2 - 120, getHeight() / 2 + 20);
        g.drawString("Score: " + score, getWidth() / 2 - 40, getHeight() / 2 + 50);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Serpiente");
        Serpiente game = new Serpiente();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
