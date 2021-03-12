class Strada extends Harta {
    private int cost;
    private int gabarit;
    private Nod start;
    private Nod end;

    int ambuteiaj = 0;

    Nod getStart() {
        return start;
    }
    Nod getEnd() {
        return end;
    }

    int getCost() {
        return cost;
    }
    int getGabarit() {
        return gabarit;
    }
    int getAmbuteiaj() {
        return ambuteiaj;
    }

    @Override
    protected int cost(Strada strada, int cost) {
        return super.cost(strada, cost);
    }

    void setNume(Nod nod, int nume) {
        nod.setNume(nume);
    }

    void setStrada(Nod start, Nod end, int cost, int size) {
        this.cost = cost;
        this.gabarit = size;
        this.start = start;
        this.end = end;
    }
}
