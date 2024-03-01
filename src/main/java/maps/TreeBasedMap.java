package maps;

import com.sun.source.tree.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;

public class TreeBasedMap<K, V> implements CustomMutableMap<K, V> {
    public TreeMapNode<K, V> root;
    public TreeMapNode<K, V> parent;
    public TreeMapNode<K, V> current;
    public Comparator<K> keyComparator;
    public TreeBasedMap(Comparator<K> keyComparator, TreeMapNode<K, V> root) {
        this.keyComparator = keyComparator;
        this.root = root;
    }
    @NotNull
    @Override
    public Iterable<Entry<K, V>> getEntries() {
        private class EntriesIterator implements Iterator<Entry<K, V>> {
            @Override
            public boolean hasNext() {
                
            }

            @Override
            public Entry<K, V> next() {
                return null;
            }
        }
    }

    @NotNull
    @Override
    public Iterable<K> getKeys() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<V> getValues() {
        return null;
    }

    @Nullable
    @Override
    public V get(K key) {
        while (current.value == key) {
            if (current == null) {
                return null;
            }
            else if (keyComparator.compare(key, current.getKey()) >= 0) {
                parent = current;
                current = current.getRight();
            } else if (keyComparator.compare(key, current.getKey()) < 0) {
                parent = current;
                current = current.getLeft();
            }
        }
        return current.value;
    }

    @Nullable
    @Override
    public V set(K key, V value) {
        while (current.value == key) {
            if (current == null) {
                return null;
            }
            else if (keyComparator.compare(key, current.getKey()) >= 0) {
                parent = current;
                current = current.getRight();
            } else if (keyComparator.compare(key, current.getKey()) < 0) {
                parent = current;
                current = current.getLeft();
            }
        }
        V val = current.value;
        current.key = key;
        current.value = value;
        return val;
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        while (current.value == key) {
            if (current == null) {
                current  = TreeMapNode<K, V>(key, value);
                return null;
            }
            else if (keyComparator.compare(key, current.getKey()) >= 0) {
                parent = current;
                current = current.getRight();
            } else if (keyComparator.compare(key, current.getKey()) < 0) {
                parent = current;
                current = current.getLeft();
            }
        }
        V val = current.value;
        current.key = key;
        current.value = value;
        return val;
    }

    @Nullable
    @Override
    public V put(@NotNull Entry<K, V> entry) {
        return this.put(entry.getKey(), entry.getValue());
    }

    @Nullable
    @Override
    public V remove(K key) {
        while (current.key == key) {
            if (current == null) {
                return null;
            }
            else if (keyComparator.compare(key, current.getKey()) >= 0) {
                parent = current;
                current = current.getRight();
            } else if (keyComparator.compare(key, current.getKey()) < 0) {
                parent = current;
                current = current.getLeft();
            }
        }
        V val = current.value;
        if (current.getLeft() != null) {
            parent = current;
            current = current.getLeft();
        } else if (current.getRight() != null) {
            parent = current;
            current = current.getRight();
        } else {
            return val;
        }

        while (current.key == null) {
            if (current.getLeft() != null) {
                current.key = current.getLeft().key;
                current.value = current.getLeft().value;
                parent = current;
                current = current.getLeft();
            } else if (current.getRight() != null) {
                current.key = current.getRight().key;
                current.value = current.getRight().value;
                parent = current;
                current = current.getRight();
            }
        }

        parent.setLeft(null);
        parent.setRight(null);
        return val;
    }

    @Override
    public boolean contains(K key) {
        return false;
    }
}
