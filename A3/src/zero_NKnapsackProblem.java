import java.util.ArrayList;
import java.util.Random;
public class zero_NKnapsackProblem {
    ArrayList<Integer> weights = new ArrayList<>();
    ArrayList<Integer> values = new ArrayList<>();
    ArrayList<Integer> numOfCopies = new ArrayList<>();
    int weightLimits = -1;
    int barometer = 0;
    public zero_NKnapsackProblem(){
//        int seed = 361;
//        Knapsack kn = new Knapsack(10,500,5);
        Knapsack kn = new Knapsack(10,1000,5);

        ArrayList<Integer[]> mydata = kn.items;
        this.weightLimits = kn.weightLimit;
        System.out.println("The weight limit is " +  this.weightLimits);
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

    public int solver(ArrayList<Integer>weights, ArrayList<Integer>values, ArrayList<Integer>numOfCopies, int weightLimits){
        int n = values.size();
        ArrayList<Integer> numUsed = new ArrayList<>();
        ArrayList<Integer> weightss = new ArrayList<>();
        ArrayList<Integer> valuess = new ArrayList<>();
        int nn = 0;
        for(int i = 0; i < n; i++){
            int c = numOfCopies.get(i);
                for(int j =1; j <=c; j++){
                    weightss.add(weights.get(i) * j );
                    valuess.add(values.get(i)*j);
                    numUsed.add(j+1);
                }

        }
        nn = weightss.size();
        int[][] dpResult = new int[nn+1][weightLimits+1];
//
//        for (int i =0; i < n; i++){
//            for (int j = 0; j < n; j++) {
//                dpResult[i][j] = 0;
//
//            }
//            numUsed.add(0);
//        }

        for (int i = 1; i <= nn; i++){
            for (int j=1; j <= weightLimits; j++){
                this.barometer +=1;
                if(weightss.get(i-1)> j){
                    dpResult[i][j] = dpResult[i-1][j];
                }
                else{
//                    if (numUsed.get(i)> )
                    dpResult[i][j] = Math.max(dpResult[i-1][j], (dpResult[i-1][j-(weightss.get(i-1))]+valuess.get(i-1)));
                }
//                if (weightss.get(i-1)<= j){
//
//                    if (numOfCopies.get(i-1)>0){
//                        dpResult[i][j] = Math.max((values.get(i-1)+dpResult[i-1][j-weights.get(i-1)]), dpResult[i-1][j]);
//                        numOfCopies.set(i-1,numOfCopies.get(i-1)-1);
//                    }
//                    //                    m[x][y] = Math.max(m[x-1][y], (m[x-1][y- (weights.get(x-1))]+ values.get(x-1)));
//
//                    else{
//                        dpResult[i][j] = Math.max((values.get(i-1) + dpResult[i-1][j-(weights.get(i-1))]), dpResult[i-1][j]);
//                    }
//
//                }
//                else{
//                    dpResult[i][j] = dpResult[i-1][j];
//                }
            }
        }

        displayResultMap(dpResult);
        System.out.println(" The result is : " + dpResult[nn][weightLimits]);
        return dpResult[nn][weightLimits];
    }


    public void displayResultMap(int[][] m){
        int rows = m.length;
        int cols = m[0].length;

        for(int i = 0; i<rows; i++)
        {
            for(int j = 0; j<cols; j++)
            {
                System.out.print(m[i][j]);
                System.out.print("  |");
            }
            System.out.println();
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
        System.out.println(" The barometer says : " + this.barometer );

    }

     public static void main(String[] args) {
        zero_NKnapsackProblem oNknapsack = new zero_NKnapsackProblem();
        oNknapsack.solver(oNknapsack.weights, oNknapsack.values, oNknapsack.numOfCopies, oNknapsack.weightLimits);
    }


}



