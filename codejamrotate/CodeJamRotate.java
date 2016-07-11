//File:		CodeJamRotate.java
//Author:	Gary Bezet
//Date:		2016-07-11
//Desc:		Designed to solve google Code Jam Rotate from 2010 Round 1A
//Problem:	https://code.google.com/codejam/contest/544101/dashboard#s=p0
//Results:	A-small-practice.in: 75ms       A-large-practice.out: 166ms

package codejamrotate;

import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CodeJamRotate {

    
    //variables
    public long starttime;
    
    //file streams
    private BufferedReader infile;
    private PrintStream outfile;
    
    //filename variables
    private String infileopt, outfileopt;  
    
    private long linenum = 0; //keeps track of the current line number of the infile for error printing purposes    
    
    private int totalcases;  //total number of cases
    
    private TestCase[] testcases;  //holds all the test cases
    
       
    
    /*
        Functions
    */
    
    //program starts here
    public void run(String[] args) {
        this.loadoptions(args);  //load arguments
        
        this.openFiles();
        
        this.loadData();
        
        //rotate all cases
        long startrotate = System.currentTimeMillis();
        for(int i=0; i<totalcases; i++) {
            
            testcases[i].rotate();
            
        }
        System.err.printf("%1$d test cases rotated in %2$d...solving\n", totalcases, System.currentTimeMillis() - startrotate );
        
        long startsolve = System.currentTimeMillis();
        for(int i=0; i<totalcases;i++) {
            testcases[i].solve();
        }
        System.err.printf("%1$d test cases solved in %2$dms...\nOutputing data to:  %3$s\n\n", this.totalcases, System.currentTimeMillis()-startsolve, this.outfileopt);
        
        long startoutput = System.currentTimeMillis();
        for(int i=0; i<this.totalcases; i++) {
            outfile.printf("Case #%1$d: %2$s\n", testcases[i].casenum, testcases[i].answer());
        }        
        System.err.printf("%1$d test cases output in %2$dms!\n", this.totalcases, System.currentTimeMillis() - startoutput);
        
        this.closeFiles();  //close files
    }
    
    //load data from file into data structure
    private void loadData() {
        
        long loaddatastarttime = System.currentTimeMillis();  //the time we started loading data
        
        System.err.printf("\nLoading data from \"%1$s\"...\n", this.infileopt);
        
        String line = this.infileReadLine();
        
        //get case numbers
        try{
            totalcases = Integer.parseInt(line);
        } catch(NumberFormatException ex) {
            System.err.printf("Could not parse total number of cases (line #%1$d) from:  %2$s", this.linenum, this.infileopt);
            System.exit(4);
        }
        
        //initialize testcases array
        testcases = new TestCase[totalcases];
        
        
        //load all cases
        for(int i=0; i < this.totalcases; i++) {
            
            
            line = this.infileReadLine(); //get matrix size and k

            String[] matrixandk = line.split(" ");
            
            if(matrixandk.length != 2) {  //check formating of matrixandk line and panic
                System.err.printf("Wrong number of arguments (%3$d) line(%1$d) from:  %2$s", this.linenum, this.infileopt, matrixandk.length);
                System.exit(5);
            }
         
            //try to parse matrix size and k and initialize the data structure for this test case panic if fail
            try{
                testcases[i] = new TestCase(Integer.parseInt(matrixandk[0]), Integer.parseInt(matrixandk[1]), i+1);
                
            }catch(NumberFormatException ex) {
                System.err.printf("Could not parse matrix size and k (line #%1$d) from:  %2$s", this.linenum, this.infileopt);
                System.exit(5);
            }    
            
            System.err.printf("Reading testcase #%1$d, N=%2$d  K=%3$d\n", this.testcases[i].casenum, this.testcases[i].matrixsize, this.testcases[i].k);
            
            //read all rows in matrix
            for(int c=0; c<testcases[i].matrixsize; c++) {  
                line = this.infileReadLine();
               
                //read all columns in row
                for(int d=0; d<testcases[i].matrixsize; d++) {
                    testcases[i].matrix[c][d] = line.charAt(d);
                }
                
                
            }
            
            //System.err.print(this.testcases[i]); //for testing
            
        }
        
        System.err.printf("All data loaded in %1$dms\n", System.currentTimeMillis() - loaddatastarttime);
        
    }
    
    //open the files
    public void openFiles() {
        try {
            infile = new BufferedReader(new FileReader(infileopt));
        } catch (FileNotFoundException ex) {
            System.err.printf("Error could not open infile \"%1$s\":  $2%s\n", infileopt, ex.toString());
            System.exit(2);
        }
        
        if(outfileopt == null) {
            outfileopt = "Stdout";
            outfile = System.out;
        } else {
            try {
                outfile = new PrintStream(new File(outfileopt));
            } catch (FileNotFoundException ex) {
                System.err.printf("Error could not open outfile \"%1$s\":  $2%s\n", outfileopt, ex.toString());
                System.exit(2);
            }
        }
        
        System.err.printf("Infile:  %1$s\nOutfile:  %2$s\n", infileopt, outfileopt);
        
        
                
    }
    
    //reads a line panic if exception
    private String infileReadLine() {  
        
        linenum++;

        try {
                return infile.readLine();
        } catch (IOException e) {
                System.err.printf("Unable to read line #%3$d from \"%1$s\":  %2$s\n", infileopt, e.toString(), this.linenum);
                System.exit(3);
        }

        return null;  //get rid of netbeans error   
    }
    
    //close files
    private void closeFiles() {
        try {
            infile.close();
        } catch (Exception ex) {
            //do nothing the program is already finished
        }
        
        outfile.close();
    }
    
    //loads the command line options
    private void loadoptions(String[] args) {
        
		
        if(  2 < args.length || args.length == 0 ) {
                System.err.println("Program requires 1 or 2 arguments.  First arg is infile name, 2nd arg is outfile name StdOut by default.");
                System.exit(1);  //exit the system if arguments not correct
        }

        infileopt = args[0];

        if( args.length == 2 ) {  //set the outfile if exists
                outfileopt = args[1];
        }

    }
    
    public static void main(String[] args) {
        //load class and call run()
        CodeJamRotate prog = new CodeJamRotate();
        prog.starttime = System.currentTimeMillis();
        prog.run(args);
        System.err.printf("Program finished in %1$dms\n", System.currentTimeMillis() - prog.starttime);
        
    }
    
}
