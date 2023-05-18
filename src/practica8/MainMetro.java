package practica8;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainMetro {
    public static void printMenu() {
        System.out.println("Elige una opción:");
        System.out.println("1. Ver la información del metro ");
        System.out.println("2. Imprimir el listado de estaciones");
        System.out.println("3. Imprimir el recorrido de una línea ");
        System.out.println("4. Ver las líneas que hay en una estación");
        System.out.println("5. Calcular la ruta entre 2 estaciones");
        System.out.println("0. Salir");
        System.out.println("Opcion?: ");
    }

    public static void main(String[] args) {
        Metro metro = new Metro("metroMadrid2021.txt");
        Scanner teclado = new Scanner(System.in);
        int opcion = 1;
        while (opcion != 0) {
            do {
                printMenu();
                opcion = teclado.nextInt();
                teclado.nextLine();
            } while (opcion < 0 && opcion > 5);
            if (opcion == 1) {
                metro.printMetro();
            } else if (opcion == 2) {
                Set<String> estaciones = metro.getEstaciones();
                int i = 0;
                Iterator<String> it = estaciones.iterator();
                while (it.hasNext()) {
                    int j = 0;
                    while (i < estaciones.size() && j < 8) {//8 estaciones por linea para visualización
                        System.out.print(it.next() + " - ");
                        j++;
                        i++;
                    }
                    System.out.println();
                }

            } else if (opcion == 3) {
                System.out.println("De qué línea de metro quieres el recorrido?");
                String linea = teclado.next();
                System.out.println("Teclea 'f' para ver las estaciones en sentido normal o 'r' para verlas en sentido contrario");
                String sentido = teclado.next();
                char dir = sentido.charAt(0);
                if (dir != 'f' && dir != 'r') {
                    System.out.println("Error en la introducción de datos");
                    dir = 'f';
                }
                List<String> estaciones = metro.getRecorridoLinea(linea, dir);
                if (estaciones != null)
                    System.out.println("Estaciones de la linea " + linea + ": " + estaciones.toString());
            } else if (opcion == 4) {
                //Lineas en una estación
                System.out.println("De qué estación quieres saber las líneas?");
                String estacion = teclado.nextLine();
                Set<String> lineas = metro.getLineasEstacion(estacion.toLowerCase());
                if (lineas != null)
                    System.out.println("Las lineas que se pueden coger en " + estacion + " son: " + lineas.toString());
            } else if (opcion == 5) {
                //recorrido entre 2 estaciones
                System.out.println("Estación origen del recorrido: ");
                String estacionOrigen = teclado.nextLine();
                System.out.println("Estación destino del recorrido: ");
                String estacionDestino = teclado.nextLine();
                List<Edge<String, String>> ruta = metro.ruta(estacionOrigen.toLowerCase(), estacionDestino.toLowerCase());
                //Escrbir por pantalla la ruta encontrada
                if (ruta != null) {
                    for (int i = 0; i < ruta.size() - 1; i++) {
                        System.out.print(ruta.get(i).getTarget() + " -> " + ruta.get(i).getWeight() + " -> ");
                    }
                    System.out.println(ruta.get(ruta.size() - 1).getTarget());
                }
            }
        }
    }
}
