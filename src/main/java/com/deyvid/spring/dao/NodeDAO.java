package com.deyvid.spring.dao;

import com.deyvid.spring.model.Node;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDAO {

    public List<Node> findAllLeafNodes(Node p);

    public Node createNode();

    public void save(Node n);

    public void update(Node n);

    public Node findRoot();

    public List<Node> getChildren(Node n);

    public Integer countChildren(Node n);

    public List<Node> getDescendants(Node n);

    public Integer countDescendants(Node n);

    public Node getFirstChild(Node n);

    public Node getLastChild(Node n);

    public List<Node> getSiblings(Node n, boolean includeNode);

    public Criteria descendantsOf(Criteria c, Node n);

    public Criteria childrenOf(Criteria c, Node n);

    public Criteria siblingsOf(Criteria c, Node n);

    public Criteria ancestorsOf(Criteria c, Node n);

    public List<Node> getAncestors(Node n);

    public Node findOneById(Integer nodeId);

    public List<Node> filterByDepth(Integer depth);

    public Criteria prune(Criteria c, Node n);

    public Node getParent(Node n);

    public Boolean hasPrevSibling(Node n);

    public Node getPrevSibling(Node n);

    public Boolean hasNextSibling(Node n);

    public Node getNextSibling(Node n);

    public Integer deleteDescendants(Node n);

    public List<Node> findWholeTree();

    public void updateChildrenCollection(Node n, boolean children, String direction);

    public void deleteNode(Node n);

    public void moveToOtherTree(Node node, Node newParent);
}
