/**
 * The knapsack class contains the information about the question,
 * c representing the number of copies for each item
 * c==1 then it is a 0-1 knapsack problem
 * c>1 then it turns into a 0-N knapsack problem
 *
 *
 */
import java.util.ArrayList;
import java.util.Random;
public class Knapsack {
    int size = 0;
    int weightLimit=0;
    int c =1;//the number of copies of each items
    int maxValue =100; //the maximum value of each item can be
    // For each item, it contains three value -- weight-- the profit of each item -- the number of copies
    ArrayList<Integer[]> items = new ArrayList<>();

    public Knapsack( int numItem, int weightLimit, int maxNumber){
//        Random r = new Random();
//        r.setSeed(seed);
        if (maxNumber>0){
            this.c = maxNumber;
        }
        this.weightLimit = weightLimit;
        this.items = dataGenerator(numItem,c,weightLimit);
        this.size = this.items.size();
        describeData();
    }
    public Knapsack(){// manually inserted data
        this.size = 4;
        this.weightLimit = 10;
        ArrayList<Integer[]> data = new ArrayList<>();
        Integer[] item1 = new Integer[3];
        item1[0]  = 7;
        item1[1]  = 49;
        item1[2]  = 1;

        Integer[] item2 = new Integer[3];
        data.add(item1);
        item2[0]  = 5;
        item2[1]  = 30;
        item2[2]  = 1;
        data.add(item2);

        Integer[] item3 = new Integer[3];
        item3[0]  = 5;
        item3[1]  = 25;
        item3[2]  = 1;
        data.add(item3);

        Integer[] item4 = new Integer[3];
        item4[0]  = 4;
        item4[1]  = 24;
        item4[2]  = 1;
        data.add(item4);
        this.items = data;
        describeData();
    }



    public ArrayList<Integer[]> dataGenerator(int numItem, int c, int weightLimit){

        Random random = new Random();
        random.setSeed(361);
        ArrayList<Integer[]> items = new ArrayList<>();
        for (int i=0 ; i < numItem; i++){
            int value = random.nextInt(this.maxValue)+1;
            int weight = random.nextInt((int)(weightLimit/2))+1;
            int copies = random.nextInt(c)+1;
            Integer[] item = new Integer[3];
            item[0] = weight;
            item[1] = value;
            item[2] = copies; //number of copy
            items.add(item);
        }
        return items;
    }

    public void describeData(){
        int size = this.size;
        System.out.println("The number of objects is : " + size);
        for (int i= 0; i < size; i++){
            System.out.println("The " + i + " th item has weight : " + items.get(i)[0] + " and the value is " + items.get(i)[1] + " and it has " + items.get(i)[2]);
        }
    }
}
