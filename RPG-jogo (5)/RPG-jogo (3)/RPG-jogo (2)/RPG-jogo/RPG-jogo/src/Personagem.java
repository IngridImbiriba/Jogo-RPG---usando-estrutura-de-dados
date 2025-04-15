import java.util.Scanner;

public class Personagem {
    private static int geradorId = 1;
    private int idPersonagem;
    private String nome;
    private int nivel;
    private int vidaMaxima, vidaAtual;
    private Jogador dono;

    // Habilidades e Itens como listas encadeadas
    private HabilidadeNode habilidades;
    private ItemNode inventario;

    // Construtor
    public Personagem(String nome, Jogador dono) {
        this.idPersonagem = geradorId++;
        this.nome = nome;
        this.dono = dono;
        this.nivel = 1;
        this.vidaMaxima = 100;
        this.vidaAtual = vidaMaxima;
        this.habilidades = null;
        this.inventario = null;
    }

    // Recebe dano e reduz a vida
    public void receberDano(int valor) {
        this.vidaAtual -= valor;
        if (vidaAtual < 0) vidaAtual = 0;
    }
    public void defender() {
        System.out.println(this.getNome() + " se prepara para resistir aos danos!");
    }

    // Cura o personagem, não ultrapassando a vida máxima
    public void curar(int valor) {
        this.vidaAtual += valor;
        if (vidaAtual > vidaMaxima) vidaAtual = vidaMaxima;
    }

    // Verifica se o personagem está vivo
    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    // Sobe de nível
    public void subirNivel() {
        this.nivel++;
        this.vidaMaxima += 10;
        this.vidaAtual = vidaMaxima;
    }

    // Adiciona uma habilidade à lista de habilidades
    public void adicionarHabilidade(String nome, int dano) {
        habilidades = inserirHabilidade(habilidades, nome, dano);
    }

    // Adiciona um item ao inventário
    public void adicionarItem(String nome, int quantidade) {
        inventario = inserirItem(inventario, nome, quantidade);
    }

    // Insere uma habilidade na lista de habilidades (lista encadeada)
    private HabilidadeNode inserirHabilidade(HabilidadeNode cabeca, String nome, int dano) {
        HabilidadeNode novo = new HabilidadeNode(nome, dano);
        if (cabeca == null) {
            return novo;
        } else {
            HabilidadeNode temp = cabeca;
            while (temp.proximo != null) {
                temp = temp.proximo;
            }
            temp.proximo = novo;
            return cabeca;
        }
    }

    // Insere um item na lista de itens (lista encadeada)
    private ItemNode inserirItem(ItemNode cabeca, String nome, int quantidade) {
        ItemNode novo = new ItemNode(nome, quantidade);
        if (cabeca == null) {
            return novo;
        } else {
            ItemNode temp = cabeca;
            while (temp.proximo != null) {
                temp = temp.proximo;
            }
            temp.proximo = novo;
            return cabeca;
        }
    }

    // Exibe todas as habilidades do personagem
    public void mostrarHabilidades() {
        HabilidadeNode temp = habilidades;
        while (temp != null) {
            System.out.println("Habilidade: " + temp.nome + "| Dano: " + temp.dano );
            temp = temp.proximo;
        }
    }
    // Exibe todos os itens no inventário
    public void mostrarInventario() {
        ItemNode temp = inventario;
        while (temp != null) {
            System.out.println("Item: " + temp.nome + ", Quantidade: " + temp.quantidade);
            temp = temp.proximo;
        }
    }
    // receber recompensas 
    public void receberRecompensas(int moedas, int cura, int mana){
        dono.adicionarMoedas(moedas);
        System.out.println(nome + " recebeu " + moedas + " moedas como recompensa!");

        if (cura > 0) {
            curar(cura);
            System.out.println(nome + " recuperou " + cura + " pontos de vida.");
        }
    }
    public void atacar(Monstro monstro) {
        int dano = 10 + (nivel * 2); // Dano baseado no nível
        System.out.println(nome + " ataca " + monstro.getNome() + " causando " + dano + " de dano!");
        monstro.receberDano(dano);
    }
    
    public void usarHabilidade(Monstro monstro) {
        if (habilidades == null) {
            System.out.println(nome + " não possui nenhuma habilidade para usar!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha uma habilidade para usar:");
    
        int index = 1;
        HabilidadeNode temp = habilidades;
        while (temp != null) {
            System.out.println(index + ". " + temp.nome + " (Dano: " + temp.dano + ")");
            temp = temp.proximo;
            index++;
        }
        int escolha = scanner.nextInt();
        temp = habilidades;
        for (int i = 1; i < escolha && temp != null; i++) {
            temp = temp.proximo;
        }
    
        if (temp == null) {
            System.out.println("Habilidade inválida.");
            return;
        }
    }
    
    public void loja() {
        System.out.println("\r\n" + //
                        "  _________           __         ___.                                  .__            .___                .__             __      ._.\r\n" + //
                        " /   _____/ ____     |__|____    \\_ |__   ____   _____           ___  _|__| ____    __| _/____   _____    |  |   ____    |__|____ | |\r\n" + //
                        " \\_____  \\_/ __ \\    |  \\__  \\    | __ \\_/ __ \\ /     \\   ______ \\  \\/ /  |/    \\  / __ |/  _ \\  \\__  \\   |  |  /  _ \\   |  \\__  \\| |\r\n" + //
                        " /        \\  ___/    |  |/ __ \\_  | \\_\\ \\  ___/|  Y Y  \\ /_____/  \\   /|  |   |  \\/ /_/ (  <_> )  / __ \\_ |  |_(  <_> )  |  |/ __ \\\\|\r\n" + //
                        "/_______  /\\___  >\\__|  (____  /  |___  /\\___  >__|_|  /           \\_/ |__|___|  /\\____ |\\____/  (____  / |____/\\____/\\__|  (____  /_\r\n" + //
                        "        \\/     \\/\\______|    \\/       \\/     \\/      \\/                        \\/      \\/             \\/             \\______|    \\/\\/\r\n" + //
                        " ");
        System.out.println("Seu saldo de moedas: " + dono.getSaldoMoedas() + " moedas.");
        System.out.println("Escolha o que deseja comprar:");

        System.out.println("1. Habilidade de Ataque (Custo: 50 moedas)");
        System.out.println("2. Habilidade de Defesa (Custo: 40 moedas)");
        System.out.println("3. Poção de Vida (Custo: 30 moedas)");
        System.out.println("4. Espada (Custo: 60 moedas)");

        Scanner scanner = new Scanner(System.in);
        int opcao = scanner.nextInt();

        if (opcao == 1 && dono.getSaldoMoedas() >= 50) {
            adicionarHabilidade("Ataque", 30);
            dono.removerMoedas(50);
            System.out.println("Você comprou a habilidade Ataque!");
        } else if (opcao == 2 && dono.getSaldoMoedas() >= 40) {
            adicionarHabilidade("Defesa", 10);
            dono.removerMoedas(40);
            System.out.println("Você comprou a habilidade Defesa!");
        } else if (opcao == 3 && dono.getSaldoMoedas() >= 30) {
            adicionarItem("Poção de Vida", 1);
            dono.removerMoedas(30);
            System.out.println("Você comprou a Poção de Vida!");
        } else if (opcao == 4 && dono.getSaldoMoedas() >= 60) {
            adicionarItem("Espada", 1);
            dono.removerMoedas(60);
            System.out.println("Você comprou a Espada!");
        } else {
            System.out.println("Saldo insuficiente ou opção inválida.");
        }
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public int getIdPersonagem() {
        return idPersonagem;
    }

    public Jogador getDono() {
        return dono;
    }
    
    public int getVidaMaxima() {
        return vidaMaxima;
    }
    
    public boolean temHabilidade() {
        return habilidades!= null;
    }
    public boolean temItem() {
        return inventario!= null;
    }
}