/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlexcompiler;

import java.util.HashMap;

/**
 *
 * @author Arnol
 */
public class Flujo {
    int noEtiqueta;
    HashMap etiqueta;
    static String  tag = "L";
    public Flujo(){
        this.noEtiqueta = 1;
        this.etiqueta = new HashMap<String, Codigo>();
    }
    
    public String newEtiqueta(String nombre, boolean tresDirecciones){
        if(tresDirecciones){
            this.etiqueta.put(nombre, new Codigo(tag+(this.noEtiqueta++)+":",tag+(this.noEtiqueta++)+":",tag+(this.noEtiqueta++))+":");
        }else{
            if(nombre.contains("if")){
                this.etiqueta.put(nombre, new Codigo("",tag+(this.noEtiqueta++)+":",tag+(this.noEtiqueta++)+":"));
                Codigo codigo = (Codigo) this.etiqueta.get(nombre);
                return codigo.verdad;
            }else{
                if(nombre.contains("and")){
                    this.etiqueta.put(nombre, new Codigo("",tag+(this.noEtiqueta++)+":",""));
                    Codigo codigo = (Codigo) this.etiqueta.get(nombre);
                    return codigo.verdad;
                }else{
                    if(nombre.contains("else")){
                        this.etiqueta.put(nombre, new Codigo(tag+(this.noEtiqueta++)+":","",tag+(this.noEtiqueta++)+":"));
                        Codigo codigo = (Codigo) this.etiqueta.get(nombre);
                        return "goto " +codigo.falso;
                    }else{
                        if(nombre.contains("or")){
                            this.etiqueta.put(nombre, new Codigo("","",tag+(this.noEtiqueta++)+":"));
                            Codigo codigo = (Codigo) this.etiqueta.get("if");
                            return codigo.verdad;
                        }else{
                            if(nombre.contains("until")){
                                this.etiqueta.put(nombre, new Codigo("","",tag+(this.noEtiqueta++)+":"));
                                return "";
                            }else{
                                this.etiqueta.put(nombre, new Codigo(tag+(this.noEtiqueta++)+":","",""));
                            }                         
                        }                    
                    }
                }
            }
        }

        Codigo codigo = (Codigo) this.etiqueta.get(nombre);
        return codigo.declaracion;
    }
    
    public String getEtiqueta(String nombre){
        if(this.etiqueta.containsKey(nombre)){
            Codigo codigo = (Codigo) this.etiqueta.get(nombre);
            return codigo.declaracion;
        }else{
            return "";
        }
    }

    boolean existEtiqueta(String tag) {
        return this.etiqueta.containsKey(tag);
    }

    void setEtiqueta(String nombre, Codigo codigoWhile) {
        this.etiqueta.put(nombre, codigoWhile);
    }
}
