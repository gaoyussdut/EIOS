package top.toptimus.formula.util.datatree;

public class DataTreeNode<object> {
    public DataTreeNode<object> brother = null;
    public DataTreeNode<object> child = null;
    protected object content;

    public DataTreeNode(object _content) {
        content = _content;
    }

    public String toString() {
        return content.toString();
    }

    public object getContent() {
        return content;
    }
}
