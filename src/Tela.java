
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.*;

public class Tela extends JPanel implements ActionListener {
    private static final int alturaTela = 800;
    private static final int larguraTela = 1200;
    private static final int tamBloco = 45;
    private static final int unidades = larguraTela * alturaTela / (tamBloco * tamBloco);

    private final int[] eixoX = new int[unidades];
    private final int[] eixoY = new int[unidades]; 

    private int corpoCobra = 6;
    private int blocosComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D';
    // W - cima | S - baixo | A - esquerda | D - direita
    private boolean executando = false;
    private boolean jogoReiniciado = false;
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

    public void executar () {
        criarMaca();
        executando = true;
        timer = new Timer(115, this);
        timer.start();
    }

    public void reiniciarJogo() {
        corpoCobra = 6;
        blocosComidos = 0;
        direcao = 'D';
        executando = true;
        jogoReiniciado = true;
        criarMaca();
        timer.restart();  // Reinicie o timer
    }

    private void criarMaca () {
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
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(String.valueOf(blocosComidos), 10, 35);

        } else {
            encerrar(g);
        }   
    }

    public void encerrar (Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Pontuação: " +blocosComidos, 525, 440);
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 65));
        g.drawString("You lost", 470, alturaTela / 2);
        timer.stop();
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (executando) {
            andar();
            alcancarBloco();
            inserirLimites();
        } else {
            encerrar(getGraphics());
        }
        repaint();
        jogoReiniciado = false;
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
            criarMaca();
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
                    case KeyEvent.VK_SPACE:
                        if (!executando) {
                            reiniciarJogo();
                        }
                        break;
                default:
                    break;
            }
        }
    }
}
