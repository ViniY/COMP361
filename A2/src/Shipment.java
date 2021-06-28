import java.util.ArrayList;
import java.math.*;
public class Shipment {
    ArrayList<Integer> sequence= new ArrayList<>();
    int r = 1;
    int c = 10;
    int barometer= 0;
    public Shipment(int r, int c){
        sequence.add(0);
        sequence.add(11);
        sequence.add(9);
        sequence.add(9);
        sequence.add(12);
        sequence.add(12);
        sequence.add(12);
        sequence.add(12);
        sequence.add(9);
        sequence.add(9);
        sequence.add(11);
        this.r=r;
        this.c=c;
        computeTable();

    }
    public void computeTable(){

        int DHLCost= 4*this.c;
        if (sequence.size()<4){
            int cost =0;
            for (int i=0; i<sequence.size();i++){
                cost+=sequence.get(i) * r;
                barometer++;

            }
            if(cost>DHLCost){
                System.out.println("All use the DHL and the cost is 40");
            }


        }
        Integer[][] table=new Integer[this.sequence.size()][2];
        table[0][0]=  0;
        table[1][0] = sequence.get(1) * this.r;
        table[2][0] = sequence.get(2) *this.r + table[1][0];
        table[3][0] = sequence.get(3) *this.r + table[2][0];
        table[0][1] = 1;
        table[1][1] = 1;
        table[2][1] = 1;
        table[3][1] = 1;

        for (int i=4; i < this.sequence.size(); i++){
            barometer++;
            if (table[i-1][0]+sequence.get(i)*this.r > table[i-4][0]+DHLCost){
                table[i][1] = 2;
            }
            else{
                table[i][1]=1;
        }
            table[i][0] = Math.min(table[i-1][0]+sequence.get(i)*this.r, table[i-4][0]+DHLCost);
        }

        System.out.println("The minimum cost is : " + table[sequence.size()-1][0]);
        matrixPrinter(table);
        System.out.println("Barometer : " + barometer);

    }
    public void matrixPrinter(Integer[][] display){
        for (int i = 0; i < display.length; i++) {//check matrix
            for (int j = 0; j < display[i].length; j++) {
                System.out.print(display[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Shipment sh = new Shipment(2,15);
    }
}

