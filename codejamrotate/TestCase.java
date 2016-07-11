//File:		TestCase.java
//Author:	Gary Bezet
//Date:		2016-07-11
//Desc:		Designed to solve google Code Jam Rotate from 2010 Round 1A
//Problem:	https://code.google.com/codejam/contest/544101/dashboard#s=p0
//Results:	A-small-practice.in: 75ms       A-large-practice.out: 166ms
//Comments:     The logic in this class could probably be improved so that if there are less rows or columns left to test than would been needed to get to k the iteration breaks.  But the solutions comes fast enough to were the extra headache isn't justfied.

package codejamrotate;


//Data structure for a single testcase
public class TestCase {
    
    //variables
    public int matrixsize;  //size of matrix
    public int k;  //the number in a row you need
    
    public int casenum; //case number
    
    public char[][] matrix;  //the data structure
    
    //keep track of who has k in a row so far
    boolean rhask = false;
    boolean bhask = false;

    //how many have we currently found when one of these hits k we have a winner
    int rinline = 0;  
    int binline = 0;
    
    //initialize data structure
    TestCase(int matrixsize, int k, int casenum) {
        this.matrixsize = matrixsize;

        this.k = k;
        
        this.casenum = casenum;
        
        this.matrix = new char[matrixsize][matrixsize]; //make the matrix
     
        
        
    }
    
    
    //solve this case
    public void solve() {
                   
        
        //check for accross solution
        //iter rows
        for(int r=0; r<this.matrixsize; r++) {
            //iter columns
            this.rinline = 0;
            this.binline = 0;
            for(int c=0; c<this.matrixsize; c++) {
                this.randblinelogic(r, c);
            }
            
        }
        
        //check if 4 in a row in column
        //iter over column
        for(int c=0; c<this.matrixsize; c++) {
            //iter over rows
            this.rinline = 0;
            this.binline = 0;
            for(int r=0; r<this.matrixsize; r++) {
                this.randblinelogic(r, c);
            }
        }
        
        //solve diagnoal top left to bottom right
        //iter over rows
        for(int r=0; r<this.matrixsize; r++) {
            //iter over columns
            for(int c=0; c<this.matrixsize; c++) {
                rinline = 0;  //reset r and b
                binline = 0;

                //move
                for(int offset = 0; offset + c < this.matrixsize && offset + r < this.matrixsize; offset++ ) {
                    this.randblinelogic(r+offset, c+offset);//check the logic
                }
                
            }
        }
        
        //solve diagnoal top right to bottom left
        //iter over rows        
        for(int r=0; r<this.matrixsize; r++) {
            //iter over columns
            for(int c=0; c<this.matrixsize; c++) {
                
                rinline=0;  //reset r and b line counts
                binline=0;
                
                //move
                for(int offset = 0; c-offset >= 0 && offset + r < this.matrixsize; offset++  ) {
                    this.randblinelogic(r+offset, c-offset);
                }
                
            }
            
        }
        
    }
    
    //get the answer back as the expected string
    public String answer () {
        if( rhask && bhask ) {
            return "Both";
        } else if(rhask) {
            return "Red";
        } else if(bhask) {
            return "Blue";
        } else {
            return "Neither";
        }
        
    }
    
    private void randblinelogic(int r, int c) {
        switch (this.matrix[r][c]) {
            case 'R':
                //R has this spot
                rinline++; //increment r
                binline=0; //reset b
                break;
            case 'B':
                //B has this spot
                binline++;  //increment b reset r
                rinline=0;
                break;
            default:
                //reset r and b
                rinline=0;
                binline=0;
                break;
        }

        if(rinline >= this.k) {
            rhask = true;
        }
        if(binline >= this.k) {
            bhask = true;
        }
    }
    
    //rotate the data structure
    public void rotate() {
        char[][] newmatrix = new char[this.matrixsize][this.matrixsize];
        
        //this code rotates then we will use gravity
        //iter over rows
        for(int r=0; r<this.matrixsize; r++) {
            
            //iter over columns
            for(int c=0; c<this.matrixsize; c++) {
                newmatrix[c][this.matrixsize-r-1] = this.matrix[r][c];
            }
        }
        
        //set the matrix to the new rotated matrix
        this.matrix = newmatrix;
        
        //gravity effect
        
        //iter throw rows backwards ignore row 1 because it is own bottom
        for(int r=this.matrixsize-2; r>=0; r--) {
            //iter through colums order shouldn't matter
            for(int c=0; c<this.matrixsize; c++) {
                
                if(this.matrix[r][c] == '.') {
                    continue;
                }
                
                int rowdrop = 0; //how many rows to drop
                for(int lowspot=r+1; (lowspot)<this.matrixsize && this.matrix[lowspot][c] == '.'  ; lowspot++) {
                    rowdrop++;
                }
                if(rowdrop > 0) {
                    this.matrix[r+rowdrop][c] = this.matrix[r][c];
                    this.matrix[r][c] = '.';
                }
                    
            }
        }
        
    }
    
    //stringify  we will probably only use this for testing but good to have
    @Override public String toString() {
        
        StringBuilder result = new StringBuilder();
        
        //iterate over rows
        for(int r=0; r<this.matrixsize; r++) {
            //iterate over columns
            for(int c=0; c< this.matrixsize; c++) {
                result.append(this.matrix[r][c]);
            }
            result.append(System.lineSeparator());
        }
        
        return result.toString();
    }
    
}
