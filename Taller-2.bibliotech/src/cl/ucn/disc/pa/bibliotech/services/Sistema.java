/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.princeton.cs.stdlib.StdIn;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * The Sistema.
 *
 * @author Programacion Avanzada.
 */
public final class Sistema {

    /**
     * Procesador de JSON.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The list of Socios.
     */
    private Socio[] socios;

    /**
     * The list of Libros.
     */
    private Libro[] libros;

    /**
     * Socio en el sistema.
     */
    private Socio socio;

    /**
     * The Sistema.
     */
    public Sistema() throws IOException {

        // no hay socio logeado.
        this.socios = new Socio[0];
        this.libros = new Libro[0];
        this.socio = null;

        // carga de los socios y libros.
        try {
            this.cargarInformacion();
        } catch (FileNotFoundException ex) {
            // no se encontraron datos, se agregar los por defecto.

            // creo un socio
            this.socios = Utils.append(this.socios, new Socio("John", "Doe", "john.doe@ucn.cl", 1, "john123"));

            // creo un libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Head First Java: A Brain-Friendly Guide", " Kathy Sierra", "Programming Languages", 0.0));

            // creo otro libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910772", "Effective Java", "Joshua Bloch", "Programming Languages", 0.0));

        } finally {
            // guardo la informacion.
            this.guardarInformacion();
        }

    }

    /**
     * Activa (inicia sesion) de un socio en el sistema.
     *
     * @param numeroDeSocio a utilizar.
     * @param contrasenia   a validar.
     */
    public void iniciarSession(final int numeroDeSocio, final String contrasenia) {

        // el numero de socio siempre es positivo.
        if (numeroDeSocio <= 0) {
            throw new IllegalArgumentException("El numero de socio no es valido!");
        }

        // recorro el arreglo de Socios.
        for (Socio socio : this.socios) {
            // si no lo encontre, retorno una excepsion y no lo dejo continuar.
            if (!(socio.getNumeroDeSocio() == numeroDeSocio)) {
                throw new IllegalArgumentException("El socio ingresado no existe!");
            }

        }

        // recorro el arreglo de Socios para validar la contrasenia
        for (Socio socio : this.socios) {
            // si no lo encontre, retorno una excepsion y no lo dejo continuar.
            if (!(socio.getContrasenia().equals(contrasenia))) {
                throw new IllegalArgumentException("La contraseña ingresada no existe!");
            }
        }

        // coloco los datos del socio logeado en el atributo socio
        this.socio = this.socios[0];
    }

    /**
     * Cierra la session del Socio.
     */
    public void cerrarSession() {
        this.socio = null;
    }

    /**
     * Metodo que mueve un libro de los disponibles y lo ingresa a un Socio.
     *
     * @param isbn del libro a prestar.
     */
    public void realizarPrestamoLibro(final String isbn) throws IOException {
        // el socio debe estar activo.
        if (this.socio == null) {
            throw new IllegalArgumentException("Socio no se ha logeado!");
        }

        // busco el libro.
        Libro libro = this.buscarLibro(isbn);

        // si no lo encontre, lo informo.
        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        // agrego el libro al socio.
        this.socio.agregarLibro(libro);


        // hago un ordenamiento de busqueda tipo burbuja para eliminar el libro de los disponibles
        int n = 2; // tamaño inicial del arreglo
        while (n > 1) {
            int newN = 0;
            for (int i = 1; i < n; i++) {
                if (libros[i - 1].equals(libro)) { // busco el dato ISBN que quiero eliminar
                    // intercambiar elementos
                    Libro temp = libros[i - 1];
                    libros[i - 1] = libros[i];
                    libros[i] = temp;
                    newN = i;
                }
            }
            n = newN;
        }
        // disminuir el tamaño del arreglo a 1
        libros = Arrays.copyOfRange(libros, 0, 1); // modifico el largo del arreglo


        // se actualiza la informacion de los archivos
        this.guardarInformacion();

    }

    /**
     * Obtiene un String que representa el listado completo de libros disponibles.
     *
     * @return the String con la informacion de los libros disponibles.
     */
    public String obtegerCatalogoLibros() {

        StringBuilder sb = new StringBuilder();
        for (Libro libro : this.libros) {
            sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
            sb.append("Autor     : ").append(libro.getAutor()).append("\n");
            sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
            sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
            sb.append("Calificacion: ").append(libro.getCalificacion()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Metodo que busca un libro en los libros disponibles.
     *
     * @param isbn a buscar.
     * @return el libro o null si no fue encontrado.
     */
    private Libro buscarLibro(final String isbn) {
        // recorro el arreglo de libros.
        for (Libro libro : this.libros) {
            // si lo encontre, retorno el libro.
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        // no lo encontre, retorno null.
        return null;
    }

    /**
     * Lee los archivos libros.json y socios.json.
     *
     * @throws FileNotFoundException si alguno de los archivos no se encuentra.
     */
    private void cargarInformacion() throws FileNotFoundException {

        // trato de leer los socios y los libros desde el archivo.
        this.socios = GSON.fromJson(new FileReader("socios.json"), Socio[].class);
        this.libros = GSON.fromJson(new FileReader("libros.json"), Libro[].class);
    }

    /**
     * Guarda los arreglos libros y socios en los archivos libros.json y socios.json.
     *
     * @throws IOException en caso de algun error.
     */
    private void guardarInformacion() throws IOException {

        // guardo los socios.
        try (FileWriter writer = new FileWriter("socios.json")) {
            GSON.toJson(this.socios, writer);
        }

        // guardo los libros.
        try (FileWriter writer = new FileWriter("libros.json")) {
            GSON.toJson(this.libros, writer);
        }

    }

    /**
     * Obtiene los datos del socio que ha iniciado sesiony los despliega por pantalla
     */
    public String obtenerDatosSocioLogeado() {
        if (this.socio == null) {
            throw new IllegalArgumentException("No hay un Socio logeado");
        }

        return "Nombre: " + this.socio.getNombreCompleto() + "\n"
                + "Correo Electronico: " + this.socio.getCorreoElectronico();
    }

    /**
     * Cambia la contrasenia del socio logueado
     *
     * @param contrasenia nueva para el usuario
     */
    public void cambiarContrasenia(String contrasenia) {
        this.socio.setContrasenia(contrasenia);

    }

    /**
     * Cambia el corre del usuario logeado
     *
     * @param correo nuevo del usuario
     */
    public void cambiarCorreo(String correo) {
        this.socio.setCorreoElectronico(correo);
    }

    /**
     * Califica el libro que ingrese el usuario mediante ISBN
     *
     * @param isbnString es el ISBN que ingresó el usuario
     * @throws Exception por si no se encuentra el ISBN o la calificacion no es valida
     */
    public void calificarLibro(String isbnString) throws Exception {
        Libro isbn; // creo un ISBN tipo Libro para utilizar mas adelante

        // instancio el metodo buscar libro para que idfentifique cual libro calificar mediante ISBN
        isbn = buscarLibro(isbnString);
        // si no lo encontre retorno null y termina el metodo con un mensaje personalizado
        if (isbn == null) {
            System.out.println("ISBN no encontrado!");
            return;
        }

        boolean verificador = true; // para continuar con el while

        // si lo encontre continuo con la ejecucion del metodo
        while (verificador) {
            // solicito el dato desde pantalla
            System.out.print("Ingresa un número del 1 al 5: ");
            String numeroString = StdIn.readLine();
            double numero;
            // si el dato es correcto, entro al try
            try {
                numero = Double.parseDouble(numeroString);
                System.out.println("Gracias por darnos tu opinion");
                verificador = false; // bollean falso para salir del while
                // si el dato no es un valor numerico lanzo una excepsion
            } catch (NumberFormatException e) {
                throw new Exception("El valor \"" + numeroString + "\" no es un número válido.");
            }
            // si el numero esta fuera del rango especificado, lanzo una excepsion
            if (numero < 1.0 || numero > 5.0) {
                throw new Exception("El número " + numero + " está fuera del rango de 1.0 y 5.0.");
            }

            // recorro el largo de Libros[]
            for (int i = 0; i < libros.length; i++) {
                // si lo encontre modifico el dato en cuestion.
                if (libros[i].equals(isbn)) {
                    // seteo la calificacion
                    isbn.setCalificacion(numero);
                    // actualizo los datos del libro
                    this.libros[i] = new Libro(isbn.getIsbn(), isbn.getTitulo(), isbn.getAutor(), isbn.getCategoria(),
                            isbn.getCalificacion());
                }
            }
        }
    }
}
