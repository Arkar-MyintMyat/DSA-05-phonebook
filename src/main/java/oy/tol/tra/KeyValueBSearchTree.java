package oy.tol.tra;

public class KeyValueBSearchTree<K extends Comparable<K>, V> implements Dictionary<K, V> {

    // This is the BST implementation, KeyValueHashTable has the hash table
    // implementation

    private TreeNode<K, V> root;
    private int count = 0;
    private int maxTreeDepth = 0;

    @Override
    public Type getType() {
        return Type.BST;
    }

    @Override
    public int size() {
        // TODO: Implement this
        return count;
    }

    /**
     * Prints out the statistics of the tree structure usage.
     * Here you should print out member variable information which tell something
     * about
     * your implementation.
     * <p>
     * For example, if you implement this using a hash table, update member
     * variables of the class
     * (int counters) in add(K) whenever a collision happen. Then print this counter
     * value here.
     * You will then see if you have too many collisions. It will tell you that your
     * hash function
     * is good or bad (too much collisions against data size).
     */
    @Override
    public String getStatus() {
        String toReturn = "Tree has max depth of " + maxTreeDepth + ".\n";
        toReturn += "Longest collision chain in a tree node is " + TreeNode.longestCollisionChain + "\n";
        if (null == root) {
            return toReturn;
        }
        TreeAnalyzerVisitor<K, V> visitor = new TreeAnalyzerVisitor<>();
        root.accept(visitor);
        toReturn += "Min path height to bottom: " + visitor.minHeight + "\n";
        toReturn += "Max path height to bottom: " + visitor.maxHeight + "\n";
        toReturn += "Ideal height if balanced: " + Math.ceil(Math.log(count)) + "\n";
        return toReturn;
    }

    @Override
    public boolean add(K key, V value) throws IllegalArgumentException, OutOfMemoryError {
        // TODO: Implement this
        // Remember null check.
        if (null == key || null == value) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }
        // If root is null, should go there.
        if (null == root) {
            root = new TreeNode<>(key, value);
            count = 1;
            if (maxTreeDepth < 1) {
                maxTreeDepth = 1;
            }
            return true;
        }
        int hash = key.hashCode();
        TreeNode<K, V> current = root;
        int depth = 1;
        while (true) {
            int comparison = compare(key, hash, current.keyValue.getKey());
            if (comparison == 0) {
                // update the root node. But it may have children
                // so do not just replace it with this new node but set
                // the keys and values for the already existing root.
                current.keyValue.setValue(value);
                return true;
            }
            depth++;
            if (comparison < 0) {
                if (null == current.left) {
                    current.left = new TreeNode<>(key, value);
                    break;
                }
                current = current.left;
            } else {
                if (null == current.right) {
                    current.right = new TreeNode<>(key, value);
                    break;
                }
                current = current.right;
            }
        }
        count++;
        if (depth > maxTreeDepth) {
            maxTreeDepth = depth;
        }
        return true;
    }

    private int compare(K key, int keyHash, K nodeKey) {
        int nodeHash = nodeKey.hashCode();
        if (keyHash < nodeHash) {
            return -1;
        }
        if (keyHash > nodeHash) {
            return 1;
        }
        if (key.equals(nodeKey)) {
            return 0;
        }
        int comparison = key.compareTo(nodeKey);
        if (comparison == 0) {
            return 1;
        }
        return comparison;
    }

    @Override
    public V find(K key) throws IllegalArgumentException {
        // TODO: Implement this. //Think about this
        if (null == key) {
            throw new IllegalArgumentException("Key to find cannot be null");
        }
        int hash = key.hashCode();
        TreeNode<K, V> current = root;
        while (null != current) {
            int comparison = compare(key, hash, current.keyValue.getKey());
            if (comparison == 0) {
                return current.keyValue.getValue();
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return (null);
    }

    @Override
    public void ensureCapacity(int size) throws OutOfMemoryError {
        // Nothing to do here. Trees need no capacity.
    }

    @Override
    public Pair<K, V>[] toSortedArray() {
        if (null == root) {
            TreeToArrayVisitor<K, V> empty = new TreeToArrayVisitor<>(0);
            return empty.getArray();
        }
        TreeToArrayVisitor<K, V> visitor = new TreeToArrayVisitor<>(count);
        root.accept(visitor);
        Pair<K, V>[] sorted = visitor.getArray();
        Algorithms.fastSort(sorted);
        return sorted;
    }

    @Override
    public void compress() throws OutOfMemoryError {
        // Nothing to do here, since BST does not use extra space like array based
        // structures.
    }

}
