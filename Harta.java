import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Harta implements Autoturism, Bicicleta, Camion, Motocicleta {
    private int numarNoduri;
    private int numarStrazi;
    private final ArrayList<Strada> harta = new ArrayList<>(); /* Graful in care stochez strazile */

    void createHarta(Scanner input) {
        /* Ia primele 2 numere pentru numarul de strazi/noduri */
        numarStrazi = input.nextInt();
        numarNoduri = input.nextInt();

        input.nextLine(); /* Un whitespace useless */

        /* Pentru fiecare strada din map.in adaug-o in graf(harta) */
        for (int i = 0; i < numarStrazi; i++) {
            Nod start = new Nod();
            Nod end = new Nod();
            int cost, gabarit;

            /* Ia toata linia dupa formateaza continutul si adauga strada specifica */
            String[] stringSplit = input.nextLine().split("\\s+");

            start.nume = Integer.parseInt(stringSplit[0].replace("P", ""));
            end.nume = Integer.parseInt(stringSplit[1].replace("P", ""));
            cost = Integer.parseInt(String.valueOf(stringSplit[2]));
            gabarit = Integer.parseInt(String.valueOf(stringSplit[3]));

            addStreet(start, end, cost, gabarit);
        }

        /* Pentru comenzile ramase ia fiecare linie si verifica ce fel ce comanda este */
        while (input.hasNextLine()) {
            String type;
            int ambuteiaj = 0;
            Nod start = new Nod();
            Nod end = new Nod();
            String vehicleType;

            String[] stringSplit = input.nextLine().split("\\s+");
            type = String.valueOf(stringSplit[0]); /* accident, trafic, blocaj sau drive */

            /* Daca este de tipul accident, blocaj sau trafic -> addRestriction(...) */
            /* Daca este de tipul drive -> drive(...) */

            if (type.equals("accident") || type.equals("blocaj") || type.equals("trafic")) {

                ambuteiaj += Integer.parseInt(String.valueOf(stringSplit[3]));
                start.setNume(Integer.parseInt(stringSplit[1].replace("P", "")));
                end.setNume(Integer.parseInt(stringSplit[2].replace("P", "")));

                addRestriction(type, start, end, ambuteiaj);
            } else if (type.equals("drive")) {

                vehicleType = String.valueOf(stringSplit[1]); /* b, m, a sau c*/
                start.setNume(start, Integer.parseInt(stringSplit[2].replace("P", "")));
                end.setNume(end, Integer.parseInt(stringSplit[3].replace("P", "")));

                drive(vehicleType, start.getNume(), end.getNume());
            }
        }
    }

    /* Construieste strada dupa adauga strada in graf(harta) */
    private void addStreet(Nod start, Nod end, int cost, int size) {
        Strada strada = new Strada();

        strada.setStrada(start, end, cost, size);

        harta.add(strada);
    }

    /* Cauta strada in graf(harta) si adauga ambuteiajul precizat */
    /* Daca nu gaeseste strada -> nu intra pe if -> ambuteiajul ramane 0 */
    private void addRestriction(String type, Nod start, Nod end, int ambuteiaj) {
        for (int i = 0; i < numarStrazi; i++) {
            if (harta.get(i).getStart().getNume() == start.getNume() &&
                    harta.get(i).getEnd().getNume() == end.getNume()) {
                harta.get(i).ambuteiaj += ambuteiaj;
            }
        }

    }

    /* Gaseste strada specifica */
    /* Daca nu exista returneaza null */
    private Strada findStrada(int start, int end) {
        for (int i = 0; i < numarStrazi; i++) {
            if (harta.get(i).getStart().getNume() == start &&
                    harta.get(i).getEnd().getNume() == end) {
                return harta.get(i);
            }
        }
        return null;
    }

    /* Calculeaza distanta minima */
    private int minDistance(int[] dist, Boolean[] boolVect) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int k = 0; k < numarNoduri; k++)
            if (!boolVect[k] && dist[k] <= min) {
                min = dist[k];
                minIndex = k;
            }

        return minIndex;
    }

    /* Returneaza costul unei strazi */
    protected int cost(Strada strada, int cost) {
        int result;

        result = cost * strada.getCost() + strada.getAmbuteiaj();

        return result;
    }

    private void drive(String vehicleType, int src, int dest) {
        int gabarit, cost;
        Boolean[] boolVect = new Boolean[numarStrazi];
        int[] costVect = new int[numarStrazi];
        int[] parent = new int[numarStrazi];

        /* Initializarea celor 2 vectori cu false & Integer.MAX_VALUE */
        for (int i = 0; i < numarStrazi; i++) {
            costVect[i] = Integer.MAX_VALUE;
            boolVect[i] = false;
        }

        /* Calculeaza gabaritul & costul unui vehicul in functie de tip (b, m, a sau c) */
        if (vehicleType.equals("b")) {
            gabarit = Bicicleta.gabarit;
            cost = Bicicleta.cost;
        } else if (vehicleType.equals("m")) {
            gabarit = Motocicleta.gabarit;
            cost = Motocicleta.cost;
        } else if (vehicleType.equals("a")) {
            gabarit = Autoturism.gabarit;
            cost = Autoturism.cost;
        } else {
            gabarit = Camion.gabarit;
            cost = Camion.cost;
        }

        /* Distanta de la sursa trebuie sa fie mereu 0 */
        costVect[src] = 0;
        parent[src] = -1;

        /* Dijkstra-ul propriu-zis */
        for (int i = 0; i < numarStrazi; i++) {
            int index = minDistance(costVect, boolVect);

            if (index == -1) {
                continue;
            }

            boolVect[index] = true;

            for (int v = 0; v < numarStrazi ; v++) {
                Strada strada = findStrada(index, v);

                /* Daca gaseste strada */
                if (strada != null) {
                    /* Conditiile pentru Dijkstra */
                    if (!boolVect[v] && costVect[index] != Integer.MAX_VALUE &&
                           costVect[index] + cost(strada, cost) < costVect[v] &&
                            gabarit <= strada.getGabarit() ) {
                        costVect[v] = costVect[index] + cost(strada, cost);

                        parent[v] = index;
                    }
                }
            }
        }

        /* Scrie ride-ul in map.out */
        /* try & catch pentru ca poate sa arunce IOException */
        try {
            writeRide(parent, src, dest, costVect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeRide (int[] parent, int src, int dest, int[] costVect) throws IOException {
        /* Efectiv scrie ride-ul in map.out */

        int current;
        ArrayList<String> ride = new ArrayList<>();
        File file = new File("map.out");
        FileWriter fileWriter = new FileWriter(file, true);

        if (costVect[dest] != Integer.MAX_VALUE) {
            for (current = dest; current != src; current = parent[current]) {
                ride.add("P" + current);
            }

            ride.add("P" + current);

            Collections.reverse(ride);
        } else {
            ride.add("P" + src);
            ride.add("P" + dest);
        }

        for (String s : ride) {
            System.out.print(s + " ");
            fileWriter.write(s + " ");
        }

        if (costVect[dest] == Integer.MAX_VALUE) {
            System.out.println("null");
            fileWriter.write("null\n");
        } else {
            System.out.println(costVect[dest]);
            fileWriter.write(costVect[dest] + "\n");
        }

        fileWriter.close();
    }

}
