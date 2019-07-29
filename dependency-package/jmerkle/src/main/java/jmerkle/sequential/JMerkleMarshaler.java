package jmerkle.sequential;

import java.io.*;
import java.util.Map.Entry;
import java.util.TreeMap;

public class JMerkleMarshaler {

    public static JMerkle unmarshal(byte[] treeBytes) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(treeBytes);
        DataInputStream dis = new DataInputStream(bais);

        boolean isBranch = dis.readBoolean();
        // get the uppermost node's hashVal:
        byte[] hashVal = new byte[20];
        int bufferBytesRead = dis.read(hashVal);

        if (bufferBytesRead != 20)
            throw new IllegalStateException(
                    "Marshaled JMerkle binary does not contain a 20 byte hash and is therefore corrupt.");

        JMerkle jMerkle;

        if (isBranch) {
            int offset = dis.readInt();
            short numberOfChildren = dis.readShort();
            jMerkle = new Branch();
            jMerkle.hashVal = hashVal;
            ((Branch) jMerkle).offset = offset;
            unmarshalChildren(dis, (Branch) jMerkle, numberOfChildren);
        } else {
            jMerkle = unmarshalLeaf(dis, hashVal);
        }
        return jMerkle;
    }

    private static JMerkle unmarshalChildren(DataInput in, Branch parent,
                                             int childCount) throws Exception {
        for (short i = 0; i < childCount; i++) {
            // next byte is the key the following unmarshaled
            // child should reside under in the parent's HashMap:
            byte key = in.readByte();
            // next boolean is the type
            boolean childIsBranch = in.readBoolean();
            // next 20 Bytes are the child's hashVal:
            byte[] hashVal = new byte[20];
            in.readFully(hashVal);

            if (childIsBranch) {
                // next int is the inclusive byte size of the branch
                int offset = in.readInt();
                // next short is the total number of children
                // (not to exceed 256):
                short numberOfChildren = in.readShort();
                Branch childBranch = new Branch();
                childBranch.offset = offset;
                childBranch.hashVal = hashVal;
                parent.children.put(key,
                        unmarshalChildren(in, childBranch, numberOfChildren));
            } else {
                parent.children.put(key, unmarshalLeaf(in, hashVal));
            }
        }

        return parent;
    }

    private static JMerkle unmarshalLeaf(DataInput in, byte[] hashVal)
            throws Exception {
        int userKeySize = in.readInt();
        byte[] userKeyBytes = new byte[userKeySize];
        in.readFully(userKeyBytes);
        return new Leaf(userKeyBytes, hashVal);
    }

    public static byte[] marshal(JMerkle jMerkle) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        if (jMerkle != null) {
            // first bit toggles the type:
            boolean isBranch = jMerkle.isBranch();
            dos.writeBoolean(isBranch);

            // next byte[20] is its hashVal
            dos.write(jMerkle.hashVal);

            // marshal respective of the type:
            if (isBranch) {
                marshalChildren(dos, (Branch) jMerkle);
            } else {
                marshalLeaf(dos, (Leaf) jMerkle);
            }
            dos.flush();
        }

        return baos.toByteArray();
    }

    private static void marshalLeaf(DataOutput out, Leaf leaf) throws Exception {
        byte[] userKeyBytes = leaf.userKey;
        out.writeInt(userKeyBytes.length);
        out.write(userKeyBytes);
    }

    private static void marshalChildren(DataOutput out, Branch branch)
            throws Exception {
        // next int is the branch's offset
        out.writeInt(branch.offset());

        TreeMap<Byte, JMerkle> branchChildren = branch.children;
        // next short is the number of children in this branch
        out.writeShort(branchChildren.size());
        for (Entry<Byte, JMerkle> entry : branchChildren.entrySet()) {
            Byte key = entry.getKey();
            JMerkle child = entry.getValue();
            // next byte is its key:
            out.writeByte(key);
            // JMerkle child = branchChildren.get(key);
            boolean childIsBranch = child.isBranch();
            // next boolean is the child's type:
            out.writeBoolean(childIsBranch);
            // next byte[20] is the child's hashVal:
            out.write(child.hashVal);
            // toggle branch/leaf differences:
            if (childIsBranch) {
                marshalChildren(out, (Branch) child);
            } else {
                marshalLeaf(out, (Leaf) child);
            }
        }
    }
}
