import java.util.Scanner;

public class Batalha {
    private int idBatalha;
    private int turnoAtual;
    private boolean batalhaEmAndamento;

    private NoFila filaTurnos;
    private NoPilha pilhaRanking;

    private static int geradorId = 1;

    private Scanner scanner;

    public Batalha() {
        this.idBatalha = geradorId++;
        this.turnoAtual = 1;
        this.batalhaEmAndamento = true;
        this.filaTurnos = null;
        this.pilhaRanking = null;
        this.scanner = new Scanner(System.in);
    }

    private class NoFila {
        Personagem personagem;
        NoFila proximo;

        NoFila(Personagem personagem) {
            this.personagem = personagem;
        }
    }

    private class NoPilha {
        Personagem personagem;
        NoPilha proximo;

        NoPilha(Personagem personagem) {
            this.personagem = personagem;
        }
    }

    private boolean jaEstaNaFila(Personagem personagem) {
        if (filaTurnos == null) return false;
        NoFila atual = filaTurnos;
        do {
            if (atual.personagem.equals(personagem)) {
                return true;
            }
            atual = atual.proximo;
        } while (atual != filaTurnos);
        return false;
    }   
    
    public void adicionarParticipante(Personagem personagem) {
        if (personagem == null || !personagem.estaVivo()) return;
    
        // Verifica se o personagem já está na fila
        if (jaEstaNaFila(personagem)) {
            System.out.println("----- Personagem " + personagem.getNome() + " já está na batalha! -----");
            return;
        }
        NoFila novo = new NoFila(personagem);
        if (filaTurnos == null) {
            filaTurnos = novo;
            novo.proximo = novo; 
        } else {
            novo.proximo = filaTurnos.proximo;
            filaTurnos.proximo = novo;
            filaTurnos = novo;  // o novo vira o último
        }
    
        System.out.println("----- Personagem " + personagem.getNome() + " foi adicionado à batalha! -----");
    }

    public void iniciarBatalha() {
        if (filaTurnos == null) {
            System.out.println("----- Nenhum personagem para iniciar a batalha -----");
            return;
        }
        batalhaEmAndamento = true;
        System.out.println(" ==== BATALHA " + idBatalha + " INICIADA! ====");
        while (batalhaEmAndamento) {
            executarTurno();
            verificarVencedor();
        }
        exibirRankingFinal();
    }

    private void executarTurno() {
        if (!batalhaEmAndamento || filaTurnos == null) return;

        NoFila atual = filaTurnos.proximo;
        Personagem personagem = atual.personagem;

        System.out.println("\n--- Turno " + turnoAtual + " ---");
        System.out.println("=== VEZ DE: " + personagem.getNome() +" === |=|=|=| === VIDA === " + personagem.getVidaAtual() + "/" + personagem.getVidaMaxima());
        System.out.println("Saldo de moedas: " + personagem.getDono().getSaldoMoedas());

        System.out.println("====== ESCOLHA SUA AÇÃO =====");
        System.out.println("1 - ===== ATACAR ============");
        System.out.println("2 - ===== DEFENDER ==========");
        System.out.println("3 - ===== USAR HABILIDADE ===");
        System.out.println("4 - ===== USAR ITEM =========");
        System.out.println("5 - ===== FUGIR =============");
        System.out.println("===== SUA ESCOLHA:");

        int escolha = scanner.nextInt();
        scanner.nextLine();

        switch (escolha) {
            case 1:
                atacar(personagem);
                break;
            case 2:
                defender(personagem);
                break;
            case 3:
                usarHabilidade(personagem);
                break;
            case 4:
                usarItem(personagem);
                break;
            case 5:
                fugir(personagem);
                return;
            default:
                System.out.println("----Ação inválida! Perdeu o turno----");
        }

        if (personagem.estaVivo()) {
            filaTurnos = atual;
        }

        turnoAtual++;
    }

    private void atacar(Personagem atacante) {
        Personagem alvo = escolherAlvo(atacante);
        if (alvo != null) {
            alvo.receberDano(10);
            System.out.println(atacante.getNome() + " ---- ATACOU ---- " + alvo.getNome() + " causando 10 de dano!");
            if (!alvo.estaVivo()) {
                System.out.println(alvo.getNome() + " ===== FOI DERROTADO! =====");
                empilharRanking(alvo);
                removerDaFila(encontrarNoFila(alvo));
            }
        } else {
            System.out.println("----- Nenhum alvo disponível -----");
        }
    }

    private void defender(Personagem personagem) {
        personagem.curar(5);
        System.out.println(personagem.getNome() + " ---- DEFENDEU --- e se curou em 5 pontos!");
    }

    private void usarHabilidade(Personagem personagem) {
        if (personagem.temHabilidade()) {
            Personagem alvo = escolherAlvo(personagem);
            if (alvo != null) {
                alvo.receberDano(20);
                System.out.println(personagem.getNome() + " ---- USOU UMA HABILIDADE ---- causou 20 de dano em: " + alvo.getNome() + "!");
                if (!alvo.estaVivo()) {
                    System.out.println(alvo.getNome() + " ===== FOI DERROTADO! =====");
                    empilharRanking(alvo);
                    removerDaFila(encontrarNoFila(alvo));
                }
            }
        } else {
            System.out.println("-------------Nenhuma habilidade disponível no inventário.-------------");
        }
    }

    private void usarItem(Personagem personagem) {
        if (personagem.temItem()) {
            Personagem alvo = escolherAlvo(personagem);
            if (alvo != null) {
                alvo.receberDano(10);
                System.out.println(personagem.getNome() + " ---- USOU UM ITEM ---- e causou 10 de dano em:  " + alvo.getNome() + "!");
                if (!alvo.estaVivo()) {
                    System.out.println(alvo.getNome() + " ===== FOI DERROTADO! =====");
                    personagem.mostrarInventario();
                    empilharRanking(alvo);
                    removerDaFila(encontrarNoFila(alvo));
                }
            }
        } else {
            System.out.println("-------------Nenhum item disponível no inventário.-------------");
        }
    }

    private void fugir(Personagem personagem) {
        System.out.println(personagem.getNome() + " === FUJAO! ===");
        personagem.getDono().removerMoedas(20);
        System.out.println(personagem.getDono().getNome() + " perdeu 20 MOEDAS por fugir da batalha.");
        personagem.mostrarInventario();
        empilharRanking(personagem);
        removerDaFila(encontrarNoFila(personagem));
        verificarVencedor();
    }

    private Personagem escolherAlvo(Personagem atacante) {
        NoFila atual = filaTurnos.proximo;
        do {
            if (!atual.personagem.equals(atacante) && atual.personagem.estaVivo()) {
                return atual.personagem;
            }
            atual = atual.proximo;
        } while (atual != filaTurnos.proximo);

        return null;
    }

    private void empilharRanking(Personagem derrotado) {
        NoPilha novo = new NoPilha(derrotado);
        novo.proximo = pilhaRanking;
        pilhaRanking = novo;
    }

    private void removerDaFila(NoFila derrotado) {
        if (filaTurnos == null || derrotado == null) return;

        NoFila atual = filaTurnos;
        do {
            if (atual.proximo == derrotado) {
                if (derrotado == filaTurnos) {
                    if (filaTurnos == filaTurnos.proximo) {
                        filaTurnos = null;
                    } else {
                        atual.proximo = derrotado.proximo;
                        filaTurnos = atual;
                    }
                } else {
                    atual.proximo = derrotado.proximo;
                }
                return;
            }
            atual = atual.proximo;
        } while (atual != filaTurnos);
    }

    private NoFila encontrarNoFila(Personagem personagem) {
        if (filaTurnos == null) return null;

        NoFila atual = filaTurnos;
        do {
            if (atual.proximo.personagem.equals(personagem)) {
                return atual.proximo;
            }
            atual = atual.proximo;
        } while (atual != filaTurnos);

        return null;
    }

    private void verificarVencedor() {
        if (filaTurnos == null || filaTurnos.proximo == filaTurnos) {
            batalhaEmAndamento = false;
            if (filaTurnos != null) {
                empilharRanking(filaTurnos.personagem);
                System.out.println("\n\n=====VENCEDOR=====: " + filaTurnos.personagem.getNome());
                filaTurnos.personagem.getDono().adicionarMoedas(50);
                filaTurnos.personagem.subirNivel();
                System.out.println(filaTurnos.personagem.getDono().getNome() + " recebeu 50 MOEDAS e subiu de nível!");
            }
        }
    }

    private void exibirRankingFinal() {
        System.out.println("\n===== RANKING FINAL =====");
        NoPilha atual = pilhaRanking;
        int posicao = 1;
        while (atual != null) {
            System.out.println(posicao + "º - " + atual.personagem.getNome());
            atual = atual.proximo;
            posicao++;
        }
    }
}