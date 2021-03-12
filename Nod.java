class Nod extends Strada {
    int nume;

    int getNume() {
        return this.nume;
    }
    void setNume(int nume) {
        this.nume = nume;
    }

    @Override
    void setNume(Nod nod, int nume) {
        super.setNume(nod, nume);
    }
}
