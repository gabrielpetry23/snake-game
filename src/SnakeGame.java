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
    private int blocoX;
    private int blocoY;
    private char direcao = 'D';
    private boolean executando1 = false;
    private boolean executando2 = false;

    Timer timer;
    Random random;
    /*Criar menu:
     poderia criar metodo no construtor do SnakeGame que chama executar1 se escolher opcao 1
     chama executar2 se escolhe opcao 2 etc

     no executar2 muda o time pra menos e executando2 = true
     ai no metodo desenharTela() muda condicao p se tiver executando2 = true desenha uma pedra e add if executando2 no inserirLimites() bota que se
     tocar na pedra perde

     teste 1 fazer fase 2 que so aumenta a velocidade, mas antes tenho que fazer o menu ver se funciona

     solução é cri
     */
    

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
            JButton botaoOpcao1 = new JButton("Opção 1");
            JButton botaoOpcao2 = new JButton("Opção 2");
            
            painel.add(text1);
            painel.add(text2);
            painel.add(botaoOpcao1);
            painel.add(botaoOpcao2);
    
            botaoOpcao1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(painel);
                    repaint();
                    executar1();
                }
            });
    
            botaoOpcao2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executar2();
                }
            });
            
            add(painel);
            setVisible(true);
        }

    public void executar1 () {
        criarMaca();
        executando1 = true;
        timer = new Timer(100, this);
        timer.start();
    }

    public void executar2 () {
        criarMaca();
        executando2 = true;
        timer = new Timer(125, this);
        timer.start();
    }

    private void criarMaca () {
        //Cria as maças em blocos aleatorios
        blocoX = random.nextInt(larguraTela / tamBloco) * tamBloco;
        blocoY = random.nextInt(alturaTela / tamBloco) * tamBloco;
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    public void desenharTela (Graphics g) {

        if (executando1) {
            //Maça
            g.setColor(Color.red);
            g.fillOval(blocoX, blocoY, tamBloco, tamBloco); 

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
        if (executando1) {
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
        if (eixoX[0] == blocoX && eixoY[0] == blocoY) {
            corpoCobra++;
            blocosComidos++;
            criarMaca();
        }
    }

    private void inserirLimites () {
        //Condição do corpo
        for (int i = corpoCobra; i > 0; i--) { 
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                executando1 = false;
                break;
            }
        }

        //Condição das bordas
        if (eixoX[0] < 0 || eixoX[0] > larguraTela) { //Esquerda ou Direita
            executando1 = false;
        }

        if (eixoY[0] < 0 || eixoY[0] > alturaTela) { //Cima ou Baixo
            executando1 = false;
        }

        if (!executando1) {
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
