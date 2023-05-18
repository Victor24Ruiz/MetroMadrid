package practica8;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Metro {
	
	protected class InfoLineaMetro {
		protected String nombre;  //Nombre de la línea de metro
		protected String color;   //Color asociado a la línea de metro
		protected List<String> paradas;
	}
	private HashMap<String,InfoLineaMetro> lineasMetro; //información de cada linea
	private EDTreeGraph<String, String> grafoMetro;

	
	public Metro(String filename) {
		lineasMetro = new HashMap<>();
		grafoMetro = new EDTreeGraph<>(false, true); //grafo no dirigido
		leerGrafo(filename);
	}
	
	//Lee el fichero con la informaci�n de las lineas de metro
	private void leerGrafo (String nomfich) {
		Scanner inf=null;
		try {
			inf = new Scanner (new FileInputStream(nomfich));
		} catch(FileNotFoundException e){
			System.out.println("Error al abrir el fichero");
			System.exit(0);
		}
		int NLineasMetro=inf.nextInt(); //numero de lineas de metro

		for (int i=0; i<NLineasMetro; i++) {  //Para cada linea
			String lineaId = inf.next();  //linea, etiqueta arcos
			InfoLineaMetro linea=new InfoLineaMetro();
			linea.color = inf.next();
			linea.nombre = inf.next();
			//System.out.println("color "+linea.color + " nombre "+linea.nombre);
			int nparadas = inf.nextInt(); //numero de paradas de esa linea
			//System.out.println("nparadas "+nparadas);
			inf.nextLine();
			linea.paradas=new ArrayList<>();
			String origen = inf.nextLine();
			linea.paradas.add(origen.toLowerCase());  //se añade a la información de la línea
			if (!grafoMetro.containsNode(origen.toLowerCase()))  //Se añade al grafo si no está
				grafoMetro.insertNode(origen.toLowerCase());

			String dest;
			for (int j=1; j<nparadas; j++ ) {
				dest = inf.nextLine();
				linea.paradas.add(dest.toLowerCase());  //se añade a las paradas de la línea
				if (!grafoMetro.containsNode(dest.toLowerCase()))
					grafoMetro.insertNode(dest.toLowerCase());  //Se añade al grafo si no está

				grafoMetro.insertEdge(origen.toLowerCase(),dest.toLowerCase(),lineaId); //Se añade el arco al grafo
				origen=dest;
			}
			this.lineasMetro.put(lineaId,linea);
		}
		inf.close();
	}

	public void printMetro() {
		for (String linea:this.lineasMetro.keySet()) {
			InfoLineaMetro infolinea=this.lineasMetro.get(linea);
			System.out.println (linea+" "+infolinea.color+" "+infolinea.nombre+" estaciones: ");
			System.out.println(infolinea.paradas.toString());
		}
		System.out.println("Grafo del metro: ");
		this.grafoMetro.printGraphStructure();
		//Comprobaciones
		System.out.println("Numero de estaciones: "+this.grafoMetro.getKeys().size());
	}


	//Devuelve el n�mero total de estaciones del metro
	public int getNumeroEstaciones() {
		return grafoMetro.size();
	}

	
	public Set<String> getEstaciones() {
		return grafoMetro.getKeys();
	}
	
	public void printListaEstaciones() {
		Set<String> estaciones = getEstaciones();
		for (String estacion: estaciones)
			System.out.print(estacion+", ");
		System.out.println();
	}

	//Devuelve la linea que conecta directamente 2 estaciones y null si no están conectadas directamente
	public String estacionesConectadas(String origen, String destino) {
		return this.grafoMetro.getEdgeWeight(origen, destino);
	}

	//Devuelve un conjunto con los identificadores de las líneas de metro que tienen parada en esa estación
	//Si la estación no existe, devuelve null
	public Set<String> getLineasEstacion(String estacion) {
		// TO DO EJERCICIO 2
		Set<String> res = new HashSet<>();
		List<Edge<String, String>> aux = grafoMetro.getAdjacentArcs(estacion);
		if (aux != null) {
			for (int i = 0; i < aux.size(); i++) {
				if (!res.contains(aux.get(i).getWeight()))
					res.add(aux.get(i).getWeight());
			}
			return res;
		}
		return null;
	}

	//Devuelve todas las estaciones de todo el recorrido de una linea dado su identificador 
	//Si dir=='f', las devolverá en sentido almacenado, si no, en sentido contrario
	//Si la línea no existe, devuelve null
	public List<String> getRecorridoLinea(String idlinea, char dir) {
		// TO DO EJERCICIO 3
		List<String> res = new ArrayList<>();
		for (String linea : lineasMetro.keySet()) {
			if (linea.equals(idlinea)) {
				List<String> info = lineasMetro.get(idlinea).paradas;
				if (dir == 'f') {
					Iterator<String> it = info.iterator();
					while (it.hasNext())
						res.add(it.next());
				} else {
					ListIterator<String> it = info.listIterator(info.size());
					while (it.hasPrevious())
						res.add(it.previous());
				}
				break;
			}
		}
		if (res.isEmpty())
			return null;
		return res;
	}

	// Determina si dos estaciones se encuentran en la miusmo línea. Si es así devolver el código de la línea.
	// En caso contrario deveolverá null
	public String mismaLinea (String origen, String destino) {
		// TO DO EJERCICIO 4
		Set<String> lineasOrigen = getLineasEstacion(origen);
		Set<String> lineasDestino = getLineasEstacion(destino);
		if (lineasOrigen != null && lineasDestino != null) {
			Iterator<String> itOrigen = lineasOrigen.iterator();
			while (itOrigen.hasNext()) {
				String string = itOrigen.next();
				if (lineasDestino.contains(string))
					return string;
			}
		}
		return null;
	}


	// Calcula la ruta entre la estación origen y la estación destino. Devuelve una lista
	// de arcos con la siguiente estación en la ruta y la línea por la que se llega.
	//Si alguna de las dos estaciones (origen/destino) no existe, devuelve una lista vacía
	//Para calcular la ruta: si las estaciones están en el recorrido de una misma línea de metro,
	//se devolverá esa línea. En caso contrario, se buscará la ruta con menor número de paradas.

	public List<Edge<String, String>> ruta(String origen, String destino) {
		// TO DO EJERCICIO 5
		if (grafoMetro.containsNode(origen) && grafoMetro.containsNode(destino)) {
			List<Edge<String, String>> lista = new LinkedList<>();
			String linea = mismaLinea(origen, destino);
			if (linea != null) {
				List<String> recorrido = getRecorridoLinea(linea, 'f');
				int inicio = recorrido.indexOf(origen);
				int fin = recorrido.indexOf(destino);
				ListIterator<String> it = recorrido.listIterator(inicio);
				Edge<String, String> edge = new Edge(origen, linea);
				lista.add(edge);
				if (inicio <= fin) {
					int pos = inicio;
					while (it.hasNext() && pos <= fin) {
						edge = new Edge(it.next(), linea);
						if (!lista.contains(edge))
							lista.add(edge);
						pos++;
					}
				} else {
					int pos = inicio;
					while (it.hasPrevious() && pos > fin) {
						edge = new Edge(it.previous(), linea);
						if (!lista.contains(edge))
							lista.add(edge);
						pos--;
					}
				}
			} else {
				Set<String> estaciones = getEstaciones();
				Iterator<String> it = estaciones.iterator();
				List<String> list = new LinkedList<>();
				while (it.hasNext())
					list.add(it.next());
				int inicio = list.indexOf(origen);
				int fin = list.indexOf(destino);
				List<String> res = new LinkedList<>();
				if (inicio != -1 && fin != -1) {
					int[] v = bfs(inicio, fin, list);
					Stack<Integer> pila = new Stack<>();
					if (v[fin] == -1)
						return null;
					int last = fin;
					pila.push(last);
					while (last != v[last]) {
						last = v[last];
						pila.push(last);
					}
					while (!pila.isEmpty()) {
						int indice = pila.pop();
						res.add(list.get(indice));
					}
				}
				for (int i = 0; i < res.size() - 1; i++)
					lista.add(new Edge(res.get(i), grafoMetro.getEdgeWeight(res.get(i), res.get(i+1))));
				lista.add(new Edge(res.get(res.size() - 1), grafoMetro.getEdgeWeight(res.get(res.size() - 2), res.get(res.size() - 1))));
			}
			return lista;
		}
		return null;
	}

	public int[] bfs(int start, int end, List<String> list) {
		int[] camino = new int[getNumeroEstaciones()];
		for (int i = 0; i < camino.length; i++)
			camino[i] = -1;
		Queue<Integer> q = new LinkedList<>();
		q.add(start);
		camino[start] = start;
		while (!q.isEmpty() && camino[end] == -1) {
			int actual = q.remove();
			for (Edge<String, String> edge : grafoMetro.getAdjacentArcs(list.get(actual))) {
				String neighbor = edge.getTarget();
				int ady = list.indexOf(neighbor);
				if (camino[ady] == -1) {
					camino[ady] = actual;
					q.add(ady);
				}
			}
		}
		return camino;
	}
}
