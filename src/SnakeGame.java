import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int alturaTela = 800;
    private static final int larguraTela = 1000;
    private static final int tamBloco = 40;
    private static final int unidades = larguraTela * alturaTela / (tamBloco * tamBloco);
    private int qntPedras = 10;

    private final int[] eixoX = new int[unidades];
    private final int[] eixoY = new int[unidades]; 
    private int[] pedraX = new int [qntPedras];
    private int[] pedraY = new int[qntPedras];

    private int corpoCobra = 3;
    private int blocosComidos = 0;
    private int macaX;
    private int macaY;
    private char direcao = 'D';
    private boolean executandoDificil = false;
    private boolean cobraNaPedra = false;
    private boolean gameOver = false;

    Timer timer;
    Random random;
    
    SnakeGame () {
        random = new Random();
        criarPedra();
        setPreferredSize(new Dimension(larguraTela, alturaTela));
        setBackground(Color.BLACK);
        addKeyListener(new LeitorDeTeclas());
        setFocusable(true);
        menuFases();
    }

        public void menuFases() {
            JPanel painel = new JPanel();
            painel.setBackground(Color.BLACK);
            painel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(10, 0, 10, 0);
            JLabel text1 = new JLabel("Bem vindo ao Snake Game");
            JLabel text2 = new JLabel("Escolha a dificuldade que deseja jogar");
            text1.setFont(new Font("Arial", Font.BOLD, 24)); 
            text2.setFont(new Font("Arial", Font.BOLD, 18)); 
            text1.setForeground(Color.WHITE);
            text2.setForeground(Color.RED);

            setLayout(new BorderLayout()); 
            JButton botaoFacil = new JButton("Facil");
            JButton botaoMedio = new JButton("Médio");
            JButton botaoDificil = new JButton("Difícil");
            
            painel.add(text1, gbc);
            painel.add(text2, gbc);
            painel.add(botaoFacil, gbc);
            painel.add(botaoMedio, gbc);
            painel.add(botaoDificil, gbc);
    
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
        timer = new Timer(180, this);
        timer.start();
    }

    public void executarMedio () {
        criarMaca();
        timer = new Timer(100, this);
        timer.start();
    }

    public void executarDificil () {
        criarMaca();
        executandoDificil = true;
        timer = new Timer(110, this); 
        timer.start();
    }

    private void criarMaca () {
        macaX = random.nextInt(larguraTela / tamBloco) * tamBloco;
        macaY = random.nextInt(alturaTela / tamBloco) * tamBloco;
    }

    public void criarPedra () {
        for (int i = 0 ; i < qntPedras ; i++) {
            int[] posicao = gerarPosicao();
            pedraX[i] = posicao[0];
            pedraY[i] = posicao[1];
        }
    }
    
    public int[] gerarPosicao() {
        int x = 0;
        int y = 0;
        boolean posicaoValida = false;
    
        //Loop para achar posição válida, se achou armazena no vetor e retorna
        while (!posicaoValida) {
            x = random.nextInt(larguraTela / tamBloco) * tamBloco;
            y = random.nextInt(alturaTela / tamBloco) * tamBloco;
            posicaoValida = validarPosicao(x, y);
        }
    
        return new int[]{x, y};
    }
    
    public boolean validarPosicao(int x, int y) {
        //Condição cobra
        for (int j = 0; j < corpoCobra; j++) {
            if (x == eixoX[j] && y == eixoY[j]) {
                return false;
            }
        }
    
        //Condição maçã
        if (x == macaX && y == macaY) {
            return false;
        }
    
        return true; 
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
        if (executandoDificil) {
            for (int i = 0 ; i < qntPedras ; i++) {
                g.setColor(Color.GRAY);
                g.fillRect(pedraX[i], pedraY[i], tamBloco, tamBloco);
            }
            desenho(g);

        } else {
            desenho(g);
        }

        if (gameOver) {
            encerrar(g);
        }
    }

    public void encerrar (Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score: " +blocosComidos, 475, 440);
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 65));
        g.drawString("Game Over", larguraTela/3, alturaTela / 2);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("'SPACE' to try again", 430, 475);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        andar();
        comeuMaca();
        inserirLimites();
        
        if (gameOver) {
            timer.stop();
            repaint();
        } else {
            repaint();
        }
    }

    private void andar () {
        //Loop para corpo "acompanhar" cabeça 
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        //Atualiza as posições da cobra com base na direção atual
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
        }
    }

    private void inserirLimites () {
        //Condição da pedra
        if (executandoDificil) {
            for (int i = 0 ; i < qntPedras ; i++) {
                if (eixoX[0] == pedraX[i] && eixoY[0] == pedraY[i]) {
                    gameOver = true;
                    break;
                }
            }
        }

        //Condição do corpo
        for (int i = corpoCobra; i > 0; i--) { 
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                gameOver = true;
                break;
            }
        }

        //Condição das bordas
        if (eixoX[0] < 0 || eixoX[0] >= larguraTela) { //Esquerda ou Direita
            gameOver = true;
        }

        if (eixoY[0] < 0 || eixoY[0] >= alturaTela) { //Cima ou Baixo
            gameOver = true;
        }
    }

    public class LeitorDeTeclas extends KeyAdapter {
        //Interação do usuário e controle da cobra
        @Override
        public void keyPressed (KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') { //não permite que volte na direção oposta
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
                    if (gameOver) {
                        reiniciar();
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }

    public void reiniciar() {
        corpoCobra = 3;
        blocosComidos = 0;
        cobraNaPedra = false;
        gameOver = false;
        direcao = 'D';
        eixoX[0] = 0;
        eixoY[0] = 0;
        criarPedra();
        repaint();
        timer.start();
    }
}
