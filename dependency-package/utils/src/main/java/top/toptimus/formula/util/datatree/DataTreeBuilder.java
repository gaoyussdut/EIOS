package top.toptimus.formula.util.datatree;

public interface DataTreeBuilder<object> {
    public Object rootFound(object root);

    public Object nodeFound(Object cookies, object content);
}
