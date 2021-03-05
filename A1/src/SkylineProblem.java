import java.util.ArrayList;

public class SkylineProblem {
    private ArrayList<double[]> buildings = new ArrayList<>();
    public SkylineProblem(ArrayList<double[]> buildings){
        if(checkInput(buildings)){
            this.buildings = buildings;
            System.out.println("Input correct");
        }
        else{
            System.out.println("Incorrect inputs for buildings");
//            throw Exception()
        }

    }
    public boolean checkInput(ArrayList<double[]> buildings){

        for (double[] d:buildings
             ) {
            if (!(d instanceof double[])){
                System.out.println("inco");
                return false;}
            int counter = 0;
            double lft =-100;
            double right = -100;

            for (double dd: d
                 ) {
                if (counter==0) lft = dd;
                if (counter==1) right = dd;
                if (counter==2 & dd<0) return false;
                counter++;
            }
            System.out.println("Left " +Double.toString(lft));
            System.out.println("Right " +Double.toString(right));

//            if (lft>right)return false;
        }
        System.out.println("11");
        return true;
    }
/*/
  { (1, 11, 5), (2, 6, 7), (3, 13, 9), (12, 7, 16), (14, 3, 25),
         (19, 18, 22), (23, 13, 29), (24, 4, 28) }
         The data from some webpages Link:
         https://www.geeksforgeeks.org/the-skyline-problem-using-divide-and-conquer-algorithm/
 */
    public static void main(String[] args) {
        ArrayList<double[]> testData = new ArrayList<>();
        testData.add(new double[]{1, 11, 5});
        testData.add(new double[]{2, 6, 7});
        testData.add(new double[]{3, 13, 9});
        testData.add(new double[]{12, 7, 16});
        testData.add(new double[]{14, 3, 25});
        testData.add(new double[]{19, 18, 22});
        testData.add(new double[]{23, 13, 29});
        testData.add(new double[]{23, 13, 29});
        testData.add(new double[]{24, 4, 28});
        SkylineProblem skP = new SkylineProblem(testData);
    }
}
