import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int alturaTela = 800;
    private static final int larguraTela = 1200;
    private static final int tamBloco = 45;
    private static final int unidades = larguraTela * alturaTela / (tamBloco * tamBloco);

    private final int[] eixoX = new int[unidades];
    private final int[] eixoY = new int[unidades]; 



    private int corpoCobra = 6;
    private int blocosComidos;
    private int macaX;
    private int macaY;
    private int pedraX;
    private int pedraY;
    private char direcao = 'D';
    private boolean executandoFacil = false;
    private boolean executandoMedio = false;
    private boolean executandoDificil = false;

    private int pedraX2;
    private int pedraY2;

    Timer timer;
    Random random;

    //criar classe criarPedra construtor passo numero que quero criar
    
    SnakeGame () {
        random = new Random();
        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        addKeyListener(new LeitorDeTeclas());
        setFocusable(true);
        menuFases();
        //executar();
    }

        public void menuFases() {
            JPanel painel = new JPanel();
            painel.setBackground(Color.BLACK);
            JLabel text1 = new JLabel("Bem vindo ao Snake Game");
            JLabel text2 = new JLabel("Escolha a dificuldade que deseja jogar");
            text1.setFont(new Font("Arial", Font.BOLD, 24)); // Ajuste o tamanho e o estilo da fonte conforme necessário
            text1.setHorizontalAlignment(JLabel.CENTER);
            text1.setVerticalAlignment(JLabel.CENTER); // Centraliza o texto verticalmente
            text1.setForeground(Color.WHITE);

            setLayout(new BorderLayout()); 
            JButton botaoFacil = new JButton("Facil");
            JButton botaoMedio = new JButton("Médio");
            JButton botaoDificil = new JButton("Difícil");
            
            painel.add(text1);
            painel.add(text2);
            painel.add(botaoFacil);
            painel.add(botaoMedio);
            painel.add(botaoDificil);
    
            botaoFacil.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(painel);
                    repaint();
                    executarFacil();
                }
            });
    
            botaoMedio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(painel);
                    repaint();
                    executarMedio();
                }
            });

            botaoDificil.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e) {
                    remove(painel);
                    repaint();
                    executarDificil();
                }
            });
            
            add(painel);
            setVisible(true);
        }

    public void executarFacil () {
        criarMaca();
        executandoFacil = true;
        timer = new Timer(90, this);
        timer.start();
    }

    public void executarMedio () {
        criarMaca();
        executandoMedio = true;
        timer = new Timer(120, this);
        timer.start();
    }

    public void executarDificil () {
        criarMaca();
        executandoDificil = true;
        timer = new Timer(125, this);
        timer.start();
    }

    private void criarMaca () {
        //Cria as maças em blocos aleatorios
        macaX = random.nextInt(larguraTela / tamBloco) * tamBloco;
        macaY = random.nextInt(alturaTela / tamBloco) * tamBloco;
    }

    public void criarPedra () {
        pedraX = random.nextInt(larguraTela / tamBloco) * tamBloco;
        pedraY = random.nextInt(alturaTela / tamBloco) * tamBloco;
    }

    private void criarPedra2 () {
        pedraX2 = random.nextInt(larguraTela / tamBloco) * tamBloco;
        pedraY2 = random.nextInt(alturaTela / tamBloco) * tamBloco;
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    private void desenho (Graphics g) {
            //Maça
            g.setColor(Color.red);
            g.fillOval(macaX, macaY, tamBloco, tamBloco); 

            //Snake
            for (int i = 0; i < corpoCobra; i++) { 
                //Cabeça
                if (i == 0) { 
                    g.setColor(Color.green);
                    g.fillRect(eixoX[0], eixoY[0], tamBloco, tamBloco);
                } else {
                    //Corpo
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(eixoX[i], eixoY[i], tamBloco, tamBloco);
                }
            }

            //Score
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(String.valueOf(blocosComidos), 10, 35);
    }

    public void desenharTela (Graphics g) {

        if (executandoFacil || executandoMedio) {
            desenho(g);

        } else if (executandoDificil) {
            desenho(g);
            g.setColor(Color.GRAY);
            g.fillRect(pedraX, pedraY, tamBloco, tamBloco); 
            g.setColor(Color.GRAY);
            g.fillRect(pedraX2, pedraY2, tamBloco, tamBloco); 
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
    public void actionPerformed (ActionEvent e) {
        if (executandoFacil || executandoMedio || executandoDificil) {
            andar();
            comeuMaca();
            inserirLimites();
        }
        repaint();
    }

    private void andar () {
        //Loop para corpo "acompanhar" cabeça 
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

    private void comeuMaca () {
        if (eixoX[0] == macaX && eixoY[0] == macaY) {
            corpoCobra++;
            blocosComidos++;
            criarMaca();
            criarPedra();
            criarPedra2();
        }
    }

    private void inserirLimites () {
        //Condição da pedra
        if (eixoX[0] == pedraX && eixoY[0] == pedraY || eixoX[0] == pedraX2 && eixoY[0] == pedraY2) {
            executandoDificil = false;
        }

        //Condição do corpo
        for (int i = corpoCobra; i > 0; i--) { 
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                executandoFacil = false;
                executandoMedio = false;
                executandoDificil = false;
                break;
            }
        }

        //Condição das bordas
        if (eixoX[0] < 0 || eixoX[0] > larguraTela) { //Esquerda ou Direita
            executandoFacil = false;
            executandoMedio = false;
            executandoDificil = false;
        }

        if (eixoY[0] < 0 || eixoY[0] > alturaTela) { //Cima ou Baixo
            executandoFacil = false;
            executandoMedio = false;
            executandoDificil = false;
        }
        /*if (!executandoFacil) {
            timer.stop();
        } 

        if (!executandoMedio) {
            timer.stop();
        } */
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
