package maps;

public class TreeMapNode<K, V> {
    public K key;
    public V value;
    public TreeMapNode<K, V> left;
    public TreeMapNode<K, V> right;
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
