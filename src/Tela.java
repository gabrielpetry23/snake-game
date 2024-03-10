import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class Tela extends JPanel implements ActionListener{
    private static final int alturaTela = 800;
    private static final int larguraTela = 1200;
    private static final int tamBloco = 45;
    private static final int unidades = larguraTela * alturaTela / (tamBloco * tamBloco);
    private static final int intervalo = 100;
    private static final String nomeFonte = "Ink Free";

    private final int[] eixoX = new int[unidades];
    private final int[] eixoY = new int[unidades]; 

    private int corpoCobra = 6;
    private int blocosComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D';
    // W - cima | S - baixo | A - esquerda | D - direita
    private boolean executando = false;
    Timer timer;
    Random random; //gerador do número do bloco

    Tela () {
        random = new Random();
        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        addKeyListener(new LeitorDeTeclas());
        setFocusable(true);
        executar();
    }

    public void reiniciar () {
        corpoCobra = 6;
        blocosComidos = 0;
        direcao = 'D';
        executando = true;
        criarBloco();
        for (int i = 0; i < corpoCobra; i++) {
            eixoX[i] = larguraTela / 2 - i * tamBloco;
            eixoY[i] = alturaTela / 2;
        }
        timer.restart(); 
    }

    public void executar () {
        criarBloco();
        executando = true;
        timer = new Timer(intervalo, this);
        timer.start();
    }

    private void criarBloco () {
        blocoX = random.nextInt(larguraTela / tamBloco) * tamBloco;
        blocoY = random.nextInt(alturaTela / tamBloco) * tamBloco;
    }

    public void desenharTela (Graphics g) {
        if (executando) {
            g.setColor(Color.red);
            g.fillOval(blocoX, blocoY, tamBloco, tamBloco); //bloco a ser comido

            for (int i = 0; i < corpoCobra; i++) { //desenho da cobra
                if (i == 0) { //se for cabeça da cobra
                    g.setColor(Color.green);
                    g.fillRect(eixoX[0], eixoY[0], tamBloco, tamBloco);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(eixoX[i], eixoY[i], tamBloco, tamBloco);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font(nomeFonte, Font.BOLD, 30));
            FontMetrics m = getFontMetrics(g.getFont());
            g.drawString(Integer.toString(blocosComidos), (larguraTela - m.stringWidth(Integer.toString(blocosComidos))) / 2, g.getFont().getSize());

        } else {
            encerrar(g);
        }   
    }

    public void encerrar (Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font(nomeFonte, Font.BOLD, 30));
        FontMetrics fontePoints = getFontMetrics(g.getFont());
        g.drawString("Pontuação: " +blocosComidos, (larguraTela - fontePoints.stringWidth("Pontuação: " +blocosComidos)) / 2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font(nomeFonte, Font.BOLD, 70));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("You lost", (larguraTela - fm.stringWidth("You lost")) / 2, alturaTela / 2);
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    public void actionPerformed (ActionEvent e) {
        if (executando) {
            andar();
            alcancarBloco();
            inserirLimites();
        } 
        repaint();
    }

    private void andar () {
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        switch (direcao) {
            case 'W':
                eixoY[0] = eixoY[0] - tamBloco;
                break;
            case 'S':
                eixoY[0] = eixoY[0] + tamBloco;
                break;
            case 'A':
                eixoX[0] = eixoX[0] - tamBloco;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + tamBloco;
                break;
            default:
                break;
        }
    }

    private void alcancarBloco () {
        if (eixoX[0] == blocoX && eixoY[0] == blocoY) {
            corpoCobra++;
            blocosComidos++;
            criarBloco();
        }
    }

    private void inserirLimites () {
        for (int i = corpoCobra; i > 0; i--) { 
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) { //se cabeça tocou corpo
                executando = false;
                break;
            }
        }

        if (eixoX[0] < 0 || eixoX[0] > larguraTela) { //se cabeça tocou as bordas esq ou dir
            executando = false;
        }

        if (eixoY[0] < 0 || eixoY[0] > alturaTela) { //se cabeça tocou teto ou chão
            executando = false;
        }

        if (!executando) {
            timer.stop();
        }
    }

    public class LeitorDeTeclas extends KeyAdapter {
        @Override
        public void keyPressed (KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'A';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'A') {
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'S') {
                        direcao = 'W';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'W') {
                        direcao = 'S';
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
