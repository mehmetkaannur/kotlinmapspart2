package maps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class TreeBasedMap<K, V> implements CustomMutableMap<K, V> {

    private TreeMapNode<K, V> root = null;
    private final Comparator<K> keyComparator;

    public TreeBasedMap(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
    }

    private class EntriesIterator implements Iterator<Entry<K, V>> {

        Deque<TreeMapNode<K, V>> stack;
        TreeMapNode<K, V> current;

        public EntriesIterator() {
            this.stack = new LinkedList<TreeMapNode<K, V>>();
            this.current = root;
        }


        @Override
        public boolean hasNext() {
            return (!stack.isEmpty()) || current != null;
        }

        @Override
        public Entry<K, V> next() {
            TreeMapNode<K, V> next;
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }
            next = stack.pop();
            current = next.getRight();
            return new Entry<K, V>(next.getKey(), next.getValue());
        }
    }

//    public ArrayList<Entry<K, V>> entryFinder(TreeMapNode<K, V> node) {
//        ArrayList<Entry<K, V>> entries = new ArrayList<>();
//        if (node.getRight() != null) {
//            entries.addAll(entryFinder(node.getRight()));
//        }
//        if (node.getLeft() != null) {
//            entries.addAll(entryFinder(node.getLeft()));
//        }
//        entries.add(new Entry<K, V>(node.getKey(), node.getValue()));
//        return entries;
//    }

    @NotNull
    @Override
    public Iterable<Entry<K, V>> getEntries() {
        return EntriesIterator::new;
    }

    @NotNull
    @Override
    public Iterable<K> getKeys() {
        ArrayList<K> keys = new ArrayList<K>();
        for (Entry<K, V> entry : this.getEntries()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @NotNull
    @Override
    public Iterable<V> getValues() {
        ArrayList<V> values = new ArrayList<V>();
        for (Entry<K, V> entry : this.getEntries()) {
            values.add(entry.getValue());
        }
        return values;
    }

    @Nullable
    @Override
    public V get(K key) {
        TreeMapNode<K, V> current = root;
        while (current != null) {
            int compareResult = keyComparator.compare(key, current.getKey());
            if (compareResult == 0) {
                return current.getValue();
            }
            if (compareResult < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public V set(K key, V value) {
        TreeMapNode<K, V> newNode = new TreeMapNode<>(key, value);
        if (root == null) {
            root = newNode;
            return null;
        }
        TreeMapNode<K, V> current = root, next;
        while (true) {
            int compareResult = keyComparator.compare(key, current.getKey());
            if (compareResult == 0) {
                V result = current.getValue();
                current.setValue(value);
                return result;
            }
            if (compareResult < 0) {
                next = current.getLeft();
                if (next == null) {
                    current.setLeft(newNode);
                    return null;
                }
            } else {
                next = current.getRight();
                if (next == null) {
                    current.setRight(newNode);
                    return null;
                }
            }
            current = next;
        }
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return this.set(key, value);
    }

    @Nullable
    @Override
    public V put(@NotNull Entry<K, V> entry) {
        return this.set(entry.getKey(), entry.getValue());
    }

    @Nullable
    @Override
    public V remove(K key) {
        TreeMapNode<K, V> parent = null;
        TreeMapNode<K, V> current = root;
        while (current != null && keyComparator.compare(key, current.getKey()) !=
                0) {
            parent = current;
            if (keyComparator.compare(key, current.getKey()) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        if (current == null) {
            return null;
        }
        V value = current.getValue();
        if (current.getLeft() == null) {
            deleteNode(current, parent);
        } else if (current.getRight() == null) {
            deleteNode(current, parent);
        } else {
            TreeMapNode<K, V> nodeWithValueToDelete = current;
            parent = current;
            current = current.getRight();
            while (current.getLeft() != null) {
                parent = current;
                current = current.getLeft();
            }
            nodeWithValueToDelete.setKey(current.getKey());
            nodeWithValueToDelete.setValue(current.getValue());
            deleteNode(current, parent);
        }
        return value;
    }

    void deleteNode(TreeMapNode<K, V> nodeToDelete, TreeMapNode<K, V> parent) {
        if (parent == null) {
            if (nodeToDelete.getLeft() == null) {
                root = nodeToDelete.getRight();
            } else {
                root = nodeToDelete.getLeft();
            }
        } else if (parent.getLeft() == nodeToDelete) {
            if (nodeToDelete.getLeft() == null) {
                parent.setLeft(nodeToDelete.getRight());
            } else {
                parent.setLeft(nodeToDelete.getLeft());
            }
        } else {
            if (nodeToDelete.getLeft() == null) {
                parent.setRight(nodeToDelete.getRight());
            } else {
                parent.setRight(nodeToDelete.getLeft());
            }
        }
    }

    @Override
    public boolean contains(K key) {
        for (K keyFinder : this.getKeys()) {
            if (keyFinder == key) {
                return true;
            }
        }
        return false;
    }
}
