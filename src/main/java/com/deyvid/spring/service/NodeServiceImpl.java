package com.deyvid.spring.service;

import com.deyvid.spring.dao.NodeDAO;
import com.deyvid.spring.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeDAO dao;

    /**
     * Creates new node in the tree
     *
     * @return Node
     */
    @Override
    @Transactional
    public Node createNode() {
        return new Node();
    }

    @Override
    @Transactional
    public Node fillWithExampleData(Node n) {
        n.setName("child");
        n.setValue(50);
        return n;
    }

    /**
     * Updates Node (persist)
     *
     * @return Node
     */
    @Override
    @Transactional
    public void update(Node n) {
        dao.update(n);
    }

    /**
     * Saves Node (persist)
     *
     * @return Node
     */
    @Override
    @Transactional
    public void save(Node n) {
        dao.save(n);
    }

    /**
     * Finds root of the tree
     *
     * @return Node
     */
    @Override
    @Transactional
    public Node findRoot() {
        return dao.findRoot();
    }

    /**
     * Returns all the children of a root
     *
     * @return List<Node>
     */
    @Override
    @Transactional
    public List<Node> getChildren(Node n) {
        return dao.getChildren(n);
    }

    /**
     * Counts the children
     *
     * @return List<Node>
     */
    @Override
    @Transactional
    public Integer countChildren(Node n) {
        return dao.countChildren(n);
    }

    /**
     * Gets all descendants
     *
     * @return List<Node>
     */
    @Override
    @Transactional
    public List<Node> getDescendants(Node n) {
        return dao.getDescendants(n);
    }

    /**
     * Counts all descendants
     *
     * @return Integer
     */
    @Override
    @Transactional
    public Integer countDescendants(Node n) {
        return dao.countDescendants(n);
    }

    /**
     * Gets the first child of the given node
     *
     * @return Node
     */
    @Override
    @Transactional
    public Node getFirstChild(Node n) {

        return dao.getFirstChild(n);
    }

    /**
     * Gets the last child
     *
     * @return Node
     */
    @Override
    @Transactional
    public Node getLastChild(Node n) {

        return dao.getLastChild(n);
    }

    /**
     * Gets siblings
     *
     * @return List<Node>
     */
    @Override
    @Transactional
    public List<Node> getSiblings(Node n) {
        return dao.getSiblings(n, false);
    }

    /**
     * Finds node by id
     *
     * @param nodeId
     * @return Node
     */
    @Override
    @Transactional
    public Node findOneById(Integer nodeId) {
        return dao.findOneById(nodeId);
    }

    /**
     * Filters node by depth (tree level)
     *
     * @param depth
     * @return List<Node>
     */
    @Override
    @Transactional
    public List<Node> filterByDepth(Integer depth) {
        return dao.filterByDepth(depth);
    }

    /**
     * Gets parent of given node (DB way)
     *
     * @param n
     * @return Node
     */
    @Override
    @Transactional
    public Node getParent(Node n) {
        return dao.getParent(n);
    }

    @Override
    @Transactional
    public void setParent(Node n) {
        n.setParent(this.getParent(n));
    }

    /**
     * Deletes all descendants for the given node
     * Instance pooling is wiped out by this command,
     * so existing MenuItem instances are probably invalid (except for the current one)
     *
     * @return number of delete nodes
     */
    @Override
    @Transactional
    public Integer deleteDescendants(Node n) {
        return dao.deleteDescendants(n);
    }


    /**
     * Find the whole structure of tree
     *
     * @return List<Node>
     * @throws Exception
     */
    @Override
    @Transactional
    public List<Node> findWholeTree() throws Exception {
        List<Node> nodes = dao.findWholeTree();
        if (nodes.size() == 0) {
            Node newRoot = this.createNode();
            newRoot.makeRoot();
            dao.save(newRoot);
            nodes.add(newRoot);
        }
        return nodes;
    }

    /**
     * Updates tree collection to be valid tree after inserting new children
     *
     * @param n
     * @param children
     * @param direction
     */
    @Override
    @Transactional
    public void updateTreeCollection(Node n, boolean children, String direction) {
        dao.updateChildrenCollection(n, children, direction);
    }


    /**
     * * Add child to given parent as first in order
     *
     * @param parent
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Node addChildAsFirst(Node parent, String name, int value) throws Exception {
        Node newNode = this.createAndFillWithData(value, name);
        newNode = newNode.insertAsFirstChildOf(parent);
        if (this.getChildren(parent).size() > 0) {
            this.updateTreeCollection(newNode, true, "first");
        } else {
            this.updateTreeCollection(newNode, false, "none");
        }
        this.save(newNode);
        return newNode;
    }

    /**
     * Add child to given parent as last in order
     *
     * @param parent
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Node addChildAsLast(Node parent, String name, int value) throws Exception {
        Node newNode = this.createAndFillWithData(value, name);
        newNode = newNode.insertAsLastChildOf(parent);
        if (this.getChildren(parent).size() > 0) {
            this.updateTreeCollection(newNode, true, "last");
        } else {
            this.updateTreeCollection(newNode, false, "none");
        }
        this.save(newNode);
        return newNode;
    }

    /**
     * Gets sum of given leaf and its descendants
     *
     * @param node
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public int getSumOfLeaf(Node node) throws Exception {
        if (node.isLeaf()) {
            List<Node> nodes = this.getDescendants(node);
            int sum = 0;
            for (Node descendant : nodes) {
                sum += descendant.getValue();
            }
            sum += node.getValue();
            return sum;
        }
        throw new Exception("Chosen node is not a leaf");
    }

    /**
     * Deletes node. WARNING: If node has children they would be deleted too.
     *
     * @param n
     */
    @Override
    @Transactional
    public void deleteNode(Node n) {
        dao.deleteNode(n);
    }

    /**
     * Fill with data given node
     *
     * @param value
     * @param name
     * @return
     */
    private Node createAndFillWithData(int value, String name) {
        Node n = this.createNode();
        n.setValue(value);
        n.setName(name);
        return n;
    }

    /**
     * Moves node to another subtree/parent (not working yet)
     *
     * @param node
     * @param newParent
     */
    @Override
    @Transactional
    //TODO Make that works
    public void moveToOtherTree(Node node, Node newParent) {
        dao.moveToOtherTree(node, newParent);
    }

    @Override
    @Transactional
    public Node updateFields(Node currentNode, Node node) {
        currentNode.setValue(node.getValue());
        currentNode.setName(node.getName());
        return currentNode;
    }

}
