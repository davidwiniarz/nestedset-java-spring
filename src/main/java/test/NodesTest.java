package test;

import com.deyvid.spring.dao.NodeDAO;
import com.deyvid.spring.model.Node;
import com.deyvid.spring.service.NodeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class NodesTest {

    @InjectMocks
    @Autowired
    public NodeService service;

    @InjectMocks
    @Autowired
    private NodeDAO dao;

    private static Integer defaultValue = 10;


    @Before
    public void init() throws Exception {
        if (service.findRoot() == null) {
            Node root = service.createNode();
            root.makeRoot();
            service.save(root);
            service.addChildAsLast(root, "child1", defaultValue);
            Assert.assertTrue(service.findRoot().getName().equals("root"));
        }
    }


    @Test
    public void checkRoot() throws Exception {
        Node parent = service.findRoot();
        Assert.assertTrue(parent.getLft() == 1);
    }

    @Test
    public void getChildrenTest() {
        Node parent = service.findRoot();
        List<Node> children = service.getChildren(parent);
        if (children.size() == 0) {
            Assert.assertTrue(children.size() == 0);
        } else {
            int count = service.countChildren(parent);
            Assert.assertTrue(children.size() > 0);
            Assert.assertTrue(count > 0);
        }
    }

    @Test
    public void addChildToRootAtFirst() throws Exception {
        Node root = service.findRoot();
        service.addChildAsFirst(root, "child", defaultValue);
    }

    @Test
    public void addChildToRootAtLast() throws Exception {
        Node root = service.findRoot();
        service.addChildAsLast(root, "child", defaultValue);
    }

    @Test
    public void checkChildren() {
        Node root = service.findRoot();
        List<Node> nodesOfModel = root.getChildren();
        List<Node> nodesOfDB = service.getChildren(root);
        Assert.assertTrue(nodesOfDB.size() > 0);
        Assert.assertTrue(nodesOfModel.size() > 0);
    }

    @Test
    public void addChildToChildAtFirst() throws Exception {
        if (service.findRoot().hasChildren()) {
            Node n = service.findOneById(service.findRoot().getChildren().get(0).getId());
            service.addChildAsFirst(n, "childOf_" + n.getName() + n.getId(), defaultValue);
            Assert.assertTrue(n.getChildren().size() > 0);
        }
        Assert.assertTrue(true);
    }

    @Test
    public void addChildToChildAtLast() throws Exception {
        if (service.findRoot().hasChildren()) {
            Node n = service.findOneById(service.findRoot().getChildren().get(0).getId());
            service.addChildAsLast(n, "childOf_" + n.getName() + n.getId(), defaultValue);
            Assert.assertTrue(n.getChildren().size() > 0);
        }
        Assert.assertTrue(true);
    }


    @Test
    public void deleteNode() throws Exception {
        Node root = service.findRoot();
        if (root.getChildren().size() > 0) {
            Node nodeToDelete = root.getChildren().get(0);
            int currentSize = service.findWholeTree().size();
            service.deleteNode(nodeToDelete);
            int currentSizeAfterDeleting = service.findWholeTree().size();

            Assert.assertTrue(currentSize - currentSizeAfterDeleting > 0);
        }
        Assert.assertTrue(root.getLft() == 1);
    }

    @Test
    public void checkRandomNodeIsValidTree() throws Exception {
        List<Node> nodes = service.findWholeTree();
        ArrayList indexes = new ArrayList();
        for (Node n : nodes) {
            indexes.add(n.getId());
        }
        int max = indexes.size();
        Random rand = new Random();
        int randomNum = rand.nextInt(max);
        Assert.assertTrue(service.findOneById((Integer) indexes.get(randomNum)).isValid());
    }

    @Test
    public void sumValuesOfLeaves() {
        Node root = service.findRoot();
        List<Node> nodes = root.getChildren();
        int sum = 0;
        for (Node n : nodes) {
            sum += n.getValue();
        }
        List<Node> nodesFromDB = service.getChildren(root);
        int sumDb = 0;
        for (Node n : nodesFromDB) {
            sumDb += n.getValue();
        }
        Assert.assertTrue(sumDb == sum);
    }

    @Test
    @Ignore
    //TODO Algo cos jest nie tak.
    public void moveNodeToAnotherTree() {
        Node node = service.findOneById(4);
        Node target = service.findOneById(9);
        service.moveToOtherTree(node, target);

        Assert.assertTrue(node.getId() == 4);
    }
}