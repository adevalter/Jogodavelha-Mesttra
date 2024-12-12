import java.util.Random;

public class teste {
    public static void main(String[] args) {
        char[][] tabuleiro = new char[3][3];
        inicializarTabuleiro(tabuleiro);
        tabuleiro[1][1] = 'X';
        tabuleiro[2][1] = 'C';
        tabuleiro[0][0] = 'X';
        tabuleiro[2][2] = 'C';

        exibirTabuleiro(tabuleiro);

        // retornarPosicoesLivres(tabuleiro);

        jogadaParaComputadorGanhar(tabuleiro);

    }

    static void jogadaParaComputadorGanhar(char[][] tabuleiro) {

        if (tabuleiro[1][1] == ' ') {
            tabuleiro[1][1] = 'X';
            exibirTabuleiro(tabuleiro);
            return;
        }

        if (tabuleiro[0][0] == ' ' || tabuleiro[0][2] == ' ' || tabuleiro[2][0] == ' ' || tabuleiro[2][2] == ' ') {
            boolean continuar = true;
            do {
                String jogada = proximaJogaComputador(tabuleiro);
                if (!jogada.isEmpty()) {

                    String[] vetor = jogada.split("");
                    int[] jogadas = converterJogadaStringParaVetorInt(jogada);
                    tabuleiro[jogadas[0]][jogadas[1]] = 'X';
                    exibirTabuleiro(tabuleiro);
                    continuar = false;
                }

            } while (continuar);

        }

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

    static int[] converterJogadaStringParaVetorInt(String jogada) {
        int[] vetor = new int[2];
        String valor[] = jogada.split(",");
        vetor[0] = Integer.parseInt(valor[0]);
        vetor[1] = Integer.parseInt(valor[1]);
        return vetor;
    }

    static char[][] inicializarTabuleiro(char[][] tabuleiro) {

        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro.length; j++) {
                tabuleiro[i][j] = ' ';
            }
        }
        return tabuleiro;
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

    static String jogadaEmpate(char[][] tabuleiro, char caracterJogador) {
        String jogada = null;

        jogada = jogadaTentaEmpateLinhaComputador(tabuleiro, caracterJogador);

        if (jogada.equals(null)) {
            jogadaTentaEmpateColunaComputador(tabuleiro, caracterJogador);
        }

        if (jogada.equals(null)) {
            jogada = jogadaTentaEmpateDiagonalComputador(tabuleiro, caracterJogador);
        }
        if (jogada.equals(null)) {

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
        return null;
    }

    static String jogadaTentaEmpateColunaComputador(char[][] tabuleiro, char caractereComputador) {
        int quantidadeJogadaParaValidar = tabuleiro.length - 1;
        int quantidadeMinimaParaValidarVazio = tabuleiro.length - quantidadeJogadaParaValidar;
        String jogadaComputador = "";
        for (int coluna = 0; coluna < tabuleiro[0].length; coluna++) {
            int contaJogadaValida = 0;
            int qtdeVazio = 0;
            for (int linha = 0; linha < tabuleiro.length; linha++) {
                if (caractereComputador != tabuleiro[linha][coluna]) {
                    if (tabuleiro[linha][coluna] == ' ') {
                        jogadaComputador = coluna + "," + linha;
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
        return null;
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
        return null;
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
}
