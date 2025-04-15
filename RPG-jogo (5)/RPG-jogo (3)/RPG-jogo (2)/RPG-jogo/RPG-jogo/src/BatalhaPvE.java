import java.util.Random;
import java.util.Scanner;

public class BatalhaPvE {

    public Personagem player;
    public Monstro monstro;
    public Scanner scanner;
    public Random random;

    public BatalhaPvE(Personagem player, Monstro monstro) {
        this.player = player;
        this.monstro = monstro;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void iniciarBatalha() {
        System.out.println("\n=== BATALHA CONTRA MONSTRO ===");
        System.out.println("Seu personagem: " + player.getNome() + " (HP: " + player.getVidaAtual() + ")");
        System.out.println("Monstro: " + monstro.getNome() + " (HP: " + monstro.getVidaAtual() + ")\n");

        while (player.getVidaAtual() > 0 && monstro.getVidaAtual() > 0) {
            turnoJogador();
            if (monstro.getVidaAtual() <= 0) {
                System.out.println("\n========= VOCE DERROTOU O MONTRO =========");
                player.receberRecompensas(50, 10, 10); 
            }
            turnoMonstro();
            if (player.getVidaAtual() <= 0) {
                System.out.println("\nO monstro derrotou você...");
            }
        }
    }

    private void turnoJogador() {
        System.out.println("====== ESCOLHA SUA AÇÃO =====");
        System.out.println("1 - ===== ATACAR ============");
        System.out.println("2 - ===== DEFENDER ==========");
        System.out.println("3 - ===== USAR HABILIDADE ===");
        System.out.println("======== SUA ESCOLHA: ");

        int escolha = scanner.nextInt();
        scanner.nextLine();

        switch (escolha) {
            case 1:
                player.atacar(monstro);
                break;
            case 2:
                player.defender();
                break;
            case 3:
                player.usarHabilidade(monstro);
                break;
            default:
                System.out.println("----Ação inválida! Perdeu o turno----");
        }

        System.out.println(player.getNome() + " HP: " + player.getVidaAtual() + " | " + monstro.getNome() + " HP: " + monstro.getVidaAtual());
    }

    private void turnoMonstro() {
        System.out.println("\n=== Turno do Monstro ===");
        int escolha = random.nextInt(3) + 1;

        switch (escolha) {
            case 1:
                monstro.atacar(player);
                System.out.println(monstro.getNome() + "\n ===== ATACOU VOCE =====");
                break;
            case 2:
                monstro.defender();
                System.out.println(monstro.getNome() + "===== ESTA SE DEFENDENDO =====");
                break;
            case 3:
                monstro.usarHabilidade(player);
                System.out.println(monstro.getNome() + "===== USOU SUA HABILIDADE =====");
                break;
        }

        System.out.println(player.getNome() + " SUA VIDA: " + player.getVidaAtual() + " | " + monstro.getNome() + " VIDA MONSTRO: " + monstro.getVidaAtual());
    }
}
