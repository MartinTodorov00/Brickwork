import java.util.Scanner;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in); //console input
    private static int brickCount, n, m; //amount of bricks in use, columns, rows
    private static int[][] firstlayer; //layer1 2d array (given by input) - serves as a "logical matrix" upon which Layer and Brick objects work

    public static void main(String[] args) { //main

        System.out.println("Layer 1 (input)");
        String[] input = scanner.nextLine().split(" "); //input to array
        m = Integer.parseInt(input[0]); //get layer1 rows
        n = Integer.parseInt(input[1]); //get layer1 columns

        if (!checkDimension(m) || !checkDimension(n)) { //checks M x N dimensions
            System.out.println("Incorrect dimensions");
            System.exit(-1);
        }

        brickCount = n * m / 2;

        try {
            fillLayerOneArray(); //try to load input to 2d array
        } catch (Exception e) {
            System.out.println(e.getMessage()); //if input is incorrect throw exception message
            System.exit(-1);
        }
        System.out.println(); //line spacing

        if (!checkLayerOneBricks()) { //check for bricks that aren't 2x1
            System.out.println("Incorrect brick!");
            System.exit(-1);
        }

        Layer layer1 = fillLayer(firstlayer); //instantiate layer1 using populateLayer(array[][]) method

        Layer layer2 = new Layer(); //instantiate new layer2 to hold solution bricks
        int[][] layer2Array = new int[m][n]; //instantiate new array (full of 0's) for "logical matrix" for layer2

        int brickNum = 1; //start layer2 with brick 1


        // this block is implemented before 'main' brick allocating algorithm (top left to bottom right) because
        // IF the right-most, unallocated brick is vertical, it may output wrong solutions (solvable configurations outputting "0")
        for (int i = 0; i < m - 1; i += 2) { //go through all rows
            for (int j = n - 1; j >= 1; j -= 2) { //go through all columns
                if (firstlayer[i][j] == firstlayer[i + 1][j]) { //if rightmost brick of layer1 is vertical DO HORIZONTAL FIRST THERE, go back left
                    int[] a = new int[2]; //instantiate coord vector a
                    int[] b = new int[2]; //instantiate coord vector b
                    if (layer2Array[i][j] == 0 && layer2Array[i + 1][j] == 0) { //if layer2Array (logical matrix) has 0 at brick placement
                        a[0] = i;
                        a[1] = j - 1;
                        b[0] = i;
                        b[1] = j; //allocate brick coordinates
                        if (!layer1.compareBrickToLayer(new Brick(brickNum, a, b))) { //compare layer1 bricks to current brick
                            layer2.addBrick(new Brick(brickNum, a, b)); //add brick to layer2
                            layer2Array[i][j - 1] = layer2Array[i][j] = brickNum; //update brick in layer2array
                            brickNum++; //increment brickNumber up
                        }
                        a = new int[2]; //instantiate new coord vector a
                        b = new int[2]; //instantiate new coord vector b
                        a[0] = i + 1;
                        a[1] = j - 1;
                        b[0] = i + 1;
                        b[1] = j; //allocate brick coordinates
                        if (!layer1.compareBrickToLayer(new Brick(brickNum, a, b))) { //compare layer1 bricks to current brick
                            layer2.addBrick(new Brick(brickNum, a, b)); //add brick to layer2
                            layer2Array[i + 1][j - 1] = layer2Array[i + 1][j] = brickNum; //update brick in layer2array
                            brickNum++;
                        }
                    }
                }
            }
        }

        while (brickNum <= brickCount) { //go through all brick numbers, starting from current reached via the first allocation above

            int[] a = new int[2]; //instantiate coord vector a
            int[] b = new int[2]; //instantiate coord vector b

            for (int i = 0; i < m; i++) { //go through all rows
                for (int j = 0; j < n; j++) { //go through all columns
                    if (layer2.containsBrick(brickNum)) { //if brick already allocated (just-in-case code block), continue
                        continue;
                    }
                    if (j + 1 != n) { //try horizontal placement
                        a[0] = i;
                        a[1] = j;
                        b[0] = i;
                        b[1] = j + 1;   //allocate brick coordinates
                        if (layer2Array[i][j] == 0 && layer2Array[i][j + 1] == 0) { //if layer2Array has 0 at brick placement
                            if (!layer1.compareBrickToLayer(new Brick(brickNum, a, b))) { //compare layer1 bricks to current brick
                                layer2.addBrick(new Brick(brickNum, a, b)); //add brick to layer2
                                layer2Array[i][j] = layer2Array[i][j + 1] = brickNum; //update brick in layer2array
                                continue;
                            }
                        }
                    }
                    //do vertical placement if horizontal placement impossible
                    a[0] = i;
                    a[1] = j;
                    b[0] = i + 1;
                    b[1] = j; //allocate brick coordinates
                    if (layer2Array[i][j] == 0 && layer2Array[i + 1][j] == 0) { //if layer2Array has 0 at brick placement
                        if (!layer1.compareBrickToLayer(new Brick(brickNum, a, b))) { //compare layer1 bricks to current brick
                            layer2.addBrick(new Brick(brickNum, a, b)); //add brick to layer2
                            layer2Array[i][j] = layer2Array[i + 1][j] = brickNum; //update brick in layer2array
                            continue;
                        }
                    }
                }
            }
            brickNum++;
        }

        for (int[] row : layer2Array) { //for each row
            for (int num : row) { //for each column in each row
                if (num == 0) { //if layer2Array (logical matrix) contains a 0, exit program (no solution)
                    System.out.println("-1\n No solution exists");
                    System.exit(-1);
                }
            }
        }

        System.out.println("Layer2:\n");


        for (int[] row : layer2Array) { //for each row
            for (int num : row) { //for each column in each row
                System.out.printf("%d ", num); //print brick numbers separated by a single space
            }
            System.out.println(""); //new line at end of column loop
        }
        System.out.println(); //line spacing

        String[][] output = new String[2 * m + 1][2 * n + 1]; //2d array for formatted output (2*dimension+1 so each brick number is surrounded by 1-symbol width walls)
        for (int i = 0; i < 2 * m + 1; i++) { //go through all rows
            for (int j = 0; j < 2 * n + 1; j++) { //go through all columns
                output[i][j] = "*"; //populate string array with *
            }
        }
        HashMap<Integer, Brick> layer2Bricks = layer2.getLayerContents(); //get layer2 brick container (hashmap)

        for (Map.Entry<Integer, Brick> set : layer2Bricks.entrySet()) { //go through all K-V pairs
            int currentBrickNumber = set.getValue().getNumber(); //get current hashmap K-V pair's value's number
            int[] currentBrickPosition = set.getValue().getPosition(); //get current brick position vector [x1,y1,x2,y2]
            int x1 = currentBrickPosition[0]; //allocate current Brick's a[0] - x1
            int y1 = currentBrickPosition[1]; //allocate current Brick's a[1] - y1
            int x2 = currentBrickPosition[2]; //allocate current Brick's b[0] - x2
            int y2 = currentBrickPosition[3]; //allocate current Brick's b[1] - y2

            if (currentBrickPosition[0] == currentBrickPosition[2]) { //if brick horizontal
                output[x1 * 2 + 1][y1 * 2 + 1] = String.valueOf(currentBrickNumber); //place first half of brick
                output[x1 * 2 + 1][y1 * 2 + 2] = "\u2506"; //place symbol between brick (unicode light triple vertical dash)
                output[x2 * 2 + 1][y2 * 2 + 1] = String.valueOf(currentBrickNumber);
                continue;
            }
            if (currentBrickPosition[1] == currentBrickPosition[3]) { //if brick vertical
                output[x1 * 2 + 1][y1 * 2 + 1] = String.valueOf(currentBrickNumber); //place first half of brick
                output[x1 * 2 + 2][y1 * 2 + 1] = "\u2508"; //place symbol between brick (unicode light quad horizontal dash)
                output[x2 * 2 + 1][y2 * 2 + 1] = String.valueOf(currentBrickNumber);
                continue;
            }
        }

        for (int i = 0; i < 2 * m + 1; i++) {
            for (int j = 0; j < 2 * n + 1; j++) {
                if (brickCount < 10) { //print formatted output according to highest number (idea is to keep numbers and stars relatively in-line
                    System.out.printf("%-1s%1s", output[i][j], ""); //print formatted number with 1 space leading and trailing
                } else if (brickCount > 9 && brickCount < 100) {
                    System.out.printf("%-2s%2s", output[i][j], ""); //print formatted number with 2 space leading and trailing
                } else if (brickCount > 99 && brickCount < 1000) {
                    System.out.printf("%-3s%3s", output[i][j], ""); //print formatted number with 3 space leading and trailing
                } else {
                    System.out.printf("%-4s%4s", output[i][j], ""); //print formatted number with 4 space leading and trailing (highest possible brick is 5000)
                }
            }
            System.out.print("\n"); //new line for next row
        }

    }

    public static boolean checkDimension(int dimension) { //check dimensions (between 0 and 100)
        if (dimension <= 100 && dimension > 0 && dimension % 2 == 0) {
            return true;
        }
        return false;
    }

    public static void fillLayerOneArray() throws ArrayIndexOutOfBoundsException { //Fill first layer from user Input, throws out of bounds exception
        firstlayer = new int[m][n]; //initialize firstlayer 2d array as M x N size!
        for (int i = 0; i < m; i++) { //go through each row
            String[] rows = scanner.nextLine().split(" "); //split each row into elements of an array
            if (rows.length > n) {
                throw new ArrayIndexOutOfBoundsException("Layer 1 input incorrect!"); //if columns too long, throw exception
            }
            for (int g = 0; g < n; g++) {
                if (Integer.parseInt(rows[g]) == 0) {
                    throw new ArrayIndexOutOfBoundsException("Can't contain fields missing bricks!");
                }
                firstlayer[i][g] = Integer.parseInt(rows[g]); //populate all columns at row[i]
            }
        }
    }

    public static boolean checkLayerOneBricks() { //check if bricks are 1x2
        ArrayList<Integer> countList = new ArrayList<>(); // create arraylist to hold all values from 2d array
        for (int[] row : firstlayer) {
            for (int col : row) {
                countList.add(col); //fill arraylist
            }
        }

        for (int i = 1; i <= brickCount; i++) {  //loop through all possible bricks
            int count = 0; //counter for every brick number occurrence
            for (int g : countList) { //loop through arraylist
                if (g == i) {
                    count++; //counter+ if match found
                }
            }
            if (count != 2) {
                return false;
            }
        }
        return true;
    }

    public static Layer fillLayer(int[][] array) { //method that takes a 2d array as input and populates a Layer object with Brick objects
        Layer layer = new Layer(); //create & instantiate layer object
        for (int brickNumber = 1; brickNumber <= brickCount; brickNumber++) { //go through all bricks to populate Layer with bricks
            int[] a = {-1, -1}; //instantiate a[2]
            int[] b = {-1, -1}; //instantiate b[2]
            /* NB:
             * a[] and b[] are instantiated as -1, because the top left element of the matrix is 0,0
             * if they get instantiated without values (a[0]=a[1]..==0)
             * and the top left horizontal brick would get a={0,1}, b{0,0} which throws off the Brick.equals Override
             */
            for (int i = 0; i < m; i++) { //go through each row
                for (int j = 0; j < n; j++) { //go through each column
                    if (brickNumber == array[i][j]) { //if current brick number matches the matrix number
                        if (a[0] == -1 && a[1] == -1) { //first allocate a[] (x1y1) coordinate
                            a[0] = i;
                            a[1] = j;
                        } else if (b[0] == -1 && b[1] == -1) { //if a[] is already allocated for this brick, allocate b[](x2y2)
                            b[0] = i;
                            b[1] = j;
                        }
                    }
                }
            }

            layer.addBrick(new Brick(brickNumber, a, b)); //add new Brick(number, x1y1, x2y2)
        }
        return layer;
    }

}