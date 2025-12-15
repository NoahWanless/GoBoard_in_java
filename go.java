import java.util.Scanner;
import java.text.ListFormat.Style;
import java.util.ArrayList;



    /* 
     * Board key:
     * 0 = blank
     * 1 = white stone
     * 2 = black stone
     * 3 = blank space that belongs to white (through a capture)
     * 4 = blank space that belongs to black (through a capture)
     * 
     */


public class go {

    static int[][] board; 
    static int[][] isconsideredboard; 
    static int[][] isconsideredboard2;  //another is considered board for other purposes 
    static int N; //size of board

    public go(int n){
        board = new int[n][n];
        isconsideredboard = new int[n][n];
        isconsideredboard2 = new int[n][n];
        N = n;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                board[i][j] = 0;
                isconsideredboard[i][j] = 0; //a 0 here means it has not been considered
                isconsideredboard2[i][j] = 0; //a 0 here means it has not been considered
            }
        }
    }
    // Display function, that handles the formating of the baord
    public static void display(){
        System.out.print("  ");
        for(int k = 0; k < N; k++){
            System.out.printf("%4s",k);
        }
        System.out.println("");
        System.out.print(" ");
        System.out.println();

        for(int i = 0; i < N; i++){
            System.out.printf("%3s",i); //prints top row display 
            System.out.print("  ");
            for(int j = 0; j < N; j++){ //then specfic things for spacing stuff
                if(board[i][j] == 0 || board[i][j] == 4 || board[i][j] == 3) System.out.print("+"); else System.out.print(board[i][j]); //displays the corssess
                //System.out.print(board[i][j]);
                if(j < N-1){
                    System.out.print(" - ");
                }
                
            }
            System.out.println();
            System.out.print("  ");
            if(i < N-1){ //the vertical lines for the space
                for(int k = 0; k < N; k++){
                    System.out.printf("%4s","|");
                }
            }
            
            System.out.println();
            
        }
    }
    //function that attempts to place a stone
    //if fails returns false, true otherwise
    //this is just the intial check, ie is that place have a stone already
    public static boolean placeStone(int x, int y, int player){ //player is either 1 or 2, and thus shows who it is, boolen returns whether the placement was sucessful
        if(player != 1 && player != 2 ){ //error if player passed in is incorrect
            throw new java.lang.IndexOutOfBoundsException("Player was passed in as something other then 1 or 2 in the placeStone function");
        }
        try{
            if(board[x][y] == 0){
                board[x][y] = player;
                return true;
            }else{
                return false;
            }
        } catch(Exception IndexOutOfBoundsException){ //if out of bounds then throws error
            return false;
        }
        
    }
    //this function chekcs how many liberties a spot has, and returns all those points that are liberties,
    // you give it what point to consider
    public int[][] howManyLiberties(int x, int y){
        int count = 0;
        int[][] points = new int[4][2];
        //System.out.println(points[0][0]);
        int c = 0;

        if(x < N-1 && board[x+1][y] == 0){ //if a spot to the right,left,above or bellow a stone is blank, returns that point as a liberty and if that spot exsits (ie not on the edge)
            count++;
            points[c][0] = x+1;
            points[c][1] = y;
            c++; 
        }
        if(x > 0 && board[x-1][y] == 0){
            count++;
            points[c][0] = x-1;
            points[c][1] = y;
            c++;
            
        }
        if(y < N-1 && board[x][y+1] == 0){
            count++;
            points[c][0] = x;
            points[c][1] = y+1;
            c++;
            
        }
        if(y > 0 && board[x][y-1] == 0){
            count++;
            points[c][0] = x;
            points[c][1] = y-1;
            c++;
        }
        return points; //returns aviable liberities
    }
    //counts how many of a stone neighbors is the same color as it
    public int[][] neighborsOfsameColor(int x, int y){ 
        int[][] points = new int[4][2];
        int count = 0;
        int c = 0;
        if(x < N-1 && board[x+1][y] == board[x][y]){ //if the color of a stone to the right,left,bottom, or top matchs then adds to the points to return
            count++;
            points[c][0] = x;
            points[c][1] = y;
            c++;
        }
        if(x > 0 && board[x-1][y] == board[x][y]){
            count++;
            points[c][0] = x;
            points[c][1] = y;
            c++;
        }
        if(y < N-1 && board[x][y+1] == board[x][y]){
            count++;
            points[c][0] = x;
            points[c][1] = y;
            c++;
        }
        if(y > 0 && board[x][y-1] == board[x][y]){
            count++;
            points[c][0] = x;
            points[c][1] = y;
            c++;
        }
        return points;
    }


    // this takes the current state of the game, takes a point and checks whether anything has been captured 
    public ArrayList<int[]> StonescapturedFromintialPoint(int x, int y){ //checks to see how many points have been captured from a strating point and returns a list of them
        ArrayList<int[]> points_to_consider = new ArrayList<int[]>(); //list of points to consider
        ArrayList<int[]> points_captured = new ArrayList<int[]>(); //list of stones captured
        int[] point = new int[2]; point[0] = x; point[1] = y;
        points_to_consider.add(point); //adds our inital point to the list to consider
        while(!points_to_consider.isEmpty()){ //while there are points to consider
            int tempx = points_to_consider.get(0)[0];
            int tempy = points_to_consider.get(0)[1];
            int[][] temp =  howManyLiberties(tempx,tempy); //gets all the potenial liberites of the point
            if (howManyLiberties(tempx,tempy)[0][0] == 0 && howManyLiberties(tempx,tempy)[0][1] == 0 && howManyLiberties(tempx,tempy)[1][0] == 0 && howManyLiberties(tempx,tempy)[1][1] == 0){ 
                //if there are no liberites, then go onwards
                int[] p = new int[2];
                p[0] = tempx;
                p[1] = tempy;
                points_captured.add(p); //as this point as been shown to have no liberites, gets added to the list of stons potentially captured 
                //----------------------------------------------
                /*
                 * bc we know then that this point has no liberites, we check 1) does it have any neigbhors of the same color, 2) if it does, and they havent been considered
                 * alreday, add them to the list of points to consider, else move on
                 */
                int[][] neighbors = neighborsOfsameColor(tempx,tempy); //all neighbor of the same color 
                boolean isPointcaptured = false; //
                if(neighbors[0][0] != 0 && neighbors[1][0] != 0 &&neighbors[0][1] != 0 &&neighbors[1][1] != 0){ //only if neighbors of the same color are found, does it do the bellow
                    for(int i = 0; i < neighbors.length; i++){ //runs through all the neighbors of the same color,
                        for(int j = 0; j < points_captured.size(); j++){ //this checks to see if its in the points already captured list,
                            if(points_captured.get(j) == neighbors[i]){  //if its already been catured, and ispointcaptured set to true, this means it isnt added to the points to consider
                                j = points_captured.size() + 100; // and we move on to the next neighbor to consider if its alreadty been captured
                                isPointcaptured = true;
                            }
                        }
                        if(!isPointcaptured){ 
                            points_to_consider.add(neighbors[i]);
                        }
                    }
                }
                points_to_consider.remove(0); //removes the first index (which is the point we jsut considered )
            } else{ //if there are liberites, then end early and return a empty list
                ArrayList<int[]> empty = new ArrayList<int[]>();
                return empty;
            }
        }
        return points_captured;

        
    }
    //does the actual capturing of stones for every turn
    //places the stone updating the board, then checks if the stone placed would be captured from the intial placement
    // then checks if it captures any stone
    public void capturePoints(int x, int y){ 
        //___________________ This chunk is to check if a point just placed would be captured
        ArrayList<int[]> pottemp =  StonescapturedFromintialPoint(x,y);
        if(!pottemp.isEmpty()){ //if it shows that the point placed would be captured
            int colortemp = 0;
            if(board[x][y] == 1){
                colortemp = 4;
            } else {
                colortemp = 3; 
            }
            for(int i = 0; i < pottemp.size(); i++){ //captures all the points 
                board[pottemp.get(i)[0]][pottemp.get(i)[1]] = colortemp;
            }
            return;
        }
        //___________________ else we now check, does the stone we jsut placed capture other points?
        int colorWhogetsPoints = board[x][y]; //
        ArrayList<int[]> points  = new ArrayList<int[]>();
        if (x < N- 1 &&  board[x+1][y] != colorWhogetsPoints){ //goes through the 4 case of whether the stone to the right left up and down from it are of different color and are now captured
            ArrayList<int[]> temp = StonescapturedFromintialPoint(x+1,y); //list of points captured via the capture of the point to the right
            for(int i = 0; i < temp.size(); i++){ //if they are, those points get added to a super list of points captured for this turn
                points.add(temp.get(i));
            }
        }
        if (x > 0 &&  board[x-1][y] != colorWhogetsPoints){
            ArrayList<int[]> temp = StonescapturedFromintialPoint(x-1,y);
            for(int i = 0; i < temp.size(); i++){
                points.add(temp.get(i));
            }
        }
        if (y < N- 1 &&  board[x][y+1] != colorWhogetsPoints){
            ArrayList<int[]> temp = StonescapturedFromintialPoint(x,y+1);
            for(int i = 0; i < temp.size(); i++){
                points.add(temp.get(i));
            }
        }
        if (x > 0 &&  board[x][y-1] != colorWhogetsPoints){ 
            ArrayList<int[]> temp = StonescapturedFromintialPoint(x,y-1);
            for(int i = 0; i < temp.size(); i++){
                points.add(temp.get(i));
            }
        }
        int color = 0;
        if(colorWhogetsPoints == 1){
            color = 3;
        } else{
            color = 4;
        }
        for(int i = 0; i < points.size(); i++){ //nows makes the edits to the matrix to show that the points have been capturede
            board[points.get(i)[0]][points.get(i)[1]] = color;
        }
    }
    //recursive function for getting the size of a blank space for score purposes
    public int blanksize(int x, int y){
        if(x > N - 1 || y > N - 1 || y < 0 || x < 0){ //if out of bounds, doesnt add to the score
            return 0;
        }
        if(board[x][y] != 0){ //if not blank, doesnt add
            return 0;
        }
        if(isconsideredboard[x][y] == 1){ //if a blank spot already considered, return nothing
            return 0;
        }
        isconsideredboard[x][y] = 1; //marks the point as considered
        return 1 + blanksize(x+1,y) + blanksize(x-1,y) + blanksize(x,y+1) + blanksize(x,y-1);  //recursivly returns the size
    }
    // in the blank spot checks who this 'spot' belongs to for scoring purposes for the final score of the game
    public int whodoesblankbleong(int x, int y){ //!1 is white, 2 is black, 0 is edge, 3 is netural
        /*
         * how this works: recusively checks all points outside of the orgin and checks to see what the boundry of this blank space is 
         * if its all the same then will count towards someones score, else its netural 
         */

        //returns who owns the blank space, with the above key, this is a recursive function
        if(x > N - 1 || y > N - 1 || y < 0 || x < 0){ //if out of bounds, returns egde 
            return 0;
        }
        if(board[x][y] != 0){ //if not a blank space, returns that color
            return board[x][y];
        }
        if(isconsideredboard2[x][y] == 1) return 0; //if the point is already considered return 0
        isconsideredboard2[x][y] = 1; //else mark it as considered
        int[] colors = new int[4];
        //_________________it will now check the points around it to see if they have the same boundry  will store in the array color
        for(int i = 0; i < 4; i++) colors[i] = 7; 
        int counter = 0;
        if(whodoesblankbleong(x+1,y) ==3 ||whodoesblankbleong(x-1,y) ==3 ||whodoesblankbleong(x,y-1) ==3 ||whodoesblankbleong(x,y+1) ==3) return 3; //if any of the other points return 3 (meaning hte blank is netural, then also returns 3)
        if(whodoesblankbleong(x+1,y) !=0){ colors[counter] = whodoesblankbleong(x+1,y); counter++; }//now records what the color of the 4 points around it are(or what they returned)
        if(whodoesblankbleong(x-1,y) !=0){ colors[counter] = whodoesblankbleong(x-1,y); counter++; }
        if(whodoesblankbleong(x,y+1) !=0){ colors[counter] = whodoesblankbleong(x,y+1); counter++;}
        if(whodoesblankbleong(x,y-1) !=0){colors[counter] = whodoesblankbleong(x,y-1); counter++;}
        
        for(int i = 0; i < counter; i++) { //now checks to see if all the colors returned are the same, if not, returns 3, else returns the color itself
            for(int j = 0; j < counter; j++) {
                if(colors[i] !=colors[j]) return 3;
            }
        }
        return colors[0];
        
    }
    //actually counts the final score of the game when the game is done 
    public int[] finalScore(){ 
        int[] sco = new int[2]; //index 0 is whites score, index 1 is blacks score
        sco[0] = 0;
        sco[1] = 0;
        for(int i = 0; i < N; i++){ 
            for(int j = 0; j < N; j++){ 
                if(board[i][j] != 0 ){ //marks the point as considered
                    isconsideredboard[i][j] = 1;
                } 
                if(board[i][j] == 3 ){ //first goes through and counts all all 'blank' spots taht have been captured
                    sco[0] +=1;
                } else if(board[i][j] == 4){
                    sco[1] +=1;
                }
            }
        }
        //____________________now goes through and checks the blank spaces
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){ //for all points, only checks if blank and not considered
                if(isconsideredboard[i][j] == 0 && board[i][j] == 0){ 
                    System.out.println("We found a blank space, size:");
                    int size = blanksize(i, j); //gets the size of the blank spot
                    System.out.println(size);
                    System.out.println("Color");
                    int color = whodoesblankbleong(i,j); //gets who it belongs to 
                    System.out.println(color);
                    if(color == 1){
                        sco[0] =sco[0] + size; //gives the score out accoridngly
                    } else if( color == 2){
                        sco[1] = sco[1] + size;
                    }
                }  
            }
        }
        return sco;
    }


//Actual game play loop
    public static void main(String[] args){
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Hello, welcome to the game of Go, enter two integers within the bounds of the board size");
        System.out.println("If youd like to stop the game, enter -1 twice");
        go nG = new go(10); //makes the board 
        nG.display(); //displyas 
        Scanner scan = new Scanner(System.in);
        int counter = 0;
        boolean playerturn = true; //flipper for whos turn it is
        int color = 1;
        while(true){ //game loop 
            int moveX = scan.nextInt();
            int moveY = scan.nextInt();
            if (moveX == -1 && moveY == -1){
                System.out.println("Game over!");
                break;
            }
            if(playerturn){ //flips whos turn it is 
                playerturn = false;
                color = 1;
            } else{
                playerturn = true;
                color = 2;
            }
            while(!placeStone(moveX, moveY, color)){ //places the stone
                System.out.println("You cant place a piece there!");
                moveX = scan.nextInt();
                moveY = scan.nextInt();
            } 
            
            nG.capturePoints(moveX, moveY); //checks for captures
            nG.display(); //displays the result
            counter++;
            if(counter > N*N){ //once counter passes this point the game ends automatically
                break;
            }
        }
        System.out.println("Here is the final score:");
        int[] score = nG.finalScore();
        System.out.print("White's score:");
        System.out.println(score[0]);
        System.out.print("Black's score:");
        System.out.println(score[1]);
        
        scan.close();
        nG.display();
    }



}   