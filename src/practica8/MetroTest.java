package practica8;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.*;

public class MetroTest {
    protected class RecorridoLineas {
        protected List<String> forward;
        protected List<String> reverso;
    }

    protected class Arco {
        protected String origen, destino;
        protected String peso;
    }

    protected class Ruta {
        protected String origen, destino;
        protected List<String> secuencia;
    }

    private HashMap<String, RecorridoLineas> recorridos; //clave idLinea, recorridos
    private HashMap<String, Set<String>> estacionLineas; //clave estacion, lineas que paran
    private List<Arco> comprobarPesos;
    private List<Arco> comprobarMismaLinea;
    private List<Ruta> comprobarRutas;

    private void collectInfo() {
        Scanner inFile = null;
        try {
            inFile = new Scanner(new FileInputStream("ckecking.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Error al abrir el fichero checking.txt");
            System.exit(0);
        }
        this.recorridos = new HashMap<>();
        int nlineas = inFile.nextInt();
        //System.out.println("nLineas " + nlineas);
        for (int i = 0; i < nlineas; i++) {
            String id = inFile.next();
            int nestaciones = inFile.nextInt();
            //System.out.println("nestaciones " + nestaciones);
            inFile.nextLine();  //salto de linea
            RecorridoLineas recorrido = new RecorridoLineas();
            recorrido.forward = new LinkedList<>();
            for (int j = 0; j < nestaciones; j++) {
                String estacion = inFile.nextLine();
                //System.out.println("estacion "+estacion);
                recorrido.forward.add(estacion);
            }
            recorrido.reverso = new LinkedList<>();
            ListIterator<String> it = recorrido.forward.listIterator(recorrido.forward.size());
            while (it.hasPrevious())
                recorrido.reverso.add(it.previous());

            this.recorridos.put(id, recorrido);
        }

        //Leer estaciones y paradas
        this.estacionLineas = new HashMap<>();
        int nestaciones = inFile.nextInt();
        inFile.nextLine();
        //System.out.println("nestaciones " + nestaciones);
        for (int i = 0; i < nestaciones; i++) {
            String estacion = inFile.nextLine();
            //System.out.println("estacion " + estacion);
            Set<String> lineas = new HashSet<>();
            int n = inFile.nextInt();
            //System.out.println("n "+n);
            for (int j = 0; j < n; j++) {
                String lin = inFile.next();
                //System.out.println("lin "+lin);
                lineas.add(lin);
            }
            inFile.nextLine(); //salto de línea
            this.estacionLineas.put(estacion, lineas);
        }

        //LeerInformacion para getEdgeWeight
        this.comprobarPesos = new LinkedList<>();
        int npruebas = inFile.nextInt();
        inFile.nextLine(); //linea en blanco
        //System.out.println("npruebas "+npruebas);
        for (int i = 0; i < npruebas; i++) {
            Arco arco = new Arco();
            arco.origen = inFile.nextLine();
            arco.destino = inFile.nextLine();
            arco.peso = inFile.nextLine();
            //System.out.println("orgien, destino, peso "+arco.origen + " "+arco.destino+ " "+arco.peso);
            this.comprobarPesos.add(arco);
        }

        //LeerInformacion para mismaLinea
        this.comprobarMismaLinea = new LinkedList<>();
        npruebas = inFile.nextInt();
        //System.out.println("npurebas "+npruebas);
        inFile.nextLine(); //linea en blanco
        for (int i = 0; i < npruebas; i++) {
            Arco arco = new Arco();
            arco.origen = inFile.nextLine();
            arco.destino = inFile.nextLine();
            arco.peso = inFile.nextLine();
            //System.out.println("orgien, destino, peso "+arco.origen + " "+arco.destino+ " "+res);
            //if (res.equals("true"))
            //    arco.enLinea = true;
            //else
            //   arco.enLinea = false;
            this.comprobarMismaLinea.add(arco);
        }

        //Leer Informacion para rutas
        this.comprobarRutas = new LinkedList<>();
        npruebas = inFile.nextInt();
        //System.out.println("numero de pruebas "+npruebas);
        inFile.nextLine(); //salto de linea
        for (int i = 0; i < npruebas; i++) {
            Ruta ruta = new Ruta();
            ruta.origen = inFile.nextLine().toLowerCase();
            ruta.destino = inFile.nextLine().toLowerCase();
            //System.out.println("origen , destino "+ruta.origen + " "+ruta.destino);
            int n = inFile.nextInt();
            //System.out.println("n "+n);
            inFile.nextLine(); //salto de linea
            ruta.secuencia = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                ruta.secuencia.add(inFile.nextLine());
            }
            this.comprobarRutas.add(ruta);
        }
        inFile.close();
    }

    //TestMetro() {
    //    collectInfo();
    //}

    @org.junit.Test
    public void getEdgeWeightTest() {
        Metro metro = new Metro("metroMadrid2021.txt");
        collectInfo();

        System.out.println("Probando getEdgeWeight (source, target)");
        ListIterator<Arco> it = this.comprobarPesos.listIterator();
        while (it.hasNext()) {
            Arco arco = it.next();
            String resultado = metro.estacionesConectadas(arco.origen, arco.destino);
            System.out.println("Para getEdgeWeight (" + arco.origen + ", " + arco.destino + ") ...");
            System.out.println("Resultado esperado: " + arco.peso);
            System.out.println("Resultado obtenido: " + resultado);
            if (arco.peso.equals("null"))
                assertEquals(null, resultado);
            else
                assertEquals(arco.peso, resultado);
        }
    }

    @org.junit.Test
    public void mismaLineaTest() {
        Metro metro = new Metro("metroMadrid2021.txt");
        collectInfo();

        System.out.println("Comprobando mismaLinea(estacion1, estacion2) ");
        ListIterator<Arco> it = this.comprobarMismaLinea.listIterator();
        while (it.hasNext()) {
            Arco arco = it.next();
            String resultado = metro.mismaLinea(arco.origen, arco.destino);
            System.out.println("Para mismaLinea (" + arco.origen + ", " + arco.destino + ") ...");
            System.out.println("Resultado esperado: " + arco.peso);
            System.out.println("Resultado obtenido: " + resultado);
            if (arco.peso.equals("null"))
                assertEquals(null, resultado);
            else
                assertEquals(arco.peso, resultado);
        }
    }

    @org.junit.Test
    public void getLineasEstacionTest() {
        Metro metro = new Metro("metroMadrid2021.txt");
        collectInfo();

        System.out.println("Probando getLineasEstacion ( estacion )");
        for (String estacion : this.estacionLineas.keySet()) {
            Set<String> resultado = metro.getLineasEstacion(estacion);
            Set<String> esperado = this.estacionLineas.get(estacion);
            System.out.println("... para estación " + estacion);
            System.out.println("Resultado esperado: " + esperado.toString());
            System.out.println("Resultado obtenido: " + resultado.toString());
            assertEquals(esperado.size(), resultado.size());
            assertEquals(esperado, resultado);
        }
    }

    @org.junit.Test
    public void getRecorridoLineaTest() {
        Metro metro = new Metro("metroMadrid2021.txt");
        collectInfo();

        System.out.println("Comprobando getRecorridoLinea (linea) ");
        //probar con líneas que no existen
        List<String> resultado = metro.getRecorridoLinea("L22", 'f');
        System.out.println("Linea L22");
        System.out.println("Resultado esperado: null");
        if (resultado == null)
            System.out.println("Resultado obtenido: " + resultado);
        else
            System.out.println("Resultado obtenido: " + resultado.toString());
        assertEquals(null, resultado);

        for (String linea : this.recorridos.keySet()) {
            resultado = metro.getRecorridoLinea(linea, 'f');
            List<String> esperado = this.recorridos.get(linea).forward;
            System.out.println("Linea: " + linea);
            System.out.println("Resultado esperado: " + esperado.toString());
            System.out.println("Resultado obtenido: " + resultado.toString());
            assertEquals(esperado, resultado);

            resultado = metro.getRecorridoLinea(linea, 'r');
            esperado = this.recorridos.get(linea).reverso;
            System.out.println("Linea: " + linea);
            System.out.println("Resultado esperado: " + esperado.toString());
            System.out.println("Resultado obtenido: " + resultado.toString());
            assertEquals(esperado, resultado);
        }
    }

    @org.junit.Test
    public void rutaTest() {
        Metro metro = new Metro("metroMadrid2021.txt");
        collectInfo();

        System.out.println("Comprobando rutas\n ");

        ListIterator<Ruta> it = this.comprobarRutas.listIterator();
        while (it.hasNext()) {
            Ruta datosRuta = it.next();
            System.out.println("ruta (" + datosRuta.origen + ", " + datosRuta.destino + ")");
            List<Edge<String, String>> resultado = metro.ruta(datosRuta.origen, datosRuta.destino);
            System.out.println("esperado: " + (datosRuta.secuencia.size() == 0 ? "null " :
                    datosRuta.secuencia.toString()));
            System.out.print("obtenido: ");
            if (resultado == null)
                System.out.println("null");
            else {
                ListIterator<Edge<String, String>> it2 = resultado.listIterator();
                while (it2.hasNext()) {
                    Edge<String, String> arco = it2.next();
                    System.out.print(" -> " + arco.getTarget());
                    if (it2.hasNext())
                        System.out.print(" -> " + arco.getWeight());
                }
                System.out.println();
            }
            System.out.println();

            if (datosRuta.secuencia.size() == 0) {
                assertNull(resultado);
                continue;
                }

            ListIterator<String> it1 = datosRuta.secuencia.listIterator();
            //System.out.println("secuencia esperada "+datosRuta.secuencia.toString());
            ListIterator<Edge<String, String>> it2 = resultado.listIterator();
            while (it1.hasNext() && it2.hasNext()) {
                Edge<String, String> arco2 = it2.next();
                String estacion = it1.next();
                if (it1.hasNext()) {
                    String linea = it1.next();
                    //System.out.println("linea esperada: " + linea + " linea obtenida: " + arco2.getWeight());
                    assertEquals(linea, arco2.getWeight());
                }
                //System.out.println("estación esperada: " + estacion + " estacion obtenida: " + arco2.getTarget());
                assertEquals(estacion, arco2.getTarget());
            }


            if (it1.hasNext()) {
                System.out.println("secuencia " + it1.next().toString());
                System.out.println("Error: ruta obtenida demasiado corta");
            }
            assertFalse(it1.hasNext());
            if (it2.hasNext())
                System.out.println("Error: ruta obtenida demasiado larga");
            assertFalse(it2.hasNext());
        }
    }


}
