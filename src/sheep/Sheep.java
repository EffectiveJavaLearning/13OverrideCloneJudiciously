package sheep;
class Sheep implements Cloneable {
    String name;

    Sheep(String name){this.name = name;}

    @Override
    public Object clone() {
        // bad, doesn't cascade up to Object
        return new Sheep(this.name);
    }
}

class WoolySheep extends Sheep {

    WoolySheep(String name) {
        super(name);
    }

    @Override
    public Object clone() {
        return super.clone();
    }
}

class Test {

    public static void main(String[] args) {
        WoolySheep dolly = new WoolySheep("Dolly");
        WoolySheep clone = (WoolySheep)(dolly.clone()); // error
    }
}