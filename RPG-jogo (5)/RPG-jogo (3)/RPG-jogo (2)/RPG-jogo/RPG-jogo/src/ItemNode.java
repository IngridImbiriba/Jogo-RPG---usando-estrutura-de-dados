public class ItemNode {

    String nome;
    int quantidade;
    ItemNode proximo;

    ItemNode(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.proximo = null;
    }
}