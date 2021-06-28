import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Random;
import java.math.*;

/**
 * In this assignment we are working for party L, so we aim to let L win.
 * And the number of precincts are even numbers we are aiming to grouping them into two district each
 * of them consist n/2 precincts
 *
 * Return True if it is possible to gerrymandering
 */
public class GerrymanderingProblem {
    int numberOfPrecincts;
    int pop;
    int barometer = 0;
    ArrayList<Precincts> precincts = new ArrayList<>();
public GerrymanderingProblem(int nP, int pop){
    this.barometer = 0;
    this.numberOfPrecincts = nP;
    this.pop = pop;
    this.precincts = this.dataGenerator(this.numberOfPrecincts,this.pop);
}
public GerrymanderingProblem(){
    Precincts p1 = new Precincts(55,45);
    Precincts p2 = new Precincts(43,57);
    Precincts p3 = new Precincts(60,40);
    Precincts p4 = new Precincts(47,53);
    ArrayList<Precincts> pp = new ArrayList<>();
    pp.add(p1);
    pp.add(p2);
    pp.add(p3);
    pp.add(p4);

    this.numberOfPrecincts = pp.size();
    this.pop = pp.get(0).voterL + pp.get(0).voterN;
    this.precincts = pp;
}


    /**
     *
     * @param num number of precincts. must be even
     * @param pop number of votes in each precincts should be even
     * @return A list of precincts
     */
    public ArrayList<Precincts> dataGenerator(int num, int pop){
    Random rand = new Random();
    int nL;
    int nN;
    ArrayList<Precincts> precincts = new ArrayList<>();
    for (int i = 0; i<num; i++){
        nL  = rand.nextInt(pop);
        nN  = pop-nL;
        Precincts p = new Precincts(nL,nN);
        precincts.add(p);
    }
    return precincts;
}

    /**
     * The function will check if the given data in the field is possible to do a gerrymandering
     * @return true if it is possible to gerrymander the n precincts
     */
    public boolean gerrymanderingProblemSolver(){
        boolean gerrymanderAble = false;
        boolean[][][][] table = computeTable(this.numberOfPrecincts,this.pop); // compute the table and pass to the valid checker
        System.out.println("The complexity of computing table : " + this.barometer);

        int mn = this.numberOfPrecincts * this.pop;
        int halfN = this.numberOfPrecincts/2;//must be int
        int n = this.numberOfPrecincts;
        int majority = mn/4+1;
        boolean result = false;
        for (int x=majority; x<=mn; x++){
            for (int y =majority; y<=mn; y++){
                this.barometer++;
                if (x>=(mn/4) && y>=(mn/4)){
                    if(table[n-1][halfN][x][y]){
                        result = true;
                    }
                }
            }
        }
    return result;
}

    /***
     *
     * @param n the number of precincts
     * @param m the number of voters in each precincts
     * @return
     */
    private boolean[][][][] computeTable(int n, int m) {
        //s(j,k,x,y) -> true if the first j precincts: k are assigned to D1 and exactly x vote for L in D1 and exactly vote for L in D2
        int j,k,x,y;

        int mn = m * n;
        int counter = 0;
        int majority = mn/4 + 1;
        ArrayList<Precincts> precincts = (ArrayList<Precincts>) this.precincts.clone();

        boolean[][][][] s = new boolean[n][(n/2)+1][mn+1][mn+1]; //table contains

//        s[0,0,0,0]= true; Base case


        for (j = 0; j < n; j++){
            for (k = 0; k <= n/2; k++){
                for (x = 0; x <= mn; x++){
                    for (y = 0; y <= mn; y++){
                        counter+=1;
                        int votesL = precincts.get(j).getVoterL();
                        if(j == 0) {
                            if (x == votesL && k == 1 && y == 0) {
                                s[j][k][x][y] = true;
                            } else if (y == votesL && k == 0 && x == 0) {
                                s[j][k][x][y] = true;
                            } else {
                                s[j][k][x][y] = false;
                            }
                        }
                        else {
                            if (k<1){
                                s[j][k][x][y] = false;
                            }
                            else if (x >= votesL  && s[j-1][k-1][x-votesL][y]) {
                                s[j][k][x][y] = true;
                            } else if ( y >= votesL && s[j-1][k][x][y-votesL]) {
                                s[j][k][x][y] = true;
                            } else {
                                s[j][k][x][y] = false;
                            }
                        }
                    }
                }
            }
        }
        this.barometer+=counter;



        return s;
    }






private class Precincts{
    int voterL;
    int voterN;
    public Precincts(int voterL, int voterN){
        this.voterL = voterL;
        this.voterN = voterN;
    }
    public int getVoterL() {
        return voterL;
    }

    public void setVoterL(int voterL) {
        this.voterL = voterL;
    }

    public int getVoterN() {
        return voterN;
    }

    public void setVoterN(int voterN) {
        this.voterN = voterN;
    }


    @Override
    public String toString() {
        String ss = "There are " + voterL + " voters for party L " +  " and there are " + voterN + " voters for party N.";
        return (ss);
    }

}
    public static void main(String[] args) {
        int numberOfPrecincts = 10; //must be even
        int popForEachPrecincts = 100;
        GerrymanderingProblem gp = new GerrymanderingProblem(numberOfPrecincts,popForEachPrecincts);
//        GerrymanderingProblem gp = new GerrymanderingProblem(); //only for test
        String info = "There are " + numberOfPrecincts + " Precintcs.";
        System.out.println(info);
        for (Precincts p : gp.precincts
             ) {
            System.out.println(p.toString());
        }
        boolean result = gp.gerrymanderingProblemSolver();
        if(result){
            System.out.println("We can do a sneaky gerrymandering");
        }
        else{
            System.out.println("Sadly we can not do gerrymandering");
        }
        int cplexity = (int) (Math.pow(numberOfPrecincts,4) * Math.pow(popForEachPrecincts,2));
        System.out.println("The complexity from formula : " + cplexity);
        System.out.println("The complexity from barometer: " );
        System.out.println(gp.barometer);
    }



}

