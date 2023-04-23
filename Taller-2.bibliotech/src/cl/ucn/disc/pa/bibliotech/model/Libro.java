/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

/**
 * Clase que representa un Libro.
 *
 * @author Programacion Avanzada.
 */
public final class Libro {

    /**
     * The ISBN.
     */
    private String isbn;

    /**
     * The Titulo.
     */
    private String titulo;

    /**
     * The Author.
     */
    private String autor;

    /**
     * The Categoria
     */
    private String categoria;

    /**
     * The Calificacion.
     */
    private double calificacion;

    /**
     * The Constructor.
     *
     * @param isbn         del libro.
     * @param titulo       del libro.
     * @param autor        del libro
     * @param categoria    del libro.
     * @param calificacion del libro
     */
    public Libro(final String isbn, final String titulo, final String autor, final String categoria, final double calificacion) {

        // validacion del ISBN
        if (isbn.length() < 10) {
            throw new IllegalArgumentException("ISBN no valido!");
        }
        this.isbn = isbn;

        // validacion del titulo
        if (titulo == null || titulo.length() == 0) {
            throw new IllegalArgumentException("Titulo no valido!");
        }
        this.titulo = titulo;


        // validacion del autor
        if (autor.length() < 2) {
            throw new IllegalArgumentException("Nombre del autor no valido!");
        }
        this.autor = autor;

        // validacion de la categoria
        if (categoria.length() < 5) {
            throw new IllegalArgumentException("Categoria no valida!");
        }
        this.categoria = categoria;

        // validacion de la calificacion
        if (calificacion < 0.0) {
            throw new IllegalArgumentException("Solo numeros sobre 0.0!");
        }
        this.calificacion = calificacion;
    }

    /**
     * @return the ISBN.
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * @return the titulo.
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * @return the autor.
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * @return the categoria.
     */
    public String getCategoria() {
        return this.categoria;
    }

    /**
     * @return la calificacion
     */
    public double getCalificacion() {
        return calificacion;
    }

    /**
     * @param calificacion a agregar a algun libro
     */
    public void setCalificacion(double calificacion) {
        double calificacionActual = getCalificacion();
        this.calificacion = (calificacionActual + calificacion) / 2;
    }
}
