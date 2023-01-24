package MinesGenerator;
//CreateTime: 2023-01-16 10:33 a.m.

import GlobalParameters.Constant;
import Util.Coordinate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class MinesGenerator {

    /**
     * constructor of MinesGenerator
     * @param matrix : the 2D matrix to be filled in, char meaning : 'M': mine, '0' : empty, '1' to '8' : how many mines around this node
     * @param clickedY : the y value of user's first click (To ensure that the user's first click does not click a mine, when generating mines, we need to avoid the cell)
     * @param clickedX : the x value of user's first click (To ensure that the user's first click does not click a mine, when generating mines, we need to avoid the cell)
     */
    public MinesGenerator(char[][] matrix, int clickedY, int clickedX){
        this.matrix = matrix;
        this.FIRSTCLICK = new Coordinate(clickedY, clickedX);
        this.ROW = matrix.length;
        this.COLUMN = matrix[0].length;
    }

    //globalize the matrix
    private final char[][] matrix;
    //globalize the user's first click coordinate
    private final Coordinate FIRSTCLICK;

    //how many rows the matrix has?
    private final int ROW;
    //how many columns the matrix has?
    private final int COLUMN;

    /**
     * connect all nodes together in order to ensure there is not a solitary area is surrounded by mines
     */
    private class TreeOfNodesInMatrix{

        //matrix that records nodes and their parent node and children nodes
        private Node[][] nodesMatrix;

        /**
         * a node in the tree of matrix
         */
        private class Node{



            //code : 'M': mine, '0' : empty
            public char code;

            //self cell
            private final Coordinate self;
            //parent
            private Node parent;
            //children list
            private ArrayList<Node> children;

            //constructor
            public Node(int y, int x, Node parentNode){
                this.self = new Coordinate(y, x);
                this.parent = parentNode;
                this.code = Constant.EMPTY;
                this.children = new ArrayList<>();
            }

            //getter and setter of attributes
            public void setParent(Node parent) {
                this.parent = parent;
            }

            public void addAChild(Node child){
                children.add(child);
            }

            public void deleteLastChild(){
                children.remove(children.size() -1);
            }

            public int getY(){
                return this.self.y();
            }

            public int getX(){
                return this.self.x();
            }
            public Coordinate getSelf() {
                return self;
            }

            public Node getParent() {
                return parent;
            }

            public ArrayList<Node> getChildren() {
                return children;
            }

            /**
             * connect with a surrounding node and consider it as the parent node of this node, the parent node can't be the children node of this node
             * @return : whether connect with parent node successfully or not
             */
            public boolean ConnectWithParentSuccessfully(){

                //iterate all possible coordinate till find a parent node
                for (int[] possibleCoordinate : Constant.SURROUNDINGCOORDINATES){
                    // y and x value of the possible coordinate
                    int newY = self.y() + possibleCoordinate[0], newX = self.x() + possibleCoordinate[1];
                    //if x and y value out of boundary or there is no such node, or there is a mine,  or it's a child node of this node, skip it
                    if (newY < 0 || newY >= ROW || newX < 0 || newX >= COLUMN || nodesMatrix[newY][newX].code == Constant.MINE || nodesMatrix[newY][newX].parent.equals(this)) continue;
                    //set the eligible node as the parent node of the node
                    this.parent = nodesMatrix[newY][newX];
                    //add this node into parent children list
                    this.parent.addAChild(this);
                    return true;
                }return false;
            }


            /**
             * if set this node to mine successfully
             * @return : successfully change this node to mine or not
             */
            public boolean setToMineSuccessfully(TreeOfNodesInMatrix outerInstance, int expectedNum){
                //set to mine
                this.code = Constant.MINE;
                //remove this node from its parent children list
                ArrayList<Node> parentList = this.parent.children;
                parentList.set(parentList.indexOf(this), parentList.get(parentList.size() - 1));
                parentList.remove(parentList.size() - 1);
                //mine can't be a parent node, change the parent node of its children node
                for (int i = 0; i < children.size(); i++){
                    //if a node can't find a new parent node, this node can't be a mine
                    if (!children.get(i).ConnectWithParentSuccessfully()){
                        //recover changed children nodes
                        for (int j = 0; j < i; j++){
                            Node child = children.get(j);
                            child.parent.deleteLastChild();
                            child.setParent(this);
                        }//set back into an empty cell and add itself into parent children list
                        this.code = Constant.EMPTY;
                        this.parent.addAChild(this);
                        return false;
                    }
                }
                if (outerInstance.getNumOfAccessibleNodes() != expectedNum){
                    //recover changed children nodes
                    for (Node child : children) {
                        child.parent.deleteLastChild();
                        child.setParent(this);
                    }//set back into an empty cell and add itself into parent children list
                    this.code = Constant.EMPTY;
                    this.parent.addAChild(this);
                    return false;
                }else{
                    this.parent = null;
                    this.children = null;
                    return true;
                }

            }
        }

        //the head of the tree
        Node root;

        public TreeOfNodesInMatrix(){
            //initialize the nodesMatrix
            nodesMatrix = new Node[ROW][COLUMN];
            nodesMatrix[FIRSTCLICK.y()][FIRSTCLICK.x()] = new Node(FIRSTCLICK.y(), FIRSTCLICK.x(), null);

            //initialize the root node
            root = nodesMatrix[FIRSTCLICK.y()][FIRSTCLICK.x()];

            //using DFS to connect all nodes
            ArrayDeque<Node> deque = new ArrayDeque<>();
            deque.add(root);
            while (!deque.isEmpty()){
                int len = deque.size();
                for (int i = 0; i < len; i++){
                    Node currentNode = deque.pollFirst();
                    Coordinate self = currentNode.getSelf();
                    //iterate all possible coordinate till find a parent node
                    for (int[] possibleCoordinate : Constant.SURROUNDINGCOORDINATES){
                        // y and x value of the possible coordinate
                        int newY = self.y() + possibleCoordinate[0], newX = self.x() + possibleCoordinate[1];
                        //if x and y value out of boundary or there is already has a node, skip
                        if (newY < 0 || newY >= ROW || newX < 0 || newX >= COLUMN || nodesMatrix[newY][newX] != null) continue;
                        //fill in a node
                        Node newNode = new Node(newY, newX, nodesMatrix[currentNode.getY()][currentNode.getX()]);
                        nodesMatrix[newY][newX] = newNode;

                        //add into parent node's children list
                        currentNode.addAChild(newNode);

                        //add into the deque
                        deque.addLast(newNode);
                    }
                }
            }
        }

        /**
         * How many nodes are accessible in the tree
         * @return : number of accessible nodes
         */
        public int getNumOfAccessibleNodes(){
            return helper(root);
        }

        /**
         * helper of getNumOfAccessibleNodes
         * @param root : root node
         * @return : number of accessible nodes
         */
        private int helper(Node root){
            int ans = 1;
            if (root.getChildren().size() > 0){
                for (Node child : root.getChildren()){
                    ans += helper(child);
                }
            }
            return ans;
        }

        public Node getANodeInMatrix(int y, int x){
            return nodesMatrix[y][x];
        }
    }

    //8 * 8 with 10 mines
    // 16 * 16 with 40 mines
    // 30 * 16 with 99 mines
    /**
     * generate a given number mines in the matrix
     * @param num : number of mines to be filled in
     */
    public void generateMines(int num){
        //how many cell in the matrix
        int sum = ROW * COLUMN;
        //initialize a tree
        TreeOfNodesInMatrix tree = new TreeOfNodesInMatrix();
        //use a ArrayList to store all nodes that can be a mine
        ArrayList<TreeOfNodesInMatrix.Node> nodesThatCanBeAMine = new ArrayList<>();
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COLUMN; j++){
                TreeOfNodesInMatrix.Node node = tree.getANodeInMatrix(i, j);
                //root node (first click) need to be empty (neither mine nor number 1 to 8)
                if (Math.abs(node.getY() - tree.root.getY()) < 2 && Math.abs(node.getX() - tree.root.getX()) < 2) continue;
                nodesThatCanBeAMine.add(node);
            }
        }
        //new a random object to select a random node to become a mine
        Random random = new Random();
        //fill in mines
        int currentNumOfMines = 0;
        while (currentNumOfMines < num && nodesThatCanBeAMine.size() > 0){
            int ran = random.nextInt(0, nodesThatCanBeAMine.size());
            //if set the node into mine successfully
            if (nodesThatCanBeAMine.get(ran).setToMineSuccessfully(tree, sum - currentNumOfMines - 1)){
                //number of mines + 1
                currentNumOfMines ++;
                //clear the node from nodesThatCanBeMine list
                nodesThatCanBeAMine.set(ran, nodesThatCanBeAMine.get(nodesThatCanBeAMine.size() -1));
                nodesThatCanBeAMine.remove(nodesThatCanBeAMine.size() - 1);
            }
        }

        //after filled in all mines, convert the nodes matrix in tree into a char matrix
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COLUMN; j++){
                char code = tree.getANodeInMatrix(i, j).code;
                //if current node is a mine, surrounding node need to add 1 to indicate that's a mine around itself
                if (code == Constant.MINE){
                    for (int[] possibleCoordinate : Constant.SURROUNDINGCOORDINATES){
                        int newY = i + possibleCoordinate[0], newX = j + possibleCoordinate[1];
                        //if x and y value out of boundary or the node
                        if (newY < 0 || newY >= ROW || newX < 0 || newX >= COLUMN || tree.getANodeInMatrix(newY, newX).code == Constant.MINE) continue;
                        matrix[newY][newX] ++;

                    }matrix[i][j] = Constant.MINE;
                }else{
                    matrix[i][j] += Constant.EMPTY;
                }
            }
        }
        //clear
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[i].length; j++) {
//                if (i == FIRSTCLICK.y() && j == FIRSTCLICK.x()) System.out.printf("\u001B[34m%c\u001B[0m ", matrix[i][j]);
//                else if (matrix[i][j] == Constant.MINE) System.out.print("\u001B[31mM\u001B[0m ");
//                else System.out.print(matrix[i][j] + " ");
//            }
//            System.out.println();
//        }
    }

    //clear
//    public static void main(String[] args){
//        Random random = new Random();
//        new MinesGenerator(new char[30][16], random.nextInt(30), random.nextInt(16)).generateMines(99);
//    }


}
