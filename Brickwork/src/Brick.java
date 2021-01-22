import java.util.*;

public class Brick {

    private int number; //brick object number
    private int[] a, b; //brick object "coordinates" as 2 vectors
    private int[] positionArray; //brick position vector

    public Brick(int number, int[] a, int[] b) { //brick constructor
        this.number=number; //instantiate number
        this.a=a; //instantiate a[] vector
        this.b=b; //instantiate b[] vector
        this.positionArray=this.getPosition();
    }

    public int[] getPosition() { //return vector [x1,y1,x2,y2]
        int[] vector = new int[4]; //instantiate coordinate vector with a size of 4
        vector[0]=a[0]; //get x1
        vector[1]=a[1]; //get y1
        vector[2]=b[0]; //get x2
        vector[3]=b[1]; //get y2
        return vector;
    }

    public int getNumber() { //return brick number
        return this.number;
    }

    @Override
    public boolean equals(Object obj) { //check brick position compared to other brick
        if (this == obj) //if the instance of 'this'= instance of 'obj' return true
            return true;
        if (obj == null) //if compared object is not instantiated return false
            return false;
        if (getClass() != obj.getClass()) //if objects' class is different, return false
            return false;
        Brick other = (Brick) obj; //cast compared object as Brick
        if (!Arrays.equals(positionArray, other.positionArray)) //if x1y1,x2y2 dont match between 2 bricks, return false
            return false;
        return true;
    }

    @Override
    public String toString() { //toString override printing out number, x1y1,x2y2, used for layer.printLayer() test method
        return "Brick [number=" + number + ", a=" + Arrays.toString(a) + ", b=" + Arrays.toString(b) + "]";
    }



}