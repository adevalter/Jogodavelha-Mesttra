import java.nio.channels.Pipe.SourceChannel;
import java.util.Random;
import java.util.Scanner;

public class App {
    final static String CARACTERES_IDENTIFICADORES_ACEITOS = "XO0UC";
    char caracterUsuario;
    char caracterComputador;
    final static int TAMANHO_TABULEIRO = 3;
    static int vitoriasUsuario = 0;
    static int vitoriasComputador = 0;
    static int empateUsuarioComputador = 0;
    public static String dificuldade = "";
    static int quantidadeVezesComputadorJogou = 0;

    public static void main(String[] args) throws Exception {

        Scanner teclado = new Scanner(System.in);

        char resposta;

        do {
            char[][] tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
            limparTela();

            if (dificuldade.isEmpty()) {
                obterDificuldade(teclado);
            }

            inicializarTabuleiro(tabuleiro);

            // TODO: Faça a inicialização do tabuleiro aqui

            // Definimos aqui qual é o caractere que cada jogador irá utilizar no jogo.
            // TODO: chame as funções obterCaractereUsuario() e obterCaractereComputador
            // para definir quais caracteres da lista de caracteres aceitos que o jogador
            // quer configurar para ele e para o computador.

            char caractereUsuario = obterCaractereUsuario(teclado);
            char caractereComputador = obterCaractereComputador(teclado, caractereUsuario);

            // Esta variavel é utilizada para definir se o usuário começa a jogar ou não.
            // Valor true, usuario começa jogando, valor false computador começa.
            // TODO: obtenha o valor booleano sorteado
            boolean vezUsuarioJogar = sortearValorBooleano();

            boolean jogoContinua;

            do {
                // controla se o jogo terminou
                jogoContinua = true;

                // TODO: Exiba o tabuleiro aqui
                limparTela();

                ExibirPlacar(vitoriasComputador, vitoriasUsuario, empateUsuarioComputador);

                exibirTabuleiro(tabuleiro);

                if (vezUsuarioJogar) {

                    // TODO: Execute processar vez do usuario
                    tabuleiro = processarVezUsuario(teclado, tabuleiro, caractereUsuario);
                    limparTela();
                    exibirTabuleiro(tabuleiro);
                    // Verifica se o usuario venceu
                    // TODO: Este if deve executar apenas se teve ganhador
                    if (teveGanhador(tabuleiro, caractereUsuario)) {
                        // TODO: Exiba que o usuario ganhou
                        limparTela();
                        exibirTabuleiro(tabuleiro);
                        exibirVitoriaUsuario();
                        jogoContinua = false;
                    }

                    // TODO: defina qual o vaor a variavel abaixo deve possuir
                    vezUsuarioJogar = false;
                } else {

                    // TODO: Execute processar vez do computador
                    tabuleiro = processarVezComputador(tabuleiro, caractereComputador);

                    // Verifica se o computador venceu
                    // TODO: Este if deve executar apenas se teve ganhador
                    if (teveGanhador(tabuleiro, caractereComputador)) {
                        limparTela();
                        exibirTabuleiro(tabuleiro);
                        exibirVitoriaComputador();
                        // TODO: Exiba que o computador ganhou
                        jogoContinua = false;
                    }

                    // TODO: defina qual o vaor a variavel abaixo deve possuir
                    vezUsuarioJogar = true;
                }

                // TODO: Este if deve executar apenas se o jogo continua E
                // ocorreu tempate. Utilize o metodo teveEmpate()
                if (teveEmpate(tabuleiro)) {
                    limparTela();
                    exibirEmpate();
                    // TODO: Exiba que ocorreu empate
                    jogoContinua = false;

                }
            } while (jogoContinua);

            System.out.print("Deseja jogar novamente (s/n): ");
            resposta = teclado.nextLine().charAt(0);
            dificuldade = "";

        } while (resposta == 's');
        teclado.close();

    }

    static char[][] inicializarTabuleiro(char[][] tabuleiro) {

        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro.length; j++) {
                tabuleiro[i][j] = ' ';
            }
        }
        return tabuleiro;
    }

    static char obterCaractereUsuario(Scanner teclado) {

        while (true) {
            System.out.print("Digite um caractere que você deseja utilizar (X, O, 0, U, C): ");
            String resposta = teclado.nextLine().toUpperCase(); // Converte para maiúsculo para não ocorrer exceção.

            if (resposta.length() == 1) {
                char caractereDigitado = resposta.charAt(0);
                if (CARACTERES_IDENTIFICADORES_ACEITOS.contains(resposta)) {
                    return caractereDigitado;
                }
            }
            System.out.println("Caractere inválido! Tente novamente.");
        }
    }

    static char obterCaractereComputador(Scanner teclado, char caractereUsuario) {

        while (true) {
            System.out.print("Digite um caractere que o computador irá utilizar (X, O, 0, U, C): ");
            String resposta = teclado.nextLine().toUpperCase(); // Normaliza para maiúsculo.

            if (resposta.length() == 1) {
                char caractereDigitado = resposta.charAt(0);

                if (CARACTERES_IDENTIFICADORES_ACEITOS.contains(resposta) && caractereUsuario != caractereDigitado) {
                    return caractereDigitado;
                } else {
                    System.out.println("Digite um caractere diferente do usuário!");
                }
            }
            System.out.println("Caractere inválido! Tente novamente.");
        }
    }

    static boolean jogadaValida(String posicoesLivres, int linha, int coluna) {
        String jogada = linha + "" + coluna;
        if (posicoesLivres.contains(jogada)) {
            return true;
        } else {
            return false;
        }
    }

    static int[] obterJogadaUsuario(String posicoesLivres, Scanner teclado) {
        while (true) {
            try {
                System.out.print("Digite a linha e a coluna separados por um espaço: ");
                String input = teclado.nextLine();
                String[] inputs = input.split(" ");
                if (inputs.length != 2) {
                    throw new IllegalArgumentException("Por favor, digite exatamente dois valores.");
                }
                int linha = Integer.parseInt(inputs[0]);
                int coluna = Integer.parseInt(inputs[1]);
                linha -= 1;
                coluna -= 1;
                if (jogadaValida(posicoesLivres, linha, coluna)) {
                    return new int[] { linha, coluna };
                } else {
                    System.out.println("Jogada não permitida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite números válidos.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static int[] obterJogadaComputador(String posicoesLivres) {

        Random random = new Random();

        String linhaColuna[] = posicoesLivres.split(";");

        String[][] posicoes = new String[linhaColuna.length][2];

        for (int i = 0; i < linhaColuna.length; i++) {
            String[] valor = linhaColuna[i].split("");
            posicoes[i][0] = valor[0];
            posicoes[i][1] = valor[1];
        }

        int sorteio = random.nextInt(0, linhaColuna.length);
        String converter = posicoes[sorteio][0] + "," + posicoes[sorteio][1];
        // System.out.println(converter);
        return converterJogadaStringParaVetorInt(converter);
    }

    static int[] converterJogadaStringParaVetorInt(String jogada) {
        int[] vetor = new int[2];
        String valor[] = jogada.split(",");
        vetor[0] = Integer.parseInt(valor[0]);
        vetor[1] = Integer.parseInt(valor[1]);
        return vetor;
    }

    static char[][] processarVezUsuario(Scanner teclado, char[][] tabuleiro, char caractereUsuario) {
        System.out.println("\nÉ a sua vez de jogar!");
        int[] jogada = obterJogadaUsuario(retornarPosicoesLivres(tabuleiro), teclado);
        return retornarTabuleiroAtualizado(tabuleiro, jogada, caractereUsuario);
    }

    /*
     * Descrição: Utilizado para realizar as ações necessárias para processar a vez
     * do computador jogar. Este método é encarregado de obter a jogada do
     * computador através do método obterJogadaComputador, depois realizar a
     * atualização do tabuleiro através do método retornarTabuleiroAtualizado e
     * retornar o tabuleiro atualizado
     * Nível de complexidade: 4 de 10
     */
    static char[][] processarVezComputador(char[][] tabuleiro, char caractereComputador) {
        int[] jogada = { -1, -1 };

        if (dificuldade.equals("moderado")) {
            jogada = jogadaComputadorEmpate(tabuleiro, caractereComputador);

            if (jogada[0] >= 0) {

                return retornarTabuleiroAtualizado(tabuleiro, jogada, caractereComputador);
            }

        }

        if (dificuldade.equals("dificil")) {
            quantidadeVezesComputadorJogou++;

            if (quantidadeVezesComputadorJogou >= 2) {
                jogada = jogadaComputadorVitoria(tabuleiro, caractereComputador);
                if (jogada[0] < 0) {
                    jogada = jogadaComputadorEmpate(tabuleiro, caractereComputador);
                }
            }
            if (jogada[0] < 0) {
                jogada = jogadaInicialParaComputadorGanhar(tabuleiro, caractereComputador);
            }
            return retornarTabuleiroAtualizado(tabuleiro, jogada, caractereComputador);
        }

        jogada = obterJogadaComputador(retornarPosicoesLivres(tabuleiro));

        return retornarTabuleiroAtualizado(tabuleiro, jogada, caractereComputador);

    }

    static String retornarPosicoesLivres(char[][] tabuleiro) {
        String posicaoLivre = "";
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro.length; j++) {
                if (tabuleiro[i][j] == ' ') {
                    posicaoLivre += i + "" + j + ";";
                }
            }
        }
        posicaoLivre = posicaoLivre.replaceAll(";$", "");
        return posicaoLivre;
    }

    static boolean teveGanhador(char[][] tabuleiro, char caractereJogador) {
        if (teveGanhadorLinha(tabuleiro, caractereJogador)) {
            return true;
        }
        if (teveGanhadorColuna(tabuleiro, caractereJogador)) {
            return true;
        }
        if (teveGanhadorDiagonalPrincipal(tabuleiro, caractereJogador)) {
            return true;
        }
        if (teveGanhadorDiagonalSecundaria(tabuleiro, caractereJogador)) {
            return true;
        }
        return false;
    }

    static boolean teveGanhadorLinha(char[][] tabuleiro, char caractereJogador) {
        for (int linha = 0; linha < tabuleiro.length; linha++) {

            boolean venceu = true;
            for (int coluna = 0; coluna < tabuleiro.length; coluna++) {
                if (tabuleiro[linha][coluna] != caractereJogador) {
                    venceu = false;
                    break;
                }
            }
            if (venceu) {
                return true;
            }
        }
        return false;
    }

    static boolean teveGanhadorColuna(char[][] tabuleiro, char caractereJogador) {
        for (int coluna = 0; coluna < tabuleiro[0].length; coluna++) {
            boolean venceu = true;
            for (int linha = 0; linha < tabuleiro.length; linha++) {
                if (tabuleiro[linha][coluna] != caractereJogador) {
                    venceu = false;
                    break;
                }
            }
            if (venceu) {
                return true;
            }
        }
        return false;
    }

    static boolean teveGanhadorDiagonalPrincipal(char[][] tabuleiro, char caractereJogador) {
        for (int i = 0; i < tabuleiro.length; i++) {
            if (tabuleiro[i][i] != caractereJogador) {
                return false;
            }
        }
        return true;
    }

    static boolean teveGanhadorDiagonalSecundaria(char[][] tabuleiro, char caractereJogador) {
        int n = tabuleiro.length;
        for (int i = 0; i < n; i++) {
            if (tabuleiro[i][n - 1 - i] != caractereJogador) {
                return false;
            }
        }
        return true;
    }

    static void limparTela() {

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void exibirTabuleiro(char[][] tabuleiro) {
        System.out.println("COLUNA:       1" + "     2" + "     3\n");
        System.out.println(
                "Linha 1     " + "  " + tabuleiro[0][0] + "  |  " + tabuleiro[0][1] + "  |  " + tabuleiro[0][2]);
        System.out.println("            -----+-----+-----");
        System.out.println(
                "Linha 2     " + "  " + tabuleiro[1][0] + "  |  " + tabuleiro[1][1] + "  |  " + tabuleiro[1][2]);
        System.out.println("            -----+-----+-----");
        System.out.println(
                "Linha 3     " + "  " + tabuleiro[2][0] + "  |  " + tabuleiro[2][1] + "  |  " + tabuleiro[2][2]);

    }

    static char[][] retornarTabuleiroAtualizado(char[][] tabuleiro, int[] jogada,
            char caractereJogador) {
        tabuleiro[jogada[0]][jogada[1]] = caractereJogador;
        return tabuleiro;
    }

    static void exibirVitoriaComputador() {
        vitoriasComputador++;
        System.out.println("Computador em modo vencedor! Que venha a próxima batalha.");
        System.out.println("       .====================.");
        System.out.println("      ||                    ||");
        System.out.println("      ||    \\ O  O /       ||");
        System.out.println("      ||      \\___/        ||");
        System.out.println("      ||   COMPUTADOR FELIZ ||");
        System.out.println("      ||====================||");
        System.out.println("      ||     PROCESSANDO... ||");
        System.out.println("      ||____________________||");
        System.out.println("      '----------------------'");
    }

    static void exibirEmpate() {
        empateUsuarioComputador++;
        System.out.println("O jogo terminou em empate!");
        System.out.println("  _____   _____ ");
        System.out.println(" |     | |     |");
        System.out.println(" |  0  | |  X  |");
        System.out.println(" |_____| |_____|");
        System.out.println("    0   X   0   ");
    }

    static void exibirVitoriaUsuario() {
        vitoriasUsuario++;
        System.out.println("Vitória do usuário! Comemore!");
        System.out.println("       _______  ");
        System.out.println("      /       \\ ");
        System.out.println("     |  O   O  |");
        System.out.println("     |    ^    |");
        System.out.println("     |   \\_/   |");
        System.out.println("      \\_______/ ");
        System.out.println("        | | |   ");
        System.out.println("       /  |  \\  ");
        System.out.println("      /   |   \\ ");
        System.out.println("     /    |    \\ ");
        System.out.println("    /     |     \\ ");
        System.out.println("   /______|______\\ ");
        System.out.println("   \"Eu sou o campeão!\"");
    }

    static boolean teveEmpate(char[][] tabuleiro) {
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                if (tabuleiro[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean sortearValorBooleano() {
        Random random = new Random();
        return random.nextBoolean();
    }

    static void ExibirPlacar(int vitoriasComputador, int vitoriasUsuario, int empate) {

        System.out.println("╔═════════════════════════════════╗");
        System.out.println("║            PLACAR               ║");
        System.out.println("╠════════════════╦════════════════╣");
        System.out.printf("║ Usuário        ║ %-14d ║%n", vitoriasUsuario);
        System.out.printf("║ Computador     ║ %-14d ║%n", vitoriasComputador);
        System.out.printf("║ Empate         ║ %-14d ║%n", empate);
        System.out.println("╚════════════════╩════════════════╝");

    }

    static int[] jogadaComputadorEmpate(char[][] tabuleiro, char caracterJogador) {
        int[] jogada = { -1, -1 };
        String JogadaLinhaEmpate = jogadaTentaEmpateLinhaComputador(tabuleiro, caracterJogador);
        String jogadaColunaComputador = jogadaTentaEmpateColunaComputador(tabuleiro, caracterJogador);
        String jogadaDiagonalComputador = jogadaTentaEmpateDiagonalComputador(tabuleiro, caracterJogador);

        if (!JogadaLinhaEmpate.isEmpty()) {
            return converterJogadaStringParaVetorInt(JogadaLinhaEmpate);
        }

        if (!jogadaColunaComputador.isEmpty()) {
            return converterJogadaStringParaVetorInt(jogadaColunaComputador);
        }

        if (!jogadaDiagonalComputador.isEmpty()) {
            return converterJogadaStringParaVetorInt(jogadaDiagonalComputador);
        }

        return jogada;
    }

    static String jogadaTentaEmpateLinhaComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeJogadaParaValidar = tabuleiro.length - 1;
        int quantidadeMinimaParaValidarVazio = tabuleiro.length - quantidadeJogadaParaValidar;
        String jogadaComputador = "";
        for (int i = 0; i < tabuleiro.length; i++) {
            int contaJogadaValida = 0;
            int qtdeVazio = 0;
            for (int j = 0; j < tabuleiro.length; j++) {
                if (caractereComputador != tabuleiro[i][j]) {
                    if (tabuleiro[i][j] == ' ') {
                        jogadaComputador = i + "," + j;
                        qtdeVazio++;
                        if (qtdeVazio >= 2) {
                            break;
                        }
                    }
                    if (caractereComputador != tabuleiro[i][j] && tabuleiro[i][j] != ' ') {
                        contaJogadaValida++;
                    }

                    if (quantidadeJogadaParaValidar == contaJogadaValida
                            && qtdeVazio == quantidadeMinimaParaValidarVazio) {
                        return jogadaComputador;

                    }
                    // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
                    // posicaoOcupada += i + "" + j + ";";
                }

            }
        }
        return "";
    }

    static String jogadaTentaEmpateColunaComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeJogadaParaValidar = tabuleiro[0].length - 1;
        int quantidadeMinimaParaValidarVazio = tabuleiro[0].length - quantidadeJogadaParaValidar;
        String jogadaComputador = "";
        for (int coluna = 0; coluna < tabuleiro[0].length; coluna++) {
            int contaJogadaValida = 0;
            int qtdeVazio = 0;
            for (int linha = 0; linha < tabuleiro.length; linha++) {
                if (caractereComputador != tabuleiro[linha][coluna]) {
                    if (tabuleiro[linha][coluna] == ' ') {
                        jogadaComputador = linha + "," + coluna;
                        qtdeVazio++;
                        if (qtdeVazio >= 2) {
                            break;
                        }
                    }
                    if (caractereComputador != tabuleiro[linha][coluna] && tabuleiro[linha][coluna] != ' ') {
                        contaJogadaValida++;
                    }

                    if (quantidadeJogadaParaValidar == contaJogadaValida
                            && qtdeVazio == quantidadeMinimaParaValidarVazio) {
                        return jogadaComputador;

                    }
                    // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
                    // posicaoOcupada += i + "" + j + ";";
                }

            }
        }
        return "";
    }

    static String jogadaTentaEmpateDiagonalComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeLoop = tabuleiro.length;
        int quantidadeJogadaParaValidar = tabuleiro.length - 1;
        int quantidadeMinimaParaValidarVazio = tabuleiro.length - quantidadeJogadaParaValidar;
        String jogadaComputador = "";
        int contaJogadaValida = 0;
        int qtdeVazio = 0;

        for (int linha = 0; linha < quantidadeLoop; linha++) {
            if (caractereComputador != tabuleiro[linha][linha]) {
                if (tabuleiro[linha][linha] == ' ') {
                    jogadaComputador = linha + "," + linha;
                    qtdeVazio++;
                    if (qtdeVazio >= 2) {
                        break;
                    }
                }
                if (caractereComputador != tabuleiro[linha][linha] && tabuleiro[linha][linha] != ' ') {
                    contaJogadaValida++;
                }

                if (quantidadeJogadaParaValidar == contaJogadaValida
                        && qtdeVazio == quantidadeMinimaParaValidarVazio) {
                    return jogadaComputador;

                }
                // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
                // posicaoOcupada += i + "" + j + ";";
            }

        }

        jogadaComputador = "";
        contaJogadaValida = 0;
        qtdeVazio = 0;

        for (int linha = 0; linha < quantidadeLoop; linha++) {
            if (caractereComputador != tabuleiro[linha][quantidadeLoop - 1 - linha]) {
                if (tabuleiro[linha][quantidadeLoop - 1 - linha] == ' ') {
                    jogadaComputador = linha + "," + (quantidadeLoop - 1 - linha);
                    qtdeVazio++;
                    if (qtdeVazio >= 2) {
                        break;
                    }
                }
                if (caractereComputador != tabuleiro[linha][quantidadeLoop - 1 - linha]
                        && tabuleiro[linha][quantidadeLoop - 1 - linha] != ' ') {
                    contaJogadaValida++;
                }

                if (quantidadeJogadaParaValidar == contaJogadaValida
                        && qtdeVazio == quantidadeMinimaParaValidarVazio) {
                    return jogadaComputador;

                }
                // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
                // posicaoOcupada += i + "" + j + ";";
            }

        }
        return "";
    }

    /*********************************************
     * computador tentar ganhar
     ********************************************/

    static int[] jogadaInicialParaComputadorGanhar(char[][] tabuleiro, char caractereComputador) {

        int[] jogada = { -1, -1 };

        if (tabuleiro[1][1] == ' ') {

            String posicao = "1,1";
            return converterJogadaStringParaVetorInt(posicao);

        }

        if (tabuleiro[0][0] == ' ' || tabuleiro[0][2] == ' ' || tabuleiro[2][0] == ' ' || tabuleiro[2][2] == ' ') {
            boolean continuar = true;
            do {
                String proximaJogada = proximaJogaComputador(tabuleiro);
                if (!proximaJogada.isEmpty()) {

                    String[] vetor = proximaJogada.split(",");
                    String posicao = vetor[0] + "," + vetor[1];
                    return converterJogadaStringParaVetorInt(posicao);

                }

            } while (continuar);

        }

        return jogada;

    }

    static String proximaJogaComputador(char[][] tabuleiro) {
        Random rnd = new Random();
        for (int i = 0; i < tabuleiro.length; i++) {
            int vetor1 = rnd.nextInt(2) * 2;
            int vetor2 = rnd.nextInt(2) * 2;
            if (tabuleiro[vetor1][vetor2] == ' ') {
                // tabuleiro[vetor1][vetor2] = 'X';

                return vetor1 + "," + vetor2;
            }
        }
        return "";
    }

    static int[] jogadaComputadorVitoria(char[][] tabuleiro, char caracterJogador) {
        int[] jogada = { -1, -1 };
        String JogadaLinhaVitoria = jogadaTentaVitoriaLinhaComputador(tabuleiro, caracterJogador);
        String jogadaColunaComputador = jogadaTentaVitoriaColunaComputador(tabuleiro, caracterJogador);
        String jogadaDiagonalComputador = jogadaTentaVitoriaDiagonalComputador(tabuleiro, caracterJogador);

        if (!JogadaLinhaVitoria.isEmpty()) {
            return converterJogadaStringParaVetorInt(JogadaLinhaVitoria);
        }

        if (!jogadaColunaComputador.isEmpty()) {
            return converterJogadaStringParaVetorInt(jogadaColunaComputador);
        }

        if (!jogadaDiagonalComputador.isEmpty()) {
            return converterJogadaStringParaVetorInt(jogadaDiagonalComputador);
        }

        return jogada;
    }

    static String jogadaTentaVitoriaLinhaComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeJogadaParaValidar = tabuleiro.length - 1;
        int quantidadeMinimaParaValidarVazio = tabuleiro.length - quantidadeJogadaParaValidar;
        String jogadaComputador = "";
        for (int i = 0; i < tabuleiro.length; i++) {
            int contaJogadaValida = 0;
            int qtdeVazio = 0;
            for (int j = 0; j < tabuleiro.length; j++) {

                if (tabuleiro[i][j] == ' ') {
                    jogadaComputador = i + "," + j;
                    qtdeVazio++;
                    if (qtdeVazio > 1) {
                        break;
                    }
                }
                if (caractereComputador == tabuleiro[i][j] && tabuleiro[i][j] == ' ') {
                    contaJogadaValida++;
                }

                if (quantidadeJogadaParaValidar == contaJogadaValida
                        && qtdeVazio == quantidadeMinimaParaValidarVazio) {
                    return jogadaComputador;

                }
                // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);

            }
        }
        return "";
    }

    static String jogadaTentaVitoriaColunaComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeJogadaParaValidar = tabuleiro[0].length - 1;
        int quantidadeMinimaParaValidarVazio = tabuleiro[0].length - quantidadeJogadaParaValidar;
        String jogadaComputador = "";
        for (int coluna = 0; coluna < tabuleiro[0].length; coluna++) {
            int contaJogadaValida = 0;
            int qtdeVazio = 0;
            for (int linha = 0; linha < tabuleiro.length; linha++) {

                if (tabuleiro[linha][coluna] == ' ') {
                    jogadaComputador = linha + "," + coluna;
                    qtdeVazio++;
                    if (qtdeVazio > 1) {
                        break;
                    }
                }
                if (caractereComputador == tabuleiro[linha][coluna] && tabuleiro[linha][coluna] != ' ') {
                    contaJogadaValida++;
                }

                if (quantidadeJogadaParaValidar == contaJogadaValida
                        && qtdeVazio == quantidadeMinimaParaValidarVazio) {
                    return jogadaComputador;

                }
                // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
                // posicaoOcupada += i + "" + j + ";";

            }
        }
        return "";
    }

    static String jogadaTentaVitoriaDiagonalComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeLoop = tabuleiro.length;
        int quantidadeJogadaParaValidar = tabuleiro.length - 1;

        String jogadaComputador = "";
        int contaJogadaValida = 0;
        int qtdeVazio = 0;

        for (int linha = 0; linha < quantidadeLoop; linha++) {

            if (tabuleiro[linha][linha] == ' ') {
                jogadaComputador = linha + "," + linha;
                qtdeVazio++;
                if (qtdeVazio > 1) {
                    break;
                }
            }
            if (caractereComputador == tabuleiro[linha][linha] && tabuleiro[linha][linha] != ' ') {
                contaJogadaValida++;
            }

            if (quantidadeJogadaParaValidar == contaJogadaValida && qtdeVazio == 1) {
                return jogadaComputador;

            }
            // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
            // posicaoOcupada += i + "" + j + ";";

        }

        jogadaComputador = "";
        contaJogadaValida = 0;
        qtdeVazio = 0;

        for (int linha = 0; linha < quantidadeLoop; linha++) {

            if (tabuleiro[linha][quantidadeLoop - 1 - linha] == ' ') {
                jogadaComputador = linha + "," + (quantidadeLoop - 1 - linha);
                qtdeVazio++;
                if (qtdeVazio > 1) {
                    break;
                }
            }
            if (caractereComputador == tabuleiro[linha][quantidadeLoop - 1 - linha]
                    && tabuleiro[linha][quantidadeLoop - 1 - linha] != ' ') {
                contaJogadaValida++;
            }

            if (quantidadeJogadaParaValidar == contaJogadaValida && qtdeVazio == 1) {
                return jogadaComputador;

            }
            // System.out.println(i + " col " + j + " " + tabuleiro[i][j]);
            // posicaoOcupada += i + "" + j + ";";

        }
        return "";
    }

    static void obterDificuldade(Scanner teclado) {
        boolean continuaLoop = true;
        do {
            System.out.print("Qual dificudade Dejesa 1- Fácil - 2 Moderado - 3 Díficil ");
            int resposta = teclado.nextInt(); // Converte para maiúsculo para não ocorrer exceção.
            try {
                switch (resposta) {
                    case 1:
                        dificuldade = "facil";
                        teclado.nextLine();
                        continuaLoop = false;
                        break;
                    case 2:
                        dificuldade = "moderado";
                        teclado.nextLine();
                        continuaLoop = false;
                        break;
                    case 3:
                        teclado.nextLine();
                        dificuldade = "dificil";
                        continuaLoop = false;
                        break;
                    default:
                        System.out.println("opção Inválida");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Digite apenas números");
            }
        } while (continuaLoop);

    }

}
