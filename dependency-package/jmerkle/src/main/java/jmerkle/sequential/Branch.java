package jmerkle.sequential;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class Branch extends JMerkle {

    private static final long serialVersionUID = 240333586693350332L;

    transient int offset;

    // hashVal will differ depending on key order; TreeMap is used
    // in order to maintain idempotent consistency.
    TreeMap<Byte, JMerkle> children = new TreeMap<>();

    /* default */Branch() {
    }

    @Override
    JMerkle alterInternal(int offset, List<JMerkleAlterable> alterations) {

        if (alterations != null) {

            Map<Byte, List<JMerkleAlterable>> collisions = new HashMap<>();

            for (JMerkleAlterable alteration : alterations) {
                byte[] keyBytes = alteration.getKey().getBytes();
                byte offsetKey = JMerkle.hash(keyBytes)[offset];

                if (children.containsKey(offsetKey)) {

                    List<JMerkleAlterable> collisionAlterations = collisions
                            .computeIfAbsent(offsetKey, k -> new ArrayList<>());

                    collisionAlterations.add(alteration);

                } else {
                    Serializable value = alteration.getValue();
                    if (value != null) {
                        // we're in accordance w/ our balance rules...
                        // create and insert the new Leaf:
                        Leaf leaf = new Leaf(keyBytes, JMerkle.hash(value));
                        children.put(offsetKey, leaf);
                    }
                }

            }

            for (Entry<Byte, List<JMerkleAlterable>> collisionEntry : collisions
                    .entrySet()) {

                Byte collisionKey = collisionEntry.getKey();

                List<JMerkleAlterable> pendingAlterations = collisionEntry
                        .getValue();

                JMerkle child = children.get(collisionKey);

                if (child.isBranch()) {
                    child.alterInternal(offset + 1, pendingAlterations);
                } else {
                    // the alteration insert result on a leaf can result in
                    // 1) an update to that leaf,
                    // 2) deletion of that leaf, or
                    // 3) conversion of that leaf into a branch:
                    if (child.alterInternal(offset + 1, pendingAlterations) == null) {
                        // if null, the leaf alteration was a 'delete':
                        children.remove(collisionKey);
                    }
                }
            }
        }

        Collection<JMerkle> values = children.values();
        switch (values.size()) {
            case 0:
                return null;
            case 1: {
                // if it's a leaf, there's no longer a need for this branch.
                JMerkle jMerkle = new ArrayList<>(values).get(0);
                if (!jMerkle.isBranch())
                    return jMerkle;
            }
            default:
                int childBytes = 0;
                for (JMerkle jMerkle : values) {
                    childBytes += jMerkle.offset();
                }
                this.offset = 27 + children.size() + childBytes;
                this.hashVal = null;
                this.hashVal = JMerkle.hash(this);
                return this;
        }
    }

    @Override
    List<UserKeyWrapper> allKeysInternal() {
        List<UserKeyWrapper> allkeys = new ArrayList<>();

        Collection<JMerkle> childValues = children.values();
        for (JMerkle jMerkle : childValues) {
            allkeys.addAll(jMerkle.allKeysInternal());
        }

        return allkeys;
    }

    @Override
    boolean isBranch() {
        return true;
    }

    /**
     * Returns whether the provided <code>Leaf</code> is a child of this
     * <code>Branch</code>. Recursively exhausts child <code>Branch</code>es,
     * but short-circuits once a match has been found.
     *
     * @param leaf a non-null <code>Leaf</code>.
     * @return If a <code>Leaf</code> with an identical key is found while
     * traversing the <code>Branch</code>, the <code>Leaf</code>
     * parameter's hash value is compared to the <code>Branch</code>'s
     * <code>Leaf</code>'s hash value. <code>true</code> is returned if
     * the respective hash values are identical; <code>false</code>
     * otherwise. If a <code>Leaf</code> of identical key is not found
     * and all of the <code>Branch</code>'s children have been
     * exhausted, returns <code>null</code>.
     */
    /* default */Boolean contains(Leaf leaf) {
        // cycle through all the children:
        for (JMerkle jMerkle : children.values()) {
            if (jMerkle.isBranch()) {
                // if branch, rinse and repeat:
                Boolean contains = ((Branch) jMerkle).contains(leaf);
                if (contains != null)
                    return contains;
            } else {
                Leaf childLeaf = (Leaf) jMerkle;
                // if leaf, check its userKey:
                if (Arrays.equals(childLeaf.userKey, leaf.userKey)) {
                    // if userKeys are equal, enable
                    // upstream short-circuit on hashVal:
                    return Arrays.equals(childLeaf.hashVal, leaf.hashVal);
                }
            }
        }
        return null; // explicit null to facilitate short-circuiting logic.
    }

    @Override
    int offset() {
        return offset;
    }
}