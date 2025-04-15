public class HabilidadeNode{
    String nome;
    int dano;
    HabilidadeNode proximo;

    HabilidadeNode(String nome, int dano) {
        this.nome = nome;
        this.dano = dano;
        this.proximo = null;
    }
}