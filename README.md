# Nested set - Nodes
@dwiniarz


## Start
Project simply implements nested set hierarchy with its necessary methods.
It's written in Java with Spring/Hibernate/JSP.


## Basic config:
Project currently works on HSQL.
 -   Database name: XDB
 -   USER: SA
 -   PASSWORD:
 -   DIALECT: org.hibernate.dialect.HSQLDialect


Each node has name and value. Moreover, before first launch root will be created.

## Available funcionality:
- creating new childs to given node (as first or last child)
- simple CRUD functions with deleting nodes and editing its value and name
- moving nodes to others subtrees (not implemented yet)
    
## Requirements
Project war tested on Tomcat 7.0.56 with "/" context.
It's prepared to be used on Mysql oraz HSQL, but you of course can load your own libraries.

## Maven
Project is fully mavanized.

## Test
In folder test you can find a lot of methods which confirm correctness of the nestes set algorithm. See test plan.doc

## Assets
They are builtin. Query, bootstrap just to look and act nice.

## DB SCRIPT (just to begin)
        SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
        SET time_zone = "+00:00";


        /*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
        /*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
        /*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
        /*!40101 SET NAMES utf8 */;

        --
        -- Baza danych: `nodes`
        --

        -- --------------------------------------------------------

        --
        -- Struktura tabeli dla tabeli `node`
        --

        CREATE TABLE IF NOT EXISTS `node` (
        `id` int(11) NOT NULL,
          `depth` int(11) NOT NULL,
          `lft` int(11) NOT NULL,
          `name` varchar(255) NOT NULL,
          `rgt` int(11) NOT NULL,
          `value` int(11) NOT NULL,
          `parent_id` int(11) DEFAULT NULL
        ) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

        --
        -- Zrzut danych tabeli `node`
        --

        INSERT INTO `node` (`id`, `depth`, `lft`, `name`, `rgt`, `value`, `parent_id`) VALUES
        (1, 0, 1, 'root', 2, 0, NULL);

        --
        -- Indeksy dla zrzutów tabel
        --

        --
        -- Indexes for table `node`
        --
        ALTER TABLE `node`
         ADD PRIMARY KEY (`id`), ADD KEY `FK_ujoy3qeqhyby34ybbxvhd13e` (`parent_id`);

        --
        -- AUTO_INCREMENT for dumped tables
        --

        --
        -- AUTO_INCREMENT dla tabeli `node`
        --
        ALTER TABLE `node`
        MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=15;
        --
        -- Ograniczenia dla zrzutów tabel
        --

        --
        -- Ograniczenia dla tabeli `node`
        --
        ALTER TABLE `node`
        ADD CONSTRAINT `FK_ujoy3qeqhyby34ybbxvhd13e` FOREIGN KEY (`parent_id`) REFERENCES `node` (`id`);

        /*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
        /*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
        /*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


## Spring configuration
Its configurations is based on XML files, which just got me better.

## Api
- Boolean isInTree
- Boolean isRoot
- Boolean isLeaf
- Node makeRoot
- Integer sumValue
- Boolean isDescendantOf(Node parent)
- Boolean inAncestorOf(Node child)
- Boolean hasParent
- Boolean hasChildren
- Boolean isValid
- Node insertAsFirstChildOf(Node parent)
- Node insertAsLastChildOf(Node parent) 

Service methods: 
- Node createNode();
- void update(Node n);
- void save(Node n);
- Node fillWithExampleData(Node n);
- Node findRoot();
- List<Node> getChildren(Node n);
- Integer countChildren(Node n);
- List<Node> getDescendants(Node n);
- Integer countDescendants(Node n);
- Node getFirstChild(Node n);
- Node getLastChild(Node n);
- List<Node> getSiblings(Node n);
- Node findOneById(Integer nodeId);
- List<Node> filterByDepth(Integer depth);
- Node getParent(Node n);
- void setParent(Node n);
- Integer deleteDescendants(Node n);
- List<Node> findWholeTree() throws Exception;
- void updateTreeCollection(Node n, boolean children, String direction);
- void updateTree(Node n, int newPos);
- Node addChildAsFirst(Node parent, String name, int value) throws Exception;
- Node addChildAsLast(Node parent, String name, int value) throws Exception;
- int getSumOfLeaf(Node leaf) throws Exception;
- void deleteNode(Node n);



## Docs refs
Here you can find pages I was using to build this implemention.
- http://ops-welt.blogspot.com/2009/03/nested-set-mit-hibernate-und-spring.html
- http://www.devme.it/more-more-and-more/nested-set-java-spring-postgresql-plpgsql-yahoo-framework/
- http://mikehillyer.com/articles/managing-hierarchical-data-in-mysql/
- http://members.chello.at/fritz.ritzberger/downloads/jpatreedao/JpaTreeDao.html
- http://www.waitingforcode.com/mysql/managing-hierarchical-data-in-mysql-nested-set/read
- https://github.com/romanb/JPA-NestedSet/blob/master/src/main/java/org/code_factory/jpa/nestedset/JpaNode.java
- http://we-rc.com/blog/2015/07/19/nested-set-model-practical-examples-part-i