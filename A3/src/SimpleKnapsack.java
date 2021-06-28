import java.util.ArrayList;

public class SimpleKnapsack {
    int barometer = 0;
    ArrayList<Integer[]> mydata= new ArrayList<>();
    int weightLimit = -1;
    public SimpleKnapsack(boolean gerneator){
        if (gerneator) {
            Knapsack kproblem = new Knapsack(5, 50, 1); //0-1 knapsack problem
            this.weightLimit = 50;
            this.mydata = kproblem.items;
        }
        else{
            Knapsack kpoblem = new Knapsack();
            this.mydata = kpoblem.items;
        }
//        this.mydata = kproblem.items;
    }
    public SimpleKnapsack(int size, int weightLimit){
        //use the manual inserted data from knapsack class;
        this.barometer = 0;
    }

    public int solver(int[]ws, int[]v, int n, int w){
//        System.out.printl;


        if (n<=0 || w<=0){
            return 0;
        }
        int [][] m = new int[n+1][w+1];
        for (int i = 0; i  <= w ; i++) {
            m[0][i] = 0;
        }

        for (int x = 1; x <= n ; x++) {
            for (int y = 1; y <= w; y++){
                this.barometer++;
                if(ws[x-1] > y){
                    m[x][y] = m[x-1][y];
                }
                else{
                    m[x][y] = Math.max(m[x-1][y], (m[x-1][y- (ws[x-1])]+ v[x-1]));
                }

            }

        }
        displayResultMap(m);
        return m[n][w];

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
//        SimpleKnapsack o1knapsack = new SimpleKnapsack(true);
//        SimpleKnapsack o1knapsack = new SimpleKnapsack(false);
          SimpleKnapsack o1knapsack = new SimpleKnapsack(25,50);

//        ArrayList<int[]> result =new ArrayList<>();
        Knapsack simpleData = new Knapsack();
        o1knapsack.weightLimit = simpleData.weightLimit;
        o1knapsack.mydata = simpleData.items;
        int weight[] = new int[o1knapsack.mydata.size()];

        int value[] = new int[o1knapsack.mydata.size()];
        for (int i = 0; i < o1knapsack.mydata.size(); i++){
            weight[i] = o1knapsack.mydata.get(i)[0];
            value[i] = o1knapsack.mydata.get(i)[1];
        }
        int r = o1knapsack.solver(weight,value,weight.length,o1knapsack.weightLimit);

        System.out.println("The maximum value is  :  " + r );


        System.out.println("Then lets try a bigger dataset :) ");


        SimpleKnapsack o1Large = new SimpleKnapsack(true);
        int weight1[] = new int[o1Large.mydata.size()];

        int value1[] = new int[o1Large.mydata.size()];
        ArrayList<Integer[]> mydata = o1Large.mydata;
        for (int i = 0; i < mydata.size(); i++){
            weight1[i] = mydata.get(i)[0];
            value1[i] = mydata.get(i)[1];
        }
        int size =weight1.length;
        int rr = o1Large.solver(weight1,value1,size, o1Large.weightLimit);
        System.out.println(" The final value is : " + rr);
    }

}