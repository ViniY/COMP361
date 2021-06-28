import java.lang.reflect.Array;
import java.util.ArrayList;
import java.math.*;
import java.util.Arrays;
import java.util.Comparator;

public class fastestBroadcast
{
    int barometer=0;
    int numberOfRounds = -1;//number of rounds need
    ArrayList<treeGenerator.Node> seq = new ArrayList<>(); //result
    ArrayList<treeGenerator.Node> tree = new ArrayList<>();
    public fastestBroadcast(int maxDepth,int maxNumChildren){
        treeGenerator tree = new treeGenerator(maxDepth,maxNumChildren);
        this.tree = tree.treeGenerator();
    }
//    public fastestBroadcast(){//manual inserted data basic case
//        int counter=0;
//       ArrayList<treeGenerator.Node> nodes = new ArrayList<>();
//        treeGenerator.Node root = new treeGenerator.Node();
//        root.name= "A";
//        root.id = 0;
//        counter++;
//        treeGenerator.Node b = new treeGenerator.Node(root);
//        b.name = "B";
//        b.id = counter;
//        counter++;
//        b.children = new ArrayList<treeGenerator.Node>();
//        treeGenerator.Node c = new treeGenerator.Node(b);
//        c.name = "C";
//        c.id=counter++;
//
//        treeGenerator.Node d = new treeGenerator.Node(root);
//        d.name = "D";
//        d.id =counter;
//        b.children.add(c);
//        root.children.add(b);
//        root.children.add(d);
//        nodes.add(root);
//        nodes.add(b);
//        nodes.add(c);
//        nodes.add(d);
//        this.tree = nodes;
//
//    }


    public fastestBroadcast(){//manual inserted data hard case
        int counter=0;
        ArrayList<treeGenerator.Node> nodes = new ArrayList<>();
        treeGenerator.Node root = new treeGenerator.Node();
        root.name= "A";
        root.id = 0;
        counter++;
        treeGenerator.Node b = new treeGenerator.Node(root);
        b.name = "B";
        b.id = counter;
        counter++;
        b.children = new ArrayList<treeGenerator.Node>();
        treeGenerator.Node c = new treeGenerator.Node(root);
        c.name = "C";
        c.id=counter++;

        treeGenerator.Node d = new treeGenerator.Node(b);
        d.name = "D";
        d.id =counter++;

        treeGenerator.Node e = new treeGenerator.Node(b);
        e.name = "E";
        e.id =counter++;

        treeGenerator.Node f = new treeGenerator.Node(c);
        f.name = "F";
        f.id =counter++;

        treeGenerator.Node g = new treeGenerator.Node(c);
        g.name = "G";
        g.id =counter++;

        treeGenerator.Node h = new treeGenerator.Node(g);
        h.name = "H";
        h.id =counter++;

        treeGenerator.Node i = new treeGenerator.Node(d);
        i.name = "I";
        i.id =counter++;
        treeGenerator.Node j = new treeGenerator.Node(i);
        j.name = "J";
        j.id =counter++;


        b.children.add(d);
        b.children.add(e);
        d.children.add(i);
        i.children.add(j);
        c.children.add(f);
        c.children.add(g);
        g.children.add(h);

        root.children.add(b);
        root.children.add(c);
        nodes.add(root);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);
        nodes.add(e);
        nodes.add(f);
        nodes.add(g);
        nodes.add(h);
        nodes.add(i);
        nodes.add(j);
        this.tree = nodes;

    }
    public void solver(){
        ArrayList<treeGenerator.Node> nodes = this.tree;
        computeWeight(nodes); //compute the weight for each node


        Integer[][] table = new Integer[nodes.size()][nodes.size()];
        for(int i =0; i<nodes.size(); i++){
            for (int j=0; j<nodes.size();j++){
                barometer++;

            if (i==j) table[i][j]=computeCost(nodes.get(j));
//            if (i==0 && j==0)
            else if (!computeReachable(nodes.get(i),nodes.get(j))) table[i][j] =-1;
            else {
                table[i][j] = computeCost(nodes.get(j)) ;
            }
            }
        }

//        matrixPrinter(table); //check table
        Integer[][] preResult = new Integer[nodes.size()][2];
        for (int i =0; i<nodes.size(); i++){
            preResult[i][0]=i;
            int totalCost = 0;
            for (int j=0; j< nodes.size();j++){

                totalCost+=table[i][j];
            }
            preResult[i][1] = totalCost;
        }
//        matrixPrinter(preResult);//display original preresult

        Arrays.sort(preResult, new Comparator<Integer[]>()
        {
            @Override
            public int compare(Integer[] int1, Integer[] int2)
            {
                Integer numOfKeys1 = int1[1];
                Integer numOfKeys2 = int2[1];
                return numOfKeys2.compareTo(numOfKeys1);
            }
        });
//         matrixPrinter(preResult);
         computeSolution(preResult);

    }
    public void computeSolution(Integer[][] result){
        ArrayList<String> seq=new ArrayList<>();
        int rounds=1;
        int index=0;
        int prevIndex= 0;
        treeGenerator.Node n0 = this.tree.get(result[0][0]);
        index++;
        treeGenerator.Node n1 = this.tree.get(result[1][0]);
        index++;
        String s = "From " + n0.name + " to " + n1.name + " in the first round.";
        seq.add(s);
        n0.visited = true;
        n1.visited = true;
        rounds++;
        ArrayList<Integer> visited = new ArrayList<>();
        visited.add(n0.id);
        visited.add(n1.id);
        prevIndex=index;
        while(visited.size() < this.tree.size()){
            if (this.tree.get(result[index][0]).parent.visited){
                visited.add(result[index][0]);
//                System.out.println(visited.size());
                s = "From " + this.tree.get(result[index][0]).parent.name + " to " + this.tree.get(result[index][0]).name + " in Rounds : " + Integer.toString(rounds);
                seq.add(s);
                index++;
            }
            else{
                rounds++;
                for(int i=prevIndex; i<index; i++){//update the visited states of the nodes from last round
                    this.tree.get(visited.get(i)).visited=true;
                }
            }
        }

        System.out.println("The minimum rounds needed is :" + Integer.toString(rounds));
        for (String ss : seq
             ) {
            System.out.println(ss);

        }
        System.out.println("The complexity is : " + Integer.toString(barometer));


    }
    /**
     * Simply display the 2d matrix
     * @param display
     */
    public void matrixPrinter(Integer[][] display){
        for (int i = 0; i < display.length; i++) {//check matrix
            for (int j = 0; j < display[i].length; j++) {
                System.out.print(display[i][j] + " ");
            }
            System.out.println();
        }
    }
    // compute weight of each node which is simply the number of children consist
    public void computeWeight(ArrayList<treeGenerator.Node> nodes){
        for (treeGenerator.Node n : nodes
             ) {
            n.weight = n.children.size();
        }
    }
    //Compute the cost to this node required so far
    public int computeCost( treeGenerator.Node to){
        int cost = 0;
        treeGenerator.Node temp = to;
        while(temp.id!=this.tree.get(0).id){
            barometer++;
            temp = temp.parent;
            cost+=temp.weight;
        }
        return cost;
    }

    /***
     * check if this node can be reached from the other node
     * @param from
     * @param to
     * @return
     */
    public boolean computeReachable(treeGenerator.Node from, treeGenerator.Node to){
//        if(from.id==this.tree.get(0).id) return false;
        if(to.id==this.tree.get(0).id) return false;
        if(to.parent.id == from.id) return true;
            else{
                treeGenerator.Node temp = to.parent;
                while(temp!=null){
                    barometer++;
                    if (temp.id==from.id){
                        return true;
                    }
                    temp = temp.parent;
                }
            }
        return false;
    }
    public  static void main(String[] args) {
        fastestBroadcast fs = new fastestBroadcast();
        fs.solver();

    }

}
