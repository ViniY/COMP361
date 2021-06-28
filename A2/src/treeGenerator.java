import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Random;
public class treeGenerator {
    int maxDepth;
    int maxNumChildren = 2;
    public treeGenerator(int levels, int maxNumChildren){
        this.maxDepth = levels;
        this.maxNumChildren  = maxNumChildren;
    }

    public ArrayList<Node> treeGenerator(){
        Random random = new Random();
        ArrayList<Integer> info = new ArrayList<>();
        ArrayList<Node> tree = new ArrayList<>();
        info.add(1);
        for (int i=1; i <this.maxDepth; i++){
            int cNum = random.nextInt(this.maxNumChildren+1);
            info.add(cNum);
        }
        for (int i=this.maxDepth-1; i >0 ;i--){

        }

        return tree ;
    }
    public static class Node{
        int id;
        String name;
        Node parent;
        ArrayList<Node> children = new ArrayList<>();
        boolean visited = false;
        boolean root = false;
        int weight=0;
        int cost= 0;
        public Node(){
            this.children = new ArrayList<>();
            this.parent = null;
            root = true;
        }

        public Node(Node parent){
            if(parent==null) this.root = true;
            else this.parent = parent;
//            this.children = children;
        }
        //getter and setters

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public ArrayList<Node> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<Node> children) {
            this.children = children;
        }

        public boolean isRoot() {
            return root;
        }

        public void setRoot(boolean root) {
            this.root = root;
        }
    }
}
