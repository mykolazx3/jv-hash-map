package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int RESIZE_COEFFICIENT = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkTableLength();

        Node<K, V> newNode = new Node<>(generateHash(key), key, value, null);
        int entityIndex = generateEntityIndex(newNode.hash);
        Node<K, V> currentEntity = table[entityIndex];

        if (currentEntity == null) {
            table[entityIndex] = newNode;
        } else {
            while (true) {
                if (Objects.equals(currentEntity.key, key)) {
                    currentEntity.value = value;
                    return;
                }
                if (currentEntity.next == null) {
                    currentEntity.next = newNode;
                    break;
                }
                currentEntity = currentEntity.next;
            }
        }
        size++;
    }

    private int generateHash(K key) {
        int result = 31 * (key != null ? key.hashCode() : 0);
        return result;
    }

    int generateEntityIndex(int hash) {
        return Math.abs(hash & (table.length - 1));
    }

    private void checkTableLength() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            copyEntitiesToNewTable();
        }
    }

    private void copyEntitiesToNewTable() {
        Node<K, V>[] currentTable = table;
        Node<K, V>[] newTable = new Node[table.length * RESIZE_COEFFICIENT];

        table = newTable;
        size = 0;

        for (Node<K, V> currentEntity : currentTable) {
            while (currentEntity != null) {
                put(currentEntity.key, currentEntity.value);
                currentEntity = currentEntity.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int entityIndex = generateEntityIndex(generateHash(key));
        Node<K, V> currentEntity = table[entityIndex];

        while (currentEntity != null) {
            if (Objects.equals(currentEntity.key, key)) {
                return currentEntity.value;
            }
            currentEntity = currentEntity.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void show() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> entityOnCurrentIndex = table[i];

                do {
                    System.out.print("[ " + i + " ]   ");
                    System.out.println("K: " + entityOnCurrentIndex.key
                            + "    V: " + entityOnCurrentIndex.value);

                    if (entityOnCurrentIndex.next != null) {
                        entityOnCurrentIndex = entityOnCurrentIndex.next;
                    } else {
                        break;
                    }
                } while (true);
            }
        }
        System.out.println("show end");
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
