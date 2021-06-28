import java.util.ArrayList;

public class bruteForce {
    int numItems = 0;
    int weightLimits = 0;
    int barometer = 0;
    ArrayList<Integer> weights = new ArrayList<>();
    ArrayList<Integer> values = new ArrayList<>();
    ArrayList<Integer> numOfCopies = new ArrayList<>();
    public bruteForce(int numItem, int weightLimits, int maxNumber){
        Knapsack kn = new Knapsack(numItem,weightLimits,maxNumber);
        ArrayList<Integer[]> mydata = kn.items;
        this.weightLimits = weightLimits;
        this.numItems = numItem;
        this.weights.clear();
        this.values.clear();
        this.numOfCopies.clear();
        this.barometer = 0;
        for (Integer[] x : mydata){
            weights.add(x[0]);
            values.add(x[1]);
            numOfCopies.add(x[2]);
        }

    }

    public int allPossible(int n, ArrayList<Integer> w, ArrayList<Integer> v, int weightLimits, ArrayList<Integer> numOfCopies){
        ArrayList<Integer> weightss = new ArrayList<>();
        ArrayList<Integer> valuess = new ArrayList<>();
        int nn = 0;
        for(int i = 0; i < n; i++){
            int c = numOfCopies.get(i);
            for(int j =0; j < c; j++){
                this.barometer++;
                weightss.add(w.get(i));
                valuess.add(v.get(i));
            }
        }
        nn = weightss.size();


        return knapSackSolver(weightLimits, weightss,valuess, nn);
    }

    public int knapSackSolver(int weightLimits, ArrayList<Integer> weights, ArrayList<Integer> values, int n){
        if (n==0 || weightLimits <=0) return 0;
        this.barometer++;
        if(weights.get(n-1)>weightLimits){
            return knapSackSolver(weightLimits,weights, values, n-1);
        }
        else{
            return Math.max(values.get(n-1)+knapSackSolver(weightLimits-weights.get(n-1),weights,values,n-1), knapSackSolver(weightLimits,weights,values,n-1));
        }
    }
    public static void main(String[] args) {
        bruteForce bfKnap = new bruteForce(5,50,5);
        int result = bfKnap.allPossible(bfKnap.numItems, bfKnap.weights, bfKnap.values, bfKnap.weightLimits,bfKnap.numOfCopies);
        System.out.println("The maximum value by brute force is : " +  result);
        System.out.println("The barometer for brute force is : "+ bfKnap.barometer);
    }
}
