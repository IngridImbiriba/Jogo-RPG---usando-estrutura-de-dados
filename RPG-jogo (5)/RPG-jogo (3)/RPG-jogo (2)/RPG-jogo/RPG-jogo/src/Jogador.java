import java.util.Scanner;

public class Jogador {
    private static int geradorId = 1;

    private int idJogador;
    private String nome;
    private String senha;
    private int saldoMoedas;
    private PersonagemNode personagens; 

    private Jogador(String nome, String senha) {
        this.idJogador = geradorId++;
        this.nome = nome;
        this.senha = senha;
        this.saldoMoedas = 100;  // saldo inicial
        this.personagens = null;
    }
    public static Jogador cadastrar(Scanner scanner) {
        System.out.print("|=|=|=| DIGITE O NOME DO JOGADOR: ");
        String nome = scanner.nextLine();
        while (!nome.matches("[a-zA-Z ]+")) {
            System.out.print("=== NOME INVALIDO ===  Use apenas letras: ");
            nome = scanner.nextLine();
        }

        System.out.print("|=|=|=| CRIE UMA SENHA: ");
        String senha = scanner.nextLine();

        return new Jogador(nome, senha);
    }

    public boolean autenticar(String nome, String senha) {
        return this.nome.equals(nome) && this.senha.equals(senha);
    }

    // Método para criar um novo personagem e adicionar à lista encadeada
    public void criarPersonagem(Scanner scanner, Jogador dono) {
        System.out.print("Digite o nome do personagem: ");
        String nomePersonagem = scanner.nextLine();
        while (!nome.matches("[a-zA-Z ]+")) {
            System.out.print("Nome inválido. Use apenas letras: ");
            nome = scanner.nextLine();
        }

        Personagem novoPersonagem = new Personagem(nomePersonagem, this);
        personagens = inserirPersonagem(personagens, novoPersonagem);

        System.out.println("======= PERSONAGEM  " + nomePersonagem + " CRIADO COM SUCESSO! =======");
    }

    // Método para inserir personagem na lista encadeada
    private PersonagemNode inserirPersonagem(PersonagemNode cabeca, Personagem personagem) {
        PersonagemNode novo = new PersonagemNode(personagem);
        if (cabeca == null) {
            return novo;
        } else {
            PersonagemNode temp = cabeca;
            while (temp.proximo != null) {
                temp = temp.proximo;
            }
            temp.proximo = novo;
            return cabeca;
        }
    }

    // Permite o jogador selecionar um personagem pela posição
    public Personagem selecionarPersonagem(Scanner scanner) {
        if (personagens == null) {
            System.out.println("------Nenhum personagem cadastrado------");
            return null;
        }

        System.out.println("\n======Lista de Personagens======");
        PersonagemNode temp = personagens;
        int contador = 1;
        while (temp != null) {
            System.out.println(contador + " - " + temp.personagem.getNome() + " (Nível: " + temp.personagem.getNivel() + ")");
            temp = temp.proximo;
            contador++;
        }

        System.out.print("Escolha o número do personagem: ");
        int escolha = scanner.nextInt();
        scanner.nextLine(); 

        temp = personagens;
        int atual = 1;
        while (temp != null) {
            if (atual == escolha) {
                return temp.personagem;
            }
            temp = temp.proximo;
            atual++;
        }

        System.out.println("Escolha inválida.");
        return null;
    }

    // Adiciona moedas ao saldo
    public void adicionarMoedas(int valor) {
        saldoMoedas += valor;
    }

    // Remove moedas, se houver saldo suficiente
    public void removerMoedas(int valor) {
        if (saldoMoedas >= valor) {
            saldoMoedas -= valor;
        }
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public int getSaldoMoedas() {
        return saldoMoedas;
    }

    public int getIdJogador() {
        return idJogador;
    }
}