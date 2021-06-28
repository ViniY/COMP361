import java.util.ArrayList;
import java.util.Random;
/*
This class is used for creating buildings based on the coordinates and the height of the building
 */

public class Building {
    private double left;
    private double right;
    private double height;
    private int id;
    public Building(double l,double r, double h ){
        this.left = l;
        this.right = r;
        this.height = h;
        Random rn = new Random();
        int id = rn.nextInt(100) ;
        this.id = id;
    }

}
