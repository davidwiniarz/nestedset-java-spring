package com.deyvid.spring.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Node")
public class Node {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lft", nullable = false)
    private int lft;

    @Column(name = "rgt", nullable = false)
    private int rgt;

    @Column(name = "depth", nullable = false)
    private int depth;

    @Column(name = "value", nullable = false)
    private int value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Node parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Node> children = new ArrayList<Node>();


    public List<Node> getChildren() {
        return this.children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLft() {
        return lft;
    }

    public void setLft(int lft) {
        this.lft = lft;
    }

    public int getRgt() {
        return rgt;
    }

    public void setRgt(int rgt) {
        this.rgt = rgt;
    }


    public Boolean isInTree() {
        return this.getLft() > 0 && this.getRgt() > this.getLft();
    }

    public Boolean isRoot() {
        return this.isInTree() && this.getLft() == 1;
    }

    public Boolean isLeaf() {
        return this.isInTree() && (this.getRgt() - this.getLft()) == 1;
    }

    public Node makeRoot() throws Exception {
        if (this.getLft() > 0 || this.getRgt() > 0) {
            throw new Exception("Cannot ");
        }
        this.setName("root");
        this.setLft(1);
        this.setRgt(2);
        this.setDepth(0);
        this.setValue(0);
        return this;
    }

    /**
     * Returns sum of values, and pass it to leaf
     *
     * @return
     */
    public Integer sumValue() {
        if (this.isLeaf()) {
            int depth = this.getDepth();
            int sum = this.getValue();
            Node n = this;
            for (int i = 0; depth > i; i++) {
                sum += n.getParent().getValue();
                n = n.getParent();
            }
            return sum;
        }
        return 0;
    }

    public Boolean isDescendantOf(Node parent) {
        return this.isInTree() && this.getLft() > parent.getLft() && this.getRgt() < parent.getRgt();
    }

    public Boolean inAncestorOf(Node child) {
        return child.isDescendantOf(this);
    }

    public Boolean hasParent() {
        return this.getDepth() > 0;
    }

    public Boolean hasChildren() {
        return (this.getRgt() - this.getLft()) > 1;
    }

    public Boolean isValid() {
        return this.getRgt() > this.getLft();
    }

    /**
     * Inserts the current node as first child of given parent node
     * The modifications in the current object and the tree
     * are not persisted until the current object is saved.
     *
     * @param parent
     * @return Node
     */
    public Node insertAsFirstChildOf(Node parent) throws Exception {
        if (this.isInTree()) throw new Exception("Node already in tree");
        int left = parent.getLft() + 1;
        this.setLft(left);
        this.setRgt(left + 1);
        this.setDepth(parent.getDepth() + 1);
        this.setParent(parent);

        List<Node> nodes = new ArrayList<Node>();
        nodes.add(this);
        parent.setChildren(nodes);
        return this;
    }


    /**
     * Inserts the current node as last child of given parent node
     * The modifications in the current object and the tree
     * are not persisted until the current object is saved.
     *
     * @param parent
     * @return Node
     */
    public Node insertAsLastChildOf(Node parent) throws Exception {
        if (this.isInTree()) throw new Exception("Node already in tree");
        int left = parent.getRgt();
        this.setLft(left);
        this.setRgt(left + 1);
        this.setDepth(parent.getDepth() + 1);
        this.setParent(parent);

        List<Node> nodes = new ArrayList<Node>();
        nodes.add(this);
        parent.setChildren(nodes);
        return this;
    }

}
