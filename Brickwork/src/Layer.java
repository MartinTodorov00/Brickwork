import java.util.*;

public class Layer {
    private HashMap<Integer, Brick> layerContents = new HashMap<>(); //hashmap holing brickNumber and Brick Object

    public Layer() { //empty constructor for layer object

    }

    public void addBrick(Brick brick) { //add brick if not already in hashmap
        if (!layerContents.containsKey(brick.getNumber())) { //check if Key(brick number) is in hashmap
            layerContents.put(brick.getNumber(), brick);
        }
    }

    public boolean compareBrickToLayer(Brick brick) { //compare brick to entire layer
        for (Map.Entry<Integer, Brick> set: layerContents.entrySet()) { //loop through hashmap
            if (set.getValue().equals(brick)) { //if hashmap brick equals compared brick (same object or coordinates match)
                return true;
            }
        }
        return false; //when false brick can be placed on next layer
    }

    public Brick getBrick(int brickNumber) { //method returning Brick object via specified number
        return layerContents.get(brickNumber);
    }
    public HashMap<Integer, Brick> getLayerContents() { //return Layer's container object (hashmap)
        return layerContents;
    }
    public boolean containsBrick(int brickNumber) { //check if HashMap contains brick with X number
        if (layerContents.containsKey(brickNumber)) {
            return true;
        }
        return false;
    }

    public void printLayer() {
        for (Map.Entry<Integer, Brick> set: layerContents.entrySet()) { //loop through hashmap
            System.out.println(set.toString()); //prints out entire layer brick by brick (used for testing only)
        }
    }
}