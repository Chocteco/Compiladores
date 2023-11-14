/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlexcompiler;

import java.util.ArrayList;

/**
 *
 * @author Arnol
 */


public class Nodo {
    private String nombre;
    private ArrayList<Nodo> hijos;
    private String valor;
    private int numNodo;
    private boolean declararIdentificador;
    private String tipo;
    private boolean soyIdentificador;
    
    public Nodo(String nombre)
    {
        setNombre(nombre);
        hijos = new ArrayList<>();
        //setValor("");
        setNumNodo(0);
        setDeclararIdentificador(false);
        setSoyIdentificador(false);
    }
    
    public Nodo(String nombre,boolean declararIdentificador)
    {
        setNombre(nombre);
        hijos = new ArrayList<>();
        //setValor("");
        setNumNodo(0);
        setDeclararIdentificador(declararIdentificador);
    }
    public Nodo(String nombre,boolean declararIdentificador, boolean soyIdentiicador)
    {
        setNombre(nombre);
        hijos = new ArrayList<>();
        //setValor("");
        setNumNodo(0);
        setDeclararIdentificador(declararIdentificador);
        setSoyIdentificador(soyIdentiicador);
    }
    
    public void addHijo(Nodo hijo)
    {
        hijos.add(hijo);
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the hijos
     */
    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    /**
     * @param hijos the hijos to set
     */
    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the numNodo
     */
    public int getNumNodo() {
        return numNodo;
    }

    /**
     * @param numNodo the numNodo to set
     */
    public void setNumNodo(int numNodo) {
        this.numNodo = numNodo;
    }
    
    /**
     * @return the DeclararIdentificador
     */
    public boolean getDeclararIdentificador() {
        return declararIdentificador;
    }

    /**
     * @param declararIdentificador the declararIdentificador to set
     */
    public void setDeclararIdentificador(boolean declararIdentificador) {
        this.declararIdentificador = declararIdentificador;
    }
    
    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * @param declararIdentificador the declararIdentificador to set
     */
    public void setSoyIdentificador(boolean soyIdentificador) {
        this.soyIdentificador = soyIdentificador;
    }
    
    /**
     * @return the tipo
     */
    public boolean getSoyIdentificador() {
        return soyIdentificador;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    void addHijos(ArrayList<Nodo> hijos) {
        for(int i=0 ; i< hijos.size(); i++)
            this.addHijo(hijos.get(i));
    }
}
