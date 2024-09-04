import java.util.Iterator;

import components.binarytree.BinaryTree;
import components.binarytree.BinaryTree1;
import components.set.Set;
import components.set.SetSecondary;

/**
 * {@code Set} represented as a {@code BinaryTree} (maintained as a binary
 * search tree) of elements with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code Set} elements
 * @mathdefinitions <pre>
 * IS_BST(
 *   tree: binary tree of T
 *  ): boolean satisfies
 *  [tree satisfies the binary search tree properties as described in the
 *   slides with the ordering reported by compareTo for T, including that
 *   it has no duplicate labels]
 * </pre>
 * @convention IS_BST($this.tree)
 * @correspondence this = labels($this.tree)
 *
 * @author K. Abukar
 *
 */
public class Set3a<T extends Comparable<T>> extends SetSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Elements included in {@code this}.
     */
    private BinaryTree<T> tree;

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    private static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        /*
         * isInTree boolean is false by default.
         */
        boolean isInTree = false;
        /*
         * If the tree has any labels (including the root), then check if the
         * root of the tree equals x. If it does, then isInTree is true.
         */
        if (t.size() > 0) {
            if (t.root().equals(x)) {
                isInTree = true;
                /*
                 * Otherwise, disassemble t.
                 */
            } else {
                BinaryTree<T> left = t.newInstance();
                BinaryTree<T> right = t.newInstance();
                T root = t.disassemble(left, right);
                /*
                 * Check whether x is less than or greater than the root (since
                 * the tree is sorted in this way). Recursively call isInTree on
                 * the left and right trees, respectively.
                 */
                if (x.compareTo(root) < 0) {
                    isInTree = isInTree(left, x);
                } else {
                    isInTree = isInTree(right, x);
                }
                // Reassemble the tree to restore initial state.
                t.assemble(root, left, right);
            }
        }
        return isInTree;
    }

    /**
     * Inserts {@code x} in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be inserted
     * @aliases reference {@code x}
     * @updates t
     * @requires IS_BST(t) and x is not in labels(t)
     * @ensures IS_BST(t) and labels(t) = labels(#t) union {x}
     */
    private static <T extends Comparable<T>> void insertInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        /*
         * If the tree is empty, then make x the root of its own tree with two
         * empty left and right subtrees.
         */
        if (t.size() == 0) {
            t.assemble(x, t.newInstance(), t.newInstance());
            /*
             * Otherwise, disassemble t.
             */
        } else {
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T root = t.disassemble(left, right);
            /*
             * Check whether x is less than or greater than the root (since the
             * tree is sorted in this way). Recursively call insertInTree on the
             * left and right trees, respectively.
             */
            if (x.compareTo(root) < 0) {
                insertInTree(left, x);
            } else {
                insertInTree(right, x);
            }
            // Reassemble tree to restore default state.
            t.assemble(root, left, right);
        }
    }

    /**
     * Removes and returns the smallest (left-most) label in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove the label
     * @return the smallest label in the given {@code BinaryTree}
     * @updates t
     * @requires IS_BST(t) and |t| > 0
     * @ensures <pre>
     * IS_BST(t)  and  removeSmallest = [the smallest label in #t]  and
     *  labels(t) = labels(#t) \ {removeSmallest}
     * </pre>
     */
    private static <T> T removeSmallest(BinaryTree<T> t) {
        assert t != null : "Violation of: t is not null";
        assert t.size() > 0 : "Violation of: |t| > 0";
        // Initialize smallest variable to the root of t.
        T smallest = t.root();
        /*
         * If t has a size greater than 1, then it must have subtree(s).
         */
        if (t.size() > 1) {
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T root = t.disassemble(left, right);
            /*
             * If the left subtree is not empty, then recursively call
             * removeSmallest on the left subtree to find the left-most label.
             */
            if (left.size() > 0) {
                smallest = removeSmallest(left);
                /*
                 * Otherwise, only the right subtree is non-empty, and thus, the
                 * root of the tree is the smallest label. You need to replace
                 * the root with the second smallest label of the tree (the
                 * left-most label in the right tree). Do so by recursively
                 * calling removeSmallest on the right subtree.
                 */
            } else {
                root = removeSmallest(right);
            }
            t.assemble(root, left, right);
            /*
             * Otherwise, t must not have subtrees, and thus it is the smallest
             * label. Clear t to "remove" it, since you already saved its value
             * initially.
             */
        } else {
            t.clear();
        }
        return smallest;
    }

    /**
     * Finds label {@code x} in {@code t}, removes it from {@code t}, and
     * returns it.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove label {@code x}
     * @param x
     *            the label to be removed
     * @return the removed label
     * @updates t
     * @requires IS_BST(t) and x is in labels(t)
     * @ensures <pre>
     * IS_BST(t)  and  removeFromTree = x  and
     *  labels(t) = labels(#t) \ {x}
     * </pre>
     */
    private static <T extends Comparable<T>> T removeFromTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        assert t.size() > 0 : "Violation of: x is in labels(t)";
        // Declare and then initialize left, right, and root by disassembling t.
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        T root = t.disassemble(left, right);
        // Initialize toRemove to be the root.
        T toRemove = root;
        // Holds the value of the comparison between the root and x (sought-out label).
        int compare = x.compareTo(root);
        /*
         * If the root is the sought-out label (x), replace the root with the
         * second smallest label of the tree (the left-most label in the right
         * tree, if a right tree exists). Do so by recursively calling
         * removeSmallest on the right subtree. This will replace the initial
         * root, essentially removing it. Otherwise, you can transfer up from
         * the left subtree, because it's already in proper binary tree order
         * given the right subtree doesn't exist.
         */
        if (compare == 0) {
            if (right.size() > 0) {
                root = removeSmallest(right);
                t.assemble(root, left, right);
            } else {
                t.transferFrom(left);
            }
            /*
             * Otherwise, check whether x is less than or greater than the root.
             * Recursively call removeFromTree on the left or right tree,
             * respectively.
             */
        } else if (compare < 0) {
            // Set toRemove to the result of the recursive call on left.
            toRemove = removeFromTree(left, x);
            // Reassemble the tree.
            t.assemble(root, left, right);
        } else {
            // Set toRemove to the result of the recursive call on right+.
            toRemove = removeFromTree(right, x);
            t.assemble(root, left, right);
        }
        // Return the removed element.
        return toRemove;
    }

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.tree = new BinaryTree1<>();
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Set3a() {
        this.createNewRep();
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Set<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Set3a<?> : ""
                + "Violation of: source is of dynamic type Set3<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Set3a<?>, and
         * the ? must be T or the call would not have compiled.
         */
        Set3a<T> localSource = (Set3a<T>) source;
        this.tree = localSource.tree;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert !this.contains(x) : "Violation of: x is not in this";
        // Call insertInTree to implement add.
        insertInTree(this.tree, x);
    }

    @Override
    public final T remove(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.contains(x) : "Violation of: x is in this";
        // Call removeFromTree to implement remove.
        return removeFromTree(this.tree, x);
    }

    @Override
    public final T removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";
        // Call removeSmallest to implement removeAny.
        return removeSmallest(this.tree);
    }

    @Override
    public final boolean contains(T x) {
        assert x != null : "Violation of: x is not null";
        // Call isInTree to implement contains.
        return isInTree(this.tree, x);
    }

    @Override
    public final int size() {
        return this.tree.size();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.tree.iterator();
    }
}
