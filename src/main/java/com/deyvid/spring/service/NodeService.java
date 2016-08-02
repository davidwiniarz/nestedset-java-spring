package com.deyvid.spring.service;

import com.deyvid.spring.model.Node;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NodeService {

    public Node createNode();

    public void update(Node n);

    public void save(Node n);

    public Node fillWithExampleData(Node n);

    public Node findRoot();

    public List<Node> getChildren(Node n);

    public Integer countChildren(Node n);

    public List<Node> getDescendants(Node n);

    public Integer countDescendants(Node n);

    public Node getFirstChild(Node n);

    public Node getLastChild(Node n);

    public List<Node> getSiblings(Node n);

    public Node findOneById(Integer nodeId);

    public List<Node> filterByDepth(Integer depth);

    public Node getParent(Node n);

    public void setParent(Node n);

    public Integer deleteDescendants(Node n);

    public List<Node> findWholeTree() throws Exception;

    public void updateTreeCollection(Node n, boolean children, String direction);

    public Node addChildAsFirst(Node parent, String name, int value) throws Exception;

    public Node addChildAsLast(Node parent, String name, int value) throws Exception;

    public int getSumOfLeaf(Node leaf) throws Exception;

    public void deleteNode(Node n);

    public void moveToOtherTree(Node node, Node newParent);

    public Node updateFileds(Node currentNode, Node node);
}
