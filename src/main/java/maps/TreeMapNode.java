package maps;

public class TreeMapNode<K, V> {
    private K key;
    private V value;
    private TreeMapNode<K, V> left;
    private TreeMapNode<K, V> right;

    public TreeMapNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    void setLeft(TreeMapNode<K, V> newLeft) {
        left = newLeft;
    }

    void setRight(TreeMapNode<K, V> newRight) {
        right = newRight;
    }

    TreeMapNode<K, V> getLeft() {
        return left;
    }

    TreeMapNode<K, V> getRight() {
        return right;
    }

    K getKey() {
        return key;
    }

    V getValue() {
        return value;
    }

    void setKey(K newKey) {
        key = newKey;
    }

    void setValue(V newValue) {
        value = newValue;
    }
}
