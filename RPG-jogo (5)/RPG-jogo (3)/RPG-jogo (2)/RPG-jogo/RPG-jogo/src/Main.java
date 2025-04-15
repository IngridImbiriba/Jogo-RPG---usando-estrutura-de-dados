import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Jogador jogador = null;
    boolean executando = true; 
    Personagem personagem = new Personagem(null, jogador); 
    TextoColorido.exibir();
    System.out.println(" Pressione ENTER para continuar... ");
    scanner.nextLine();
    limparTela();

    while (executando) {
        System.out.println("\n===== MENU PRINCIPAL =======");
        System.out.println("1 - === CADASTRAR JOGADOR ====");
        System.out.println("2 - === LOGIN ================");
        System.out.println("3 - === CRIAR PERSONAGEM =====");
        System.out.println("4 - === ACESSAR LOJA =========");
        System.out.println("5 - === INICIAR BATALHA ======");
        System.out.println("6 - === MOSTRAR INVENTÁRIO ===");
        System.out.println("7 - === SAIR =================");
        System.out.println("===== SUA ESCOLHA:");

        int opcao = -1;
        try {
            opcao = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Digite apenas números.");
            scanner.nextLine(); 
            continue;
        }

        limparTela(); 

        switch (opcao) {
            case 1:
                jogador = Jogador.cadastrar(scanner);
                System.out.println("Jogador cadastrado com sucesso!");
                break;
            case 2:
                System.out.print("=== DIGITE SEU NOME: ");
                String nome = scanner.nextLine();
                while (!nome.matches("[a-zA-Z ]+")) {
                    System.out.print("Nome inválido. Use apenas letras: ");
                    nome = scanner.nextLine();
                    
                }
                System.out.print("=== DIGITE SUA SENHA: ");
                String senha = scanner.nextLine();
                if (jogador != null && jogador.autenticar(nome, senha)) {
                    System.out.println("==== LOGIN FEITO :) ====");
                } else {
                    System.out.println("=== JOGADOR NÃO CADASTRADO ===");
                    jogador = null;
                }
                break;
            case 3:
                if (verificaLogin(jogador)) {
                    jogador.criarPersonagem(scanner, jogador);
                }
                break;
            case 4:
                if (verificaLogin(jogador)) {
                    Personagem personagemLoja = jogador.selecionarPersonagem(scanner);
                    if (personagemLoja != null) {
                        personagemLoja.loja();
                    } else {
                        System.out.println("Nenhum personagem selecionado.");
                    } 
                }
                break;
            case 5:
                if (verificaLogin(jogador)) {
                    System.out.println("\n=== ESCOLHA O MODO DE BATALHA ===");
                    System.out.println("1. PvP - Jogador vs Jogador");
                    System.out.println("2. PvE - Jogador vs Monstro");
                    System.out.println("===== SUA ESCOLHA:");
                    int escolhaBatalha = scanner.nextInt();
                    scanner.nextLine();

                    switch (escolhaBatalha) {
                        case 1:
                            iniciarBatalhaPvP(jogador, jogador, scanner);
                            break;
                        case 2:
                            iniciarBatalhaPvE(jogador, scanner);
                            break;
                        default:
                            System.out.println("Opção inválida para o modo de batalha.");
                            break;
                    }
                }
                break;
            case 6:
            if(verificaLogin(jogador)){
                Personagem personagemInventario = jogador.selecionarPersonagem(scanner);
                if(personagemInventario != null){
                    System.out.println("=== SEU INVENTÁRIO ===");
                    personagem.mostrarInventario();
                    personagem.mostrarHabilidades();
                }else{
                    System.out.println("Nenhum personagem selecionado");
                }
            }
            break;

            case 7:
            System.out.println("Saindo do jogo...");
            executando = false;
            break;

            default:
                System.out.println("Opção inválida.");
        }
    }
    scanner.close();
}
private static boolean verificaLogin(Jogador jogador) {
    if (jogador == null) {
        System.out.println("Você precisa estar logado para acessar essa opção.");
        return false;
    }
    return true;
}

public static void limparTela() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
}

public static void iniciarBatalhaPvP(Jogador jogador1, Jogador jogador2, Scanner scanner) {
    System.out.println("\n=== MODO PvP ===");

    // Selecionar obrigatoriamente pelo menos 1 personagem de cada jogador
    Personagem personagem1 = jogador1.selecionarPersonagem(scanner);
    if (personagem1 == null) {
        System.out.println("Jogador 1 não selecionou personagem. Batalha cancelada.");
        return;
    }

    Personagem personagem2 = jogador2.selecionarPersonagem(scanner);
    if (personagem2 == null) {
        System.out.println("Jogador 2 não selecionou personagem. Batalha cancelada.");
        return;
    }

    // Criar a batalha e adicionar os dois personagens obrigatórios
    Batalha batalha = new Batalha();
    batalha.adicionarParticipante(personagem1);
    batalha.adicionarParticipante(personagem2);

    // Perguntar se querem adicionar mais personagens
    System.out.println("Adicionar outro personagem? (s/n)");
    String resposta = scanner.nextLine();

    while (resposta.equalsIgnoreCase("s")) {
        Personagem outro1 = jogador1.selecionarPersonagem(scanner);
        Personagem outro2 = jogador2.selecionarPersonagem(scanner);

        if (outro1 != null && !outro1.equals(personagem1) && !outro1.equals(personagem2)) {
            batalha.adicionarParticipante(outro1);
        }

        if (outro2 != null && !outro2.equals(personagem2) && !outro2.equals(personagem1)) {
            batalha.adicionarParticipante(outro2);
        }

        System.out.println("Adicionar outro personagem? (s/n)");
        resposta = scanner.nextLine();
    }

    // Iniciar a batalha
    batalha.iniciarBatalha();
}


public static void iniciarBatalhaPvE(Jogador jogador, Scanner scanner) {
    System.out.println("\n=== MODO PvE ===");
    Personagem personagem = jogador.selecionarPersonagem(scanner);
    if (personagem == null) {
        System.out.println("-----Nenhum personagem selecionado para batalhar-----");
        return;
    }

    Monstro monstro = new Monstro();
    System.out.println("=== VOCE VAI ENFRENTAR O " + monstro.getNome() + " (HP: " + monstro.getVidaAtual() + ")");
    
    BatalhaPvE batalhaPvE = new BatalhaPvE(personagem, monstro);
    batalhaPvE.iniciarBatalha();
}
}
