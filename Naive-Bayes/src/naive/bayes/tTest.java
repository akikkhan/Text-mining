/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Jayati
 */
public class tTest {
    
    static  double d=5.0;
    static double mean1=0.0,mean2=0.0;
    static double sd1= 0.0;
    static double sd2= 0.0;
    static double t=0.0;
    static double dof=0.0;
    static double n = 50.0;
    static double [] acr1 = new double[50];
    
    public static void main(String args[]) throws FileNotFoundException{
        File file = new File("AcrOut.txt");
        Scanner in = new Scanner(file);
        for(int i=0;i<50;i++)
            mean1+= in.nextDouble();
        mean1 /= 50.0;
        in.close();
        System.out.println("d : "+d);
        System.out.println("Mean NB : "+mean1);
        in = new Scanner(file);
        double numerator =0.0,denumerator=0.0;
        for(int i=0;i<50;i++){
            //in=in.;
            numerator += Math.pow(mean1-in.nextDouble(), 2);
            
           
        }
        numerator /= n;
        sd1 = numerator;
        System.out.println("SD NB : "+sd1);
        file = new File("Accuracies.txt");
        in = new Scanner(file);
        for(int i=0;i<50;i++)
            mean2+= in.nextDouble();
        mean2 /= n;
        in.close();
        
        System.out.println("Mean KNN : "+mean2);
        in = new Scanner(file);
        numerator =0.0;
        denumerator=0.0;
        for(int i=0;i<50;i++){
            //in=in.;
            numerator += Math.pow(mean2-in.nextDouble(), 2);
            
           
        }
        numerator /= n;
        sd2 = numerator;
        System.out.println("SD KNN :"+sd2);
        numerator = mean1- mean2-d;
        denumerator = Math.pow(sd1/n+sd2/n, 0.5);
        t = numerator / denumerator;
        System.out.println("t : "+t);
        numerator = Math.pow(sd1/n+sd2/n,2);
        denumerator = (sd1*sd1 + sd2*sd2)/(n*n*(n-1));
        System.out.println(numerator);
        System.out.println(denumerator);
        dof = numerator / denumerator;
        System.out.println("DOF : "+dof);
        double _alpha = 0.0;
        int min =0 ,max=0;
        
        
        String nhyp = "Null Hypothesis: Ho: µ1-µ2 ≥ d";
        String ahyp = "Alternative Hypothesis: Ha: µ1-µ2 < d";
        System.out.println(nhyp);
        System.out.println(ahyp);
        _alpha = 0.005/2.0;
       
        System.out.println("For Alpha : "+_alpha*2);
        System.out.println("----------------------------");
        min= (int) Math.round(dof);
        System.out.println("Rounded DOF : "+min);
        in = new Scanner(new File("tTable.csv"));
        for(int i=0;i<dof+1;i++){
            in.nextLine();
        }
        String line = in.nextLine();
        //System.out.println(line);
        String[] vals = line.split(",");
        
        double t_alphaBy2_dof = Double.parseDouble(vals[(int) (Math.log10(0.1/ _alpha)/Math.log10(2.0))]);

        System.out.println("T(_alpha/2,DOF) = "+t_alphaBy2_dof);
        if(Math.abs(t)>t_alphaBy2_dof){
            System.out.println("Accepted : "+ahyp);
        }
        else System.out.println("Failed to Reject : "+nhyp);
         _alpha = 0.01/2.0;
        System.out.println("For Alpha : "+_alpha*2);
        System.out.println("----------------------------");
        //min= (int) Math.round(dof);
        System.out.println("Rounded DOF : "+min);
        t_alphaBy2_dof = Double.parseDouble(vals[(int) (Math.log10(0.1/ _alpha)/Math.log10(2.0))]);;
       System.out.println("T(_alpha/2,DOF) = "+t_alphaBy2_dof);
        
        if(Math.abs(t)>t_alphaBy2_dof){
            System.out.println("Accepted : "+ahyp);
        }
        else System.out.println("Failed to Reject : "+nhyp);
        _alpha = 0.05/2.0;
        System.out.println("For Alpha : "+_alpha*2);
        System.out.println("----------------------------");
        //min= (int) Math.round(dof);
        System.out.println("Rounded DOF : "+min);
        t_alphaBy2_dof = Double.parseDouble(vals[(int) (Math.log10(0.1/ _alpha)/Math.log10(2.0))]);;
        System.out.println("T(_alpha/2,DOF) = "+t_alphaBy2_dof);
        if(Math.abs(t)>t_alphaBy2_dof){
            System.out.println("Accepted : "+ahyp);
        }
        else System.out.println("Failed to Reject : "+nhyp);
         
    }
    
}
