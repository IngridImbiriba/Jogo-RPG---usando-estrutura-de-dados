import java.util.Random;

public class Monstro {
    private String nome;
    private int vidaMaxima;
    private int vidaAtual;
    private int ataqueBase;

    private Random random = new Random();

    private static final String[] NOMES = {
        "Goblin", "Esqueleto", "Orc", "Lobo Selvagem", "Slime", "Dragão Bebê", "Morcego Gigante"
    };

    public Monstro() {
        this.nome = NOMES[random.nextInt(NOMES.length)];
        this.vidaMaxima = 50 + random.nextInt(51);
        this.vidaAtual = vidaMaxima;
        this.ataqueBase = 5 + random.nextInt(11); 
    }

    public String getNome() {
        return nome;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    public void atacar(Personagem alvo) {
        int dano = ataqueBase + random.nextInt(6); // Base + 0~5
        alvo.receberDano(dano);
        System.out.println(nome + " causou " + dano + " de dano em " + alvo.getNome() + "!");
    }

    public void defender() {
        int cura = 5 + random.nextInt(6); // 5~10
        vidaAtual += cura;
        if (vidaAtual > vidaMaxima) vidaAtual = vidaMaxima;
        System.out.println(nome + " se defendeu e recuperou " + cura + " de vida!");
    }

    public void usarHabilidade(Personagem alvo) {
        int dano = ataqueBase * 2;
        alvo.receberDano(dano);
        System.out.println(nome + " usou HABILIDADE ESPECIAL e causou " + dano + " de dano em " + alvo.getNome() + "!");
    }

    public void receberDano(int dano) {
        vidaAtual -= dano;
        if (vidaAtual < 0) vidaAtual = 0;
    }
}