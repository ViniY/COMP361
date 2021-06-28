import javax.xml.stream.events.ProcessingInstruction;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
public class ImprovedGraphWithFractionalGreedy {
        ArrayList<Integer> weights = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> numOfCopies = new ArrayList<>();
        int barometer = 0;
        Node start = new Node();
        int numItems = 0;
        int weightLimits = -1;
        ArrayList<Node> nodes = new ArrayList<>();
        public ImprovedGraphWithFractionalGreedy() {
            Knapsack kn = new Knapsack(10, 5000, 5);
            ArrayList<Integer[]> mydata = kn.items;
            this.weightLimits = kn.weightLimit;
            this.weights.clear();
            this.values.clear();
            this.numOfCopies.clear();
            for (Integer[] x : mydata) {
                weights.add(x[0]);
                values.add(x[1]);
                numOfCopies.add(x[2]);
            }
            this.numItems = this.weights.size();
        }

        public int buildGraph(){

            int[] n0 = new int[this.numItems];
            int[] best_plan = new int[this.numItems];
            Node root = new Node(n0, 0, 0 , this.numItems);
            this.start = root;
            int numTotalItems = 0;
            for ( int i=0; i < this.numItems; i++){
                numTotalItems+= this.numOfCopies.get(i);
            }
            int maximum_value = 0;
            int depth = 0;
            PriorityQueue<Node> currentLevel = new PriorityQueue<>();
            currentLevel.add(root);
            while(depth < numTotalItems){
                PriorityQueue<Node> newLevel = new PriorityQueue<>();
                for (int i=0; i < currentLevel.size(); i++){
//                for (Node n: currentLevel) {
                    Node n = currentLevel.poll();
                    int[] itemsNow = n.items.clone();

                    for (int index = 0; index < numItems; index++){
                        this.barometer++;
                        int[] newNode = new int[numItems];
                        int weightNow = 0;
                        int valueNow = 0;
                        if ((itemsNow[index]+1) <= numOfCopies.get(index)){
                            newNode = itemsNow.clone();
                            newNode[index] = (newNode[index])+1;
                            weightNow = n.weightSoFar + weights.get(index);
                            valueNow = n.valueSoFar + values.get(index);
                            if (weightNow <= weightLimits) {
                                Node nnNode = new Node(newNode, valueNow, weightNow, numItems);
                                nnNode.weightSoFar = weightNow;
                                nnNode.valueSoFar = valueNow;
                                newLevel.add(nnNode);
                                n.children.add(nnNode);
                                if (valueNow >= maximum_value) {
                                    maximum_value = valueNow;
                                    if (depth == numTotalItems || weightNow == weightLimits){
                                        return maximum_value;
                                    }
                                }
                            }

                        }
                    }
                }

                currentLevel = newLevel;
                depth++;
            }
            System.out.println(" The maximum value is : " + maximum_value);
            System.out.println(" The barometer is " + this.barometer);
            return maximum_value;

        }




        public class Node implements Comparable<Node> {
            Node parent;
            ArrayList<Node> children = new ArrayList<>();
            int[] items;
            int weightSoFar = 0;
            int valueSoFar = 0;
            public Node(){}
            public Node(int[] items, int value, int weight, int numOfItems) {
                this.items = new int[numOfItems];
                this.weightSoFar = weight;
                this.valueSoFar = value;
            }

            @Override
            public int compareTo(Node node) {
                return (this.valueSoFar/this.weightSoFar) - (node.valueSoFar/node.weightSoFar);
            }
        }

        public static void main(String[] args) {
            ImprovedGraphWithFractionalGreedy knGS = new ImprovedGraphWithFractionalGreedy();
            int numItems = knGS.weights.size(); // The number of items we have maximum edges we can have
            knGS.buildGraph();

        }

}


