import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class SkylineProblem {
    private double[][] buildings;
    private int count_divide = 0;
    private int count_merge = 0;
    private int count_update = 0;
    /**
     * Constructors
     * One for the dataset input by hand
     * The other one for retrieving the dataset generated in the code
     * @param buildings
     */
    public SkylineProblem(double[][] buildings){ //this constructor only for (l,r,h) data
        if(checkInput(buildings)){
            this.buildings = buildings;
            System.out.println("Input correct");
        }
        else{
            System.out.println("Incorrect inputs for buildings");
        }
        double[][] sortedBuildings = sortBuildings(this.buildings);
        this.buildings = sortedBuildings;

    }
    public SkylineProblem(int num){
        double[][] buildings = dataGenerator(num);
        if (checkInput(buildings)){
            this.buildings = sortBuildings(buildings);
        }
        else{
            System.out.println("Incorrect Data generated");
        }

    }

    /**
     * Check the input data suitable or not
     * @param buildings
     * @return a boolean indicates if the data is suitable
     */
    public boolean checkInput(double[][] buildings){

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

            Painter p = new Painter(this.buildings,false); //draw the buildings
        }
        System.out.println(Integer.toString(buildings.length) + " buildings received as input data. :) ");
        return true;
    }

    /**
     * Sort the buildings in the 2d array based on the left coordinates of the building
     * @param buildingsToSort
     * @return 2d array consist the sorted buildings
     */
    private  double[][]  sortBuildings(double[][] buildingsToSort){

        Arrays.sort(buildingsToSort, Comparator.comparingDouble(o -> o[0]));


        return buildingsToSort;
    }

    /**
     *  Solving the skyline Problem including the direct case and divide it into two sub- problems
     *  Divide the problem into two sub-problems
     * @return the final skyline of the buildings
     */
    public ArrayList<List<Double>> solveSkyline(double[][] buildings){

        ArrayList<List<Double>> output = new ArrayList<List<Double>>();
//        double[][] buildings = this.buildings;
        int n  = buildings.length; // check the number of buildings first to make sure it has more than 2 buildings to divide
        /*
            Base cases that only contain less than two buildings
         */
        if(n==0) return output; // when buildings do not consist any building
        if(n==1) {//for the direct cases where the buildings only consist one building in it
            double left = buildings[0][0];
            double right = buildings[0][1];
            double height = buildings[0][2];
            
            output.add(new ArrayList<Double>(){{add(left);add(height);}});
            output.add(new ArrayList<Double>(){{add(right); add(0.0);}});
            return output;
        }
        /*
            If there is more than one building
            Recursively divide the input into two sub-problems
         */

        this.count_divide+=1; //counting the number of division the group
        // As the buildings been sorted so simply divide into two parts based on the index
        ArrayList<List<Double>> leftSkyline,rightSkyline;
        leftSkyline = solveSkyline(Arrays.copyOfRange(buildings,0,n/2));
        rightSkyline = solveSkyline(Arrays.copyOfRange(buildings, n/2, n));
        
        //Merge the results of these sub-problems
        return mergeSkylines(leftSkyline,rightSkyline);
    }

    /**
     * Merge the left part and the right part together
     * @param leftSkyline
     * @param rightSkyline
     * @return
     */
    private ArrayList<List<Double>> mergeSkylines(ArrayList<List<Double>> leftSkyline, ArrayList<List<Double>> rightSkyline) {
        int nLeft = leftSkyline.size(), nRight = rightSkyline.size();// Number of size of two skyline
        int pL = 0, pR = 0;
        double currY = 0, leftY = 0, rightY = 0; //Here Y representing height just for short here change it to Y
        double x, maxY;
        ArrayList<List<Double>> output = new ArrayList<List<Double>>();

        while ((pL < nLeft) && (pR < nRight)) {
            this.count_merge+=1;
            List<Double> pointL = leftSkyline.get(pL);
            List<Double> pointR = rightSkyline.get(pR);
            // pick up the smallest x
            if (pointL.get(0) < pointR.get(0)) {
                x = pointL.get(0);
                leftY = pointL.get(1);
                pL++;
            }
            else {
                x = pointR.get(0);
                rightY = pointR.get(1);
                pR++;
            }
            // max height (here short by y ) between both skylines
            maxY = Math.max(leftY, rightY);

            // update output if there is a skyline vertically change
            if (currY != maxY) {

                updateOutput(output, x, maxY);
                currY = maxY;
            }
        }

        //add to skyline
        addSkyline(output, leftSkyline, pL, nLeft, currY);
        addSkyline(output, rightSkyline, pR, nRight, currY);

        return output;
    }

    /**
     * Having new element then update the output
     */
    public void updateOutput(ArrayList<List<Double>> output, double x, double y) {
        // if skyline change is not vertical then add the new point
        if (output.isEmpty() || output.get(output.size() - 1).get(0) != x){
            output.add(new ArrayList<Double>() {{add(x); add(y); }});
        }

        // If the skyline change then update the last point
        else {
            output.get(output.size() - 1).set(1, y);
        }
    }

    /**
     * Append the new points into the output skyline
     * @param output
     * @param skyline
     * @param pp
     * @param n
     * @param currY current Y
     */
    public void addSkyline(ArrayList<List<Double>> output, ArrayList<List<Double>> skyline,
                              int pp, int n, double currY) {
        while (pp < n) {
            List<Double> point = skyline.get(pp);
            double x = point.get(0);
            double y = point.get(1);
            pp++;

            // update output
            // if there is a skyline change
            if (currY != y) {
                updateOutput(output, x, y);
                currY = y;
            }
        }
    }


    /**
     * This function is used to set up the window for drawing the buildings
     * @param buildings the buildings to be drawn
     */
    public  void drawBuildings(double[][] buildings){
            Painter p = new Painter(this.buildings,true);
//            Object lock = new Object();
            JFrame frame = new JFrame("DrawGraph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(p);
            frame.setSize(600,600);
//            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//            frame.pack(); // can be used to create a window which size can auto suits the graph
            frame.setLocationByPlatform(true);
            frame.setVisible(true);

    }

    /**
     *  Draw skylines for the output skylines, this function will open new window frame for the graph
     * @param output
     */
    public void drawSkylines(ArrayList<List<Double>> output){
        Painter p = new Painter(output,false);
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(p);
        frame.setSize(600,600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);


    }

    /**
     * Data Genreator
     * @param numData number of data to generate
     * @return 2d array consist the data set to be solved
     */
    public double[][] dataGenerator(int numData){
        double rangeMin = 0;//So here we can change the location of the buildings
        double rangeMax = 50;
        double heightMin = 1;
        double heightMax =100;
        Random r = new Random();
//        r.setSeed(361);
        ArrayList<double[]> dataset=  new ArrayList<>();
        for (int i =0; i<numData; i++) {
            double left = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            double right = rangeMin + (rangeMax -rangeMin) * r.nextDouble();
            while (true){//here only for left==right situation
                int b = Double.compare(left,right);
                if (b!=0) {break;}
                else{
                    System.out.println("Equal");
                }
                right = rangeMin + (rangeMax -rangeMin) * r.nextDouble();
            }

            double height = heightMin + (heightMax - heightMin) * r.nextDouble();
            if (left > right) {
                double temp = 0;
                temp = left;
                left = right;
                right = temp;
            }

            double[] data = new double[3];
            data[0] = left;
            data[1] = right;
            data[2] = height;

            dataset.add(data);
        }
        int len = dataset.size();

        double[][] datasetD = new double[len][3];
        for (int i=0; i<len; i++
             ) {
            datasetD[i][0] = dataset.get(i)[0];
            datasetD[i][1] = dataset.get(i)[1];
            datasetD[i][2] = dataset.get(i)[2];

        }
        return datasetD;

    }


    /**
     * Main Method
     * @param args
     */
    public static void main(String[] args) {
        //This part is used for test extreme cases and the case provided in the description of the assignment
// Data from Assignment description {(0.0, 2.0, 1.0),(1.0, 4.0, 2.0),(2.0, 3.0, 3.0),(5.0, 6.0, 2.0)}40
//        double[][] testData = new double[4][3];
//        testData[0] = new double[]{0.0, 2.0, 1.0};
//        testData[1] = new double[]{1.0, 4.0, 2.0};
//        testData[2] = new double[]{2.0, 3.0, 3.0};
//        testData[3] = new double[]{5.0, 6.0, 2.0};
        // Extreme cases that buildings are all overlapping and need to do comparisons every time
//        testData[0] = new double[]{10.0, 10.0, 10.0};
//        testData[1] = new double[]{10.0, 20.0, 20.0};
//        testData[2] = new double[]{10.0, 30.0, 30.0};
//        testData[3] = new double[]{10.0, 40.0, 40.0};


//        SkylineProblem skP = new SkylineProblem(testData); //this line using the data shown above without this gonna use the data from the generator
        int numData = 20; // number of buildings in the data-set
        SkylineProblem skP = new SkylineProblem(numData);
        skP.drawBuildings(skP.buildings);
        ArrayList<List<Double>> output = skP.solveSkyline(skP.buildings);
        skP.drawSkylines(output);
//        System.out.println("Barometer for update : " + Integer.toString(skP.count_update) );
        System.out.println("Barometer for merge : " + Integer.toString(skP.count_merge));
        System.out.println("Barometer for division : " + Integer.toString(skP.count_divide));
        System.out.println("Total Complexity : "  + Integer.toString(skP.count_divide+ skP.count_merge + skP.count_update));
    }


    /**
     *  Painter class to plot the graphs
     */
    public class Painter extends JPanel{
        private static final int BORDER_GAP = 10;
        private static  final int BOTTOM = 500;
        private double[][] buildings;
        ArrayList<List<Double>> skylines;
        boolean r; //if r is true, then draw buildings otherwise draw skyline
        public Painter(double[][] buildings,boolean r){
                this.r = r;
                this.buildings = buildings;



        }
        public Painter(ArrayList<List<Double>> skylines, boolean r){
            this.skylines = skylines;
            this.r = r;

        }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



        // draw buildings
        g2.setColor(Color.black);
        if (this.r){
            for (double[] b:buildings) { // Here using the buildings within the painter class
                int left = (int) (b[0]*10+BORDER_GAP);
                int right = (int) (b[1]*10+BORDER_GAP);
                int height = (int) (500-(b[2]*5));
                g2.drawLine(left,BOTTOM,left,height);//left part of building
                g2.drawLine(left,height,right,height);//top of the building
                g2.drawLine(right,height,right,BOTTOM);

            }
        }
        else{
           for(int i=0; i<skylines.size();i++){
               int x = (int) ((skylines.get(i).get(0))*10+ BORDER_GAP);
               int y = (int) (500.0- (skylines.get(i).get(1))*5);
               g2.setColor(Color.red);
               g2.fillOval(x-2,y,4,4);
           }
            g2.setColor(Color.black);
            int x = (int)(skylines.get(0).get(0) * 10) + BORDER_GAP;
            int y = 500 -  (int)(skylines.get(0).get(1) * 5);
            g2.drawLine(x,BOTTOM, x,y);
           for(int i=1; i< skylines.size(); i++){
                int currentX = (int)(skylines.get(i).get(0) * 10) + BORDER_GAP;
                int currentY = 500 -  (int)(skylines.get(i).get(1) * 5);
                g2.drawLine(x,y,currentX,y); // draw the top line of skyline
               g2.drawLine(currentX,y,currentX,currentY);
               x = currentX;
               y = currentY;
           }

        }
        g2.setColor(Color.BLUE);// axis
        g2.drawLine(BORDER_GAP,500,500+BORDER_GAP,500);
        g2.drawLine(BORDER_GAP,0,BORDER_GAP,500);
    }
    }
}
