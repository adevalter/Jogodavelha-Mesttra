
import java.util.Random;
import java.util.Scanner;

public class App1 {
    final static String CARACTERES_IDENTIFICADORES_ACEITOS = "XO0UC";
    char caracterUsuario;
    char caracterComputador;
    final static int TAMANHO_TABULEIRO = 3;

    public static void main(String[] args) throws Exception {

        Scanner teclado = new Scanner(System.in);

        char[][] tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        limparTela();
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
            exibirTabuleiro(tabuleiro);

            if (vezUsuarioJogar) {

                // TODO: Execute processar vez do usuario
                tabuleiro = processarVezUsuario(teclado, tabuleiro, caractereUsuario);

                // Verifica se o usuario venceu
                // TODO: Este if deve executar apenas se teve ganhador
                if (teveGanhador(tabuleiro, caractereUsuario)) {
                    // TODO: Exiba que o usuario ganhou
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
                exibirEmpate();
                // TODO: Exiba que ocorreu empate
                jogoContinua = false;
            }
        } while (jogoContinua);

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
                System.out.println("Digite a linha e a coluna separados por um espaço:");
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
        System.out.println(converter);
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
        System.out.println("É a sua vez de jogar!");
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

        int[] jogada = obterJogadaComputador(retornarPosicoesLivres(tabuleiro));
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
        for (int i = 0; i < tabuleiro.length; i++) {

            if (tabuleiro[i][0] == caractereJogador && tabuleiro[i][1] == caractereJogador
                    && tabuleiro[i][2] == caractereJogador) {
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

        System.out.println("O jogo terminou em empate!");
        System.out.println("  _____   _____ ");
        System.out.println(" |     | |     |");
        System.out.println(" |  0  | |  X  |");
        System.out.println(" |_____| |_____|");
        System.out.println("    0   X   0   ");
    }

    static void exibirVitoriaUsuario() {
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

}
