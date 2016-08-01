package com.deyvid.spring.dao;

import com.deyvid.spring.model.Node;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class NodeDAOImpl implements NodeDAO {

    private static final Logger logger = LoggerFactory.getLogger(NodeDAOImpl.class);

    @Autowired
    SessionFactory sessionFactory;

    /**
     * Finds all leaves of given node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public List<Node> findAllLeafNodes(Node n) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Node WHERE rgt = lft + 1");
        logger.info("Leaves found successfully" + n);
        if (query.list().size() > 0) return query.list();
        return null;
    }

    /**
     * Creates new node in the tree
     *
     * @return Node
     */
    @Override
    public Node createNode() {
        return new Node();
    }

    /**
     * Updates Node (persist)
     *
     * @return Node
     */
    @Override
    @Transactional
    public void save(Node n) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(n);
        logger.info("Added new node");
    }

    /**
     * Updates Node (persist)
     *
     * @return Node
     */
    @Override
    @Transactional
    public void update(Node n) {
        Session session = sessionFactory.getCurrentSession();
        session.update(n);
    }

    /**
     * Finds root of the tree
     *
     * @return Node
     */
    @Override
    public Node findRoot() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Node WHERE lft = 1");
        return (Node) query.uniqueResult();
    }

    /**
     * Returns all the children of a root
     * SqlQuery =  "FROM Node WHERE lft > :lft and rgt < :rgt and depth = :depth"
     *
     * @return List<Node>
     */
    @Override
    public List<Node> getChildren(Node n) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        Criterion condition = Restrictions.and(
                Restrictions.gt("lft", n.getLft()),
                Restrictions.lt("rgt", n.getRgt()),
                Restrictions.eq("depth", n.getDepth() + 1));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(condition);
        return (List<Node>) criteria.list();
    }

    /**
     * Counts the children
     *
     * @return List<Node>
     */
    @Override
    public Integer countChildren(Node n) {
        return this.getChildren(n).size();
    }

    /**
     * Gets all descendants
     *
     * @return List<Node>
     */
    @Override
    public List<Node> getDescendants(Node n) {
        if (n.isLeaf()) {
            return new ArrayList<Node>();
        }
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        return (List<Node>) this.descendantsOf(criteria, n).list();


    }

    /**
     * Counts all descendants
     *
     * @return Integer
     */
    @Override
    public Integer countDescendants(Node n) {
        return this.getDescendants(n).size();
    }

    /**
     * Gets the first child of the given node
     *
     * @return Node
     */
    @Override
    public Node getFirstChild(Node n) {
        if (n.isLeaf()) {
            return null;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.childrenOf(criteria, n);
            return (Node) criteria.uniqueResult();
        }
    }

    /**
     * Gets the last child
     *
     * @return Node
     */
    @Override
    public Node getLastChild(Node n) {
        if (n.isLeaf()) {
            return null;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.childrenOf(criteria, n);
            criteria.addOrder(Order.desc("lft"));
            return (Node) criteria.uniqueResult();
        }
    }

    /**
     * Gets siblings
     *
     * @return List<Node>
     */
    @Override
    public List<Node> getSiblings(Node n, boolean includeNode) {
        if (n.isRoot()) {
            return null;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.childrenOf(criteria, this.getParent(n));
            if (!includeNode) {
                this.prune(criteria, n);
            }
            return (List<Node>) criteria.list();
        }
    }

    /**
     * Gets descendats of given Node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public Criteria descendantsOf(Criteria c, Node n) {
        Criterion condition = Restrictions.and(
                Restrictions.gt("lft", n.getLft()),
                Restrictions.lt("lft", n.getRgt()));
        return c.add(condition);
    }

    /**
     * Gets children of given Node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public Criteria childrenOf(Criteria c, Node n) {
        return this.descendantsOf(c, n).add(Restrictions.eq("depth", n.getDepth() + 1));
    }

    /**
     * Gets siblings of given Node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public Criteria siblingsOf(Criteria c, Node n) {

        if (n.isRoot()) {
            return c;
        } else {
            this.childrenOf(c, this.getParent(n));
            this.prune(c, n);
            return c;
        }
    }

    /**
     * Gets ancestors of given node
     *
     * @param n
     * @return List<Node>
     */
    public List<Node> getAncestors(Node n) {
        if (n.isRoot()) {
            return new ArrayList<Node>();
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.ancestorsOf(criteria, this.getParent(n));
            this.prune(criteria, n);
            return criteria.list();
        }
    }

    /**
     * Gets ancestors of given node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public Criteria ancestorsOf(Criteria c, Node n) {
        Criterion condition = Restrictions.and(
                Restrictions.lt("lft", n.getLft()),
                Restrictions.gt("rgt", n.getRgt()));
        c.add(condition);
        return c;
    }

    /**
     * Finds node by id
     *
     * @param nodeId
     * @return Node
     */
    @Override
    public Node findOneById(Integer nodeId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("id", nodeId));
        return (Node) criteria.uniqueResult();
    }

    /**
     * Filters node by depth (tree level)
     *
     * @param depth
     * @return List<Node>
     */
    @Override
    public List<Node> filterByDepth(Integer depth) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("depth", depth));
        return (List<Node>) criteria.list();
    }

    /**
     * Deletes given node from a list
     *
     * @param n
     * @return Node
     */
    @Override
    public Criteria prune(Criteria c, Node n) {
        if (n != null) {
            c.add(Restrictions.ne("id", n.getId()));
        }
        return c;
    }

    /**
     * Gets parent of given node
     *
     * @param n
     * @return Node
     */
    @Override
    public Node getParent(Node n) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        this.ancestorsOf(criteria, n).addOrder(Order.asc("rgt"));
        criteria.setMaxResults(1);
        return (Node) criteria.uniqueResult();
    }

    /**
     * Return true if node has previous sibling
     *
     * @param n
     * @return boolean
     */
    @Override
    public Boolean hasPrevSibling(Node n) {
        if (n.isValid()) {
            return false;
        }
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("rgt", n.getRgt() - 1));
        return criteria.list().size() > 0;
    }

    /**
     * Gets previous sibling for the given node if it exists
     *
     * @return Node
     */
    @Override
    public Node getPrevSibling(Node n) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("rgt", n.getLft() - 1));
        criteria.setMaxResults(1);
        return (Node) criteria.uniqueResult();
    }

    /**
     * Determines if the node has next sibling
     *
     * @return boolean
     */
    @Override
    public Boolean hasNextSibling(Node n) {
        if (n.isValid()) {
            return false;
        }
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("lft", n.getLft() + 1));
        return criteria.list().size() > 0;
    }

    /**
     * Gets next sibling for the given node if it exist
     *
     * @return Node
     */
    @Override
    public Node getNextSibling(Node n) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("lft", n.getLft() + 1));
        criteria.setMaxResults(1);
        return (Node) criteria.uniqueResult();
    }


    /**
     * Deletes all descendants for the given node
     * Instance pooling is wiped out by this command,
     * so existing MenuItem instances are probably invalid (except for the current one)
     *
     * @return number of delete nodes
     */
    @Override
    public Integer deleteDescendants(Node n) {
        if (n.isLeaf()) {
            // save one query
            return 0;
        }
        int left = n.getLft();
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        this.descendantsOf(criteria, n);
        List<Node> nodes = criteria.list();
        int index = 0;
        for (Node node : nodes) {
            session.delete(node);
            index++;
        }
        n.setRgt(left + 1);
        this.update(n);
        return index;
    }

    /**
     * Finds whole tree in DB Struture
     *
     * @return
     */
    @Override
    public List<Node> findWholeTree() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Node order by lft asc");
        return query.list();
    }

    /**
     * Updates tree collection to be valid tree after inserting new children
     *
     * @param n
     * @param children
     * @param direction
     */
    @Override
    public void updateChildrenCollection(Node n, boolean children, String direction) {
        int newLft = n.getLft();
        int newRight = n.getRgt();
        Session session = sessionFactory.getCurrentSession();
        Query query;
        Query query2;
        if (children && direction.equals("first")) {
            query = session.createSQLQuery("update Node set rgt = rgt+2 where rgt > :newRight");
            query2 = session.createSQLQuery("update Node set lft = lft+2 where lft >= :newRight");
            query.setParameter("newRight", newRight - 1);
            query2.setParameter("newRight", newRight - 1);
        } else if (children && direction.equals("last")) {
            query = session.createSQLQuery("update Node set rgt = rgt+2 where rgt > :newLft");
            query2 = session.createSQLQuery("update Node set lft = lft+2 where lft > :newLft");
            query.setParameter("newLft", newLft - 1);
            query2.setParameter("newLft", newLft - 1);
        } else {
            query = session.createSQLQuery("update Node set rgt = rgt+2 where rgt >= :newLft");
            query2 = session.createSQLQuery("update Node set lft = lft+2 where lft > :newLft");
            query.setParameter("newLft", newLft - 1);
            query2.setParameter("newLft", newLft - 1);
        }
        query.executeUpdate();
        query2.executeUpdate();

    }

    /**
     * Deletes node. WARNING: If node has children they would be deleted too.
     *
     * @param n
     */
    @Override
    public void deleteNode(Node n) {
        int left = n.getLft();
        int right = n.getRgt();
        int width = right - left + 1;
        Query query;
        Query query2;
        Session session = sessionFactory.getCurrentSession();
        session.delete(n);
        query = session.createSQLQuery("update Node set rgt = rgt - :width where rgt > :rightParam");
        query2 = session.createSQLQuery("update Node set lft = lft - :width where lft >= :rightParam");
        query.setParameter("rightParam", right);
        query2.setParameter("rightParam", right);
        query.setParameter("width", width);
        query2.setParameter("width", width);
        query.executeUpdate();
        query2.executeUpdate();
    }

    /**
     * Moves node to antoher subtree/parent (not working yet)
     *
     * @param node
     * @param newParent
     */
    @Override
    //TODO Make that works
    public void moveToOtherTree(Node node, Node newParent) {
        int width = node.getRgt() - node.getLft() + 1;

        int newpos = newParent.getLft();
        int distance = newpos - node.getLft() + 1;
        int tmpPos = node.getLft();

        if (distance < 0) {
            distance -= width;
            tmpPos += width;
        }

        Session session = sessionFactory.getCurrentSession();
        String hql = "UPDATE Node SET lft = lft + " + width
                + " where lft >= " + newpos;
        Query query = session.createQuery(hql);
        int _return = query.executeUpdate();

        hql = "UPDATE Node SET rgt = rgt + " + width + " where rgt >= "
                + newpos;
        query = session.createQuery(hql);
        _return = query.executeUpdate();

        // Move subtree into new space
        hql = "UPDATE Node SET lft = lft + " + distance
                + ", rgt = rgt + " + distance + " where lft >= " + tmpPos
                + " and rgt < " + (tmpPos + width);
        query = session.createQuery(hql);
        _return = query.executeUpdate();

        // remove old space
        hql = "UPDATE Node SET lft = lft - " + width + " where lft > "
                + node.getRgt();
        query = session.createQuery(hql);
        _return = query.executeUpdate();

        hql = "UPDATE Node SET rgt = rgt - " + width + " where rgt > "
                + node.getRgt();
        query = session.createQuery(hql);
        _return = query.executeUpdate();


        node.setParent(newParent);
        node.setDepth(newParent.getDepth() + 1);
        this.update(node);

    }
}
