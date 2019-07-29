package jmerkle.sequential;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Leaf extends JMerkle {

    private static final long serialVersionUID = 7479498736319778107L;

    public byte[] userKey;
    public List<String> keys = new ArrayList<>();

    /* default */Leaf() {
    }

    /* default */Leaf(byte[] userKey, byte[] hashVal) {
        this.hashVal = hashVal;
        this.userKey = userKey;
    }

    @Override
    JMerkle alterInternal(int offset, List<JMerkleAlterable> alterations) {

        JMerkle context = this;

        if (alterations != null) {

            int alterationsSize = alterations.size();

//            System.out.print("alterationsSize:" + alterationsSize);
            if (alterationsSize > 1) {
                System.out.print("alterationsSize:" + alterationsSize);
                alterations.forEach(alteration -> System.out.print(",key:" + alteration.getKey() + ",value:" + JSON.toJSONString(alteration.getValue())));
                System.out.println();
            }

            alterations.forEach(alteration -> keys.add(alteration.getKey()));

            for (int i = 0; i < alterationsSize; i++) {
                JMerkleAlterable alteration = alterations.get(i);

                Serializable value = alteration.getValue();
                String key = alteration.getKey();

                if (this.userKey == null) {
                    if (value != null) {
                        // new tree:
                        this.hashVal = JMerkle.hash(value);
                        this.userKey = key.getBytes();
                    }
                } else {
                    if (Arrays.equals(this.userKey, key.getBytes())) {
                        // alteration to _this_ leaf:
                        this.hashVal = JMerkle.hash(value);

                    } else {
                        // create a new Branch:
                        Branch branch = new Branch();
                        // put the initial leaf (this one):
                        byte offsetKey = JMerkle.hash(this.userKey)[offset];
                        branch.children.put(offsetKey, this);
                        // insert the remaining alterations
                        // and switch the context to the result:
                        context = branch.alterInternal(offset,
                                alterations.subList(i, alterationsSize));
                        // inserting on the branch took care of everything;
                        // break out of the loop:
                        break;
                    }
                }
            }
        }

        return context == null || context.hashVal == null ? null : context;
    }

    @Override
    boolean isBranch() {
        return false;
    }

    @Override
    List<UserKeyWrapper> allKeysInternal() {
        if (this.userKey == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(new UserKeyWrapper(userKey));
        }
    }

    @Override
    public int hashCode() {
        return (Arrays.hashCode(userKey) + Arrays.hashCode(hashVal));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Leaf) {
            Leaf thatLeaf = (Leaf) obj;
            return Arrays.equals(userKey, thatLeaf.userKey)
                    && Arrays.equals(hashVal, thatLeaf.hashVal);
        }
        return false;
    }

    @Override
    int offset() {
        // type (1) + hashVal (20) + userKey offset (4) + userKey.length
        return 25 + userKey.length;
    }
}