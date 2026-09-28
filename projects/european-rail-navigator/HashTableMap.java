import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * A hash table map implementation that stores key-value pairs.
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    /**
     * Stores a single key-value pair.
     */
    protected class Pair {
        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }
    }

    // protected 1d array of linked lists to store key-value pairs
    protected LinkedList<Pair>[] table = null;

    // private size field to track number of keys stored in this collection
    private int size;

    /**
     * Creates a new hash table with the given capacity.
     * 
     * @param capacity initial capacity of the hash table
     * @throws IllegalArgumentException if capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public HashTableMap(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
        size = 0;
    }

    /**
     * Creates a new hash table with default capacity 8.
     */
    public HashTableMap() {
        this(8);
    }

    /**
     * Private helper method to compute the index in the  array for a given key.
     * Absolute value of key's hash code mod the capacity of the hash table.
     * @param key the key to compute the index for
     * @return the index in the array for the given key
     */
    private int getIndex(KeyType key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Private helper method to resize the hash table when load factor exceeds 0.75.
     * Creates a new array with double the capacity and rehashes all existing key-value pairs into the new array.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Pair>[] oldTable = table;
        table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];

        for (LinkedList<Pair> bucket : oldTable) {
            if (bucket != null) {
                for (Pair pair : bucket) {
                    int index = getIndex(pair.key);
                    if (table[index] == null) {
                        table[index] = new LinkedList<>();
                    }
                    table[index].add(new Pair(pair.key, pair.value));
                }
            }
        }
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * 
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        if (containsKey(key)) {
            throw new IllegalArgumentException("key already maps to a value");
        }

        if ((size + 1.0) / table.length >= 0.75) {
            resize();
        }

        int index = getIndex(key);

        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        table[index].add(new Pair(key, value));
        size++;
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * 
     * @param key the key to check
     * @return true if key is stored, false otherwise
     * @throws NullPointerException if key is null
     */
    @Override
    public boolean containsKey(KeyType key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        int index = getIndex(key);

        if (table[index] == null) {
            return false;
        }

        for (Pair pair : table[index]) {
            if (pair.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * 
     * @param key the key to look up
     * @return the value mapped to by key
     * @throws NoSuchElementException if key is not stored
     * @throws NullPointerException if key is null
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        int index = getIndex(key);

        if (table[index] == null) {
            throw new NoSuchElementException("key is not stored");
        }

        for (Pair pair : table[index]) {
            if (pair.key.equals(key)) {
                return pair.value;
            }
        }

        throw new NoSuchElementException("key is not stored");
    }

    /**
     * Removes the mapping for a key from this collection.
     * 
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException if key is not stored
     * @throws NullPointerException if key is null
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        if (key == null) {
            throw new NullPointerException("key is null");
        }

        int index = getIndex(key);

        if (table[index] == null) {
            throw new NoSuchElementException("key is not stored");
        }

        for (int i = 0; i < table[index].size(); i++) {
            Pair pair = table[index].get(i);
            if (pair.key.equals(key)) {
                ValueType removedValue = pair.value;
                table[index].remove(i);
                size--;
                return removedValue;
            }
        }

        throw new NoSuchElementException("key is not stored");
    }

    /**
     * Removes all key,value pairs from this collection without changing capacity.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        table = (LinkedList<Pair>[]) new LinkedList[table.length];
        size = 0;
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * 
     * @return number of keys stored
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Retrieves this collection's capacity.
     * 
     * @return size of underlying array
     */
    @Override
    public int getCapacity() {
        return table.length;
    }

    /**
     * Retrieves this collection's keys.
     * 
     * @return a list of keys in the underlying array
     */
    @Override
    public List<KeyType> getKeys() {
        List<KeyType> keys = new LinkedList<>();

        for (LinkedList<Pair> bucket : table) {
            if (bucket != null) {
                for (Pair pair : bucket) {
                    keys.add(pair.key);
                }
            }
        }

        return keys;
    }
}
