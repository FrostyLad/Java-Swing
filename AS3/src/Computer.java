// BRADEN JOHNSTON 20005898 - 159234 AS3 2022
public class Computer {
    int price, memory, storage;
    float screen;
    String ID, category, type, brand, CPU;

    Computer(String[] computer) { // object is initialized differently depending on how many attributes are passed in
        this.category = computer[0];
        this.type = computer[1];
        this.ID = computer[2];
        this.brand = computer[3];
        this.CPU = computer[4];

        if (computer.length == 7) {
            this.screen = Float.parseFloat(computer[5]);
            this.price = Integer.parseInt(computer[6]);
        }
        if (computer.length == 8) {
            this.memory = Integer.parseInt(computer[5]);
            this.storage = Integer.parseInt(computer[6]);
            this.price = Integer.parseInt(computer[7]);
        }
        if (computer.length == 9) {
            this.memory = Integer.parseInt(computer[5]);
            this.storage = Integer.parseInt(computer[6]);
            this.screen = Float.parseFloat(computer[7]);
            this.price = Integer.parseInt(computer[8]);
        }
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public void setScreen(float screen) {
        this.screen = screen;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }
}
