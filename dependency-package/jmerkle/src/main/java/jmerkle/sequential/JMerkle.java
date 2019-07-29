package jmerkle.sequential;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public abstract class JMerkle implements Serializable {

    private static final long serialVersionUID = 7487888709693360107L;

    /* default */ byte[] hashVal;

    /**
     * Provides the unique leaves between the two JMerkle parameters. Either or
     * both of the values may actually be null.
     */
    public static List<String> diff(JMerkle t1, JMerkle t2) {
        if (t1 != null) {
            if (t2 != null) {
                return t1.diff(t2);
            } else {
                return t1.allKeys();
            }
        } else {
            if (t2 != null) {
                return t2.allKeys();
            } else {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Provides all the leaves on the provided JMerkle. The parameter may be
     * null, in which case, the empty list is returned.
     */
    public static List<String> allkeys(JMerkle t1) {
        if (t1 != null) {
            return t1.allKeys();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Alters the leaf values of the provided JMerkle using the provided list of
     * JMerkleAlterable values. If the t1 parameter is null, creates a new
     * JMerkle and applies the alterations against it.
     */
    public static JMerkle alter(JMerkle t1, List<JMerkleAlterable> alterations) {
        if (t1 == null) {
            t1 = new Leaf();
        }
        return t1.alterInternal(0, alterations);
    }

    /**
     * This method returns a byte[] of size 20. <br/>
     * <b>Sha-1 is used to create the byte[]; its usage is <i>not</i> for
     * cryptographic purposes.</b>
     *
     * @param obj 序列化对象
     * @return byte[]
     */
    /* default */
    static byte[] hash(Serializable obj) {
        byte[] hash = null;
        if (obj != null) {
            byte[] bytes = JMerkle.getBytes(obj);
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            hash = digest.digest(bytes);
        }
        return hash;
    }

    private static byte[] getBytes(Serializable obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
        } catch (IOException e) {
            // nothing recoverable; throw a runtime exception:
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    private List<String> diff(JMerkle jMerkle) {
        System.out.println("this.allKeys().size():" + this.allKeys().size());
        System.out.println("that.allKeys().size():" + jMerkle.allKeys().size());
        List<UserKeyWrapper> internalDiff = this.diffInternal(jMerkle);
        return unwrapKeys(internalDiff);
    }

    private List<String> allKeys() {
        List<UserKeyWrapper> allKeysInternal = allKeysInternal();
        return unwrapKeys(allKeysInternal);
    }

    /* default */
    abstract boolean isBranch();

    /* default */
    abstract JMerkle alterInternal(int offset,
                                   List<JMerkleAlterable> alterations);

    /* default */
    abstract List<UserKeyWrapper> allKeysInternal();

    /* default */
    abstract int offset();

    private List<String> unwrapKeys(List<UserKeyWrapper> wrappedKeys) {
        return new ArrayList<String>() {{
            wrappedKeys.forEach(ukw -> add(new String(ukw.bytes)));
        }};
    }

    private List<UserKeyWrapper> diffInternal(JMerkle that) {
        if (this.isBranch()) {
            if (that.isBranch()) {
//                System.out.println("2 branchs");
                return diff((Branch) this, (Branch) that);
            } else {
//                System.out.println("leaf and branch");
                return diff((Leaf) that, (Branch) this);
            }
        } else {
            if (that.isBranch()) {
//                System.out.println("leaf and branch");
                return diff((Leaf) this, (Branch) that);
            } else {
//                System.out.println("leaf and leaf");
                return diff((Leaf) this, (Leaf) that);
            }
        }
    }

    private List<UserKeyWrapper> diff(Leaf thisLeaf, Leaf thatLeaf) {

        System.out.println(
                "thisLeaf.keys:" + JSON.toJSONString(thisLeaf.keys)
                        + ",thatLeaf.keys:" + JSON.toJSONString(thatLeaf.keys)
        );

        byte[] thisLeafUserKey = thisLeaf.userKey;
        byte[] thatLeafUserKey = thatLeaf.userKey;

        if (thisLeafUserKey == null) {
            if (thatLeafUserKey != null) {
                return Collections.singletonList(new UserKeyWrapper(
                        thatLeafUserKey));
            } else {
                return Collections.emptyList();
            }
        } else {
            if (thatLeafUserKey == null) {
                return Collections.singletonList(new UserKeyWrapper(
                        thisLeafUserKey));
            } else {

                List<UserKeyWrapper> keys;

                if (Arrays.equals(thisLeafUserKey, thatLeafUserKey)) {
                    if (Arrays.equals(thisLeaf.hashVal, thatLeaf.hashVal)) {
                        keys = Collections.emptyList();
                    } else {
                        keys = Collections.singletonList(new UserKeyWrapper(
                                thisLeafUserKey));
                    }
                } else {
                    // if they're different, return both:
                    keys = new ArrayList<>(2);
                    keys.add(new UserKeyWrapper(thatLeafUserKey));
                    keys.add(new UserKeyWrapper(thatLeafUserKey));
                }

                return keys;
            }
        }
    }

    private List<UserKeyWrapper> diff(Leaf leaf, Branch branch) {

        List<UserKeyWrapper> diffKeys = branch.allKeysInternal();

        Boolean contains = branch.contains(leaf);

        UserKeyWrapper leafUserKeyWrapper = new UserKeyWrapper(leaf.userKey);

        if (contains != null && contains) {
            diffKeys.remove(leafUserKeyWrapper);
        } else {
            // it might already be there, but we need to be sure:
            // (to clarify, it might already be there by the virtue
            // that the branch's leaf and the provided leaf's hash
            // vals are indeed different.)
            diffKeys.add(leafUserKeyWrapper);
        }

        return diffKeys;
    }

    private List<UserKeyWrapper> diff(Branch b1, Branch b2) {
        if (Arrays.equals(b1.hashVal, b2.hashVal)) {
            return Collections.emptyList();
        } else {

            List<UserKeyWrapper> diffKeys = new ArrayList<>();

            Map<Byte, JMerkle> b1Children = b1.children;
            Map<Byte, JMerkle> b2Children = b2.children;

            // recursive diff on keys in common:
            Set<Byte> commonKeys = new HashSet<>(b1Children.keySet());
            commonKeys.retainAll(new HashSet<>(b2Children.keySet()));

            for (Byte commonKey : commonKeys) {
                JMerkle b1Child = b1Children.get(commonKey);
                JMerkle b2Child = b2Children.get(commonKey);
                diffKeys.addAll(b1Child.diffInternal(b2Child));
            }

            // all user keys on keys unique to b1:
            Set<Byte> b1UniqueKeys = new HashSet<>(b1Children.keySet());
            b1UniqueKeys.removeAll(new HashSet<>(b2Children.keySet()));

            for (Byte b1UniqueKey : b1UniqueKeys) {
                JMerkle b1Child = b1Children.get(b1UniqueKey);
                diffKeys.addAll(b1Child.allKeysInternal());
            }

            // all user keys on keys unique to b2:
            Set<Byte> b2UniqueKeys = new HashSet<>(b2Children.keySet());
            b2UniqueKeys.removeAll(new HashSet<>(b1Children.keySet()));

            for (Byte b2UniqueKey : b2UniqueKeys) {
                JMerkle b2Child = b2Children.get(b2UniqueKey);
                diffKeys.addAll(b2Child.allKeysInternal());
            }

            return diffKeys;
        }
    }

    /*
     * Used simply for its equals method; specifically, utilizing
     * <code>Arrays.equals(byte[] a, byte[] a2)</code>.
     */
    /* default */static class UserKeyWrapper {
        byte[] bytes;

        UserKeyWrapper(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof UserKeyWrapper && Arrays.equals(bytes, ((UserKeyWrapper) obj).bytes);
        }
    }
}