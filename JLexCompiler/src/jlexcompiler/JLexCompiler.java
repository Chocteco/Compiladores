/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlexcompiler;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java_cup.runtime.Symbol;

/**
 *
 * @author HP
 */
public class JLexCompiler {
    
    public static String tipo = "";
    public static Map tablaSimbolos = new HashMap();
    public static String sintactico = ""; 
    public static String semantico = ""; 
    public static String tablaDeSimbolos = ""; 
    public static int contadorTemporal = 0;
    public static String arbolTipado = "";
    public static Boolean errorSemantico = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        String ruta1 = "C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/Lexer.flex";
        String ruta2 = "C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/LexerCup.flex";
        String[] rutaS = {"-parser", "Sintax", "C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/Sintax.cup"};
        
        //generar(ruta1, ruta2, rutaS);
        //"C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/archivo.txt"
        analizardorLexico(args);        analizadorSintacticoySemantico(args);

    }
   
    public static void generarLexer(String ruta){
        File archivo = new File (ruta);
        JFlex.Main.generate(archivo);
    }
    
    public static void generar(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception{
        File archivo;
        archivo = new File(ruta1);
        JFlex.Main.generate(archivo);
        archivo = new File(ruta2);
        JFlex.Main.generate(archivo);
        java_cup.Main.main(rutaS);
        
        Path rutaSym = Paths.get("C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
        }
        Files.move(
                Paths.get("C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/sym.java"), 
                Paths.get("C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/sym.java")
        );
        Path rutaSin = Paths.get("C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/Sintax.java"), 
                Paths.get("C:/Users/Arnol/Documents/NetBeansProjects/Compilador-Tiny/JLexCompiler/src/jlexcompiler/Sintax.java")
        );
    }
     
    public static void analizardorLexico(String[] arg){
        if(!(new File(arg[0]).exists())){
            String resultadoE = "Nada que compilar :( \n";
            //System.out.println(resultadoE);
        return;
        }
        String[] ruta = arg[0].split("/");
        int ind = 0;
        if(ruta.length > 0)
            ind = ruta.length-1;
        String rutaLexicos = arg[0].replaceFirst(ruta[ind], "Lexicos.txt");
        String rutaErrores = arg[0].replaceFirst(ruta[ind], "ErroresL.txt");
        //System.out.println(rutaLexicos);
        //System.out.println(rutaErrores);
        try {
            Reader lector = new BufferedReader(new FileReader(arg[0]));
            Lexer lexer = new Lexer(lector);
            String resultado = "";
            String resultadoE = "";
            int numL=0;
            while (true) {
                Tokens tokens = lexer.yylex();
                if (tokens == null) {
                    System.out.println(resultado);
                    Guardar(rutaLexicos, resultado);
                    Guardar(rutaErrores, resultadoE);
                    return;
                }
                numL++;
                int line =lexer.line+1;
                int column =lexer.column+1;
                switch (tokens) {
                    case ERROR:
                        resultadoE += lexer.lexeme+ "\tSimbolo no definido en el lexema "+numL+" ("+line+":"+column+")\n";
                        break;
                    case Reservada:
                        resultado +=numL+"\t"+ lexer.lexeme + "\tEs una palabra " + tokens +"\t"+line+"\t"+column+"\n";
                    break;
                    case Suma: case Resta: case Multiplicacion: case Division: case Potencia: case Igual:
                        resultado +=numL+"\t"+ lexer.lexeme + "\tEs un operador (" + tokens + ")\t"+line+"\t"+column+"\n";;
                    break;
                    case Comentario:
                        if(lexer.lexeme.contains("\r\n")){
                            resultado +=numL+"\t"+ lexer.lexeme.replace("\r\n", "") + "\tEs un " + tokens + "\t"+line+"\t"+column+"\n";
                            System.out.print("Aqui 1 "+lexer.lexeme.replace("\r\n", ""));
                        }
                        else if(lexer.lexeme.contains("\r")){
                            resultado +=numL+"\t"+ lexer.lexeme.replace("\r", "") + "\tEs un " + tokens + "\t"+line+"\t"+column+"\n";
                            System.out.print("Aqui 2");
                        }else
                            resultado +=numL+"\t"+ lexer.lexeme.replace("\n", "") + "\tEs un " + tokens + "\t"+line+"\t"+column+"\n";
                            System.out.print("Aqui 3");
                    break;
                    case Identificador: case Numero:
                        resultado +=numL+"\t"+ lexer.lexeme + "\tEs un " + tokens + "\t"+line+"\t"+column+"\n";
                        break;
                    case Menor_que: case Menor_o_igual: case Mayor_que: case Mayor_o_igual:
                    case Igual_que: case Diferente_que:
                        resultado +=numL+"\t"+ lexer.lexeme + "\tEs un comparador (" + tokens + ")\t"+line+"\t"+column+"\n";
                        break;
                    default:
                        resultado +=numL+"\t"+ lexer.lexeme + "\tEs un caracter especial (" + tokens + ")\t"+line+"\t"+column+"\n";
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        }
    }
    
    public static void analizadorSintacticoySemantico(String[] arg) throws FileNotFoundException, Exception{
        // TODO add your handling code here:
        String ST = leeBuffer(new BufferedReader(new FileReader(arg[0])));
        System.out.println(ST);
        Sintax s = new Sintax(new jlexcompiler.LexerCup(new StringReader(ST)));
        String[] ruta = arg[0].split("/");
        int ind = 0;
        if(ruta.length > 0)
            ind = ruta.length-1;
        //Sintactico
        String rutaSintactico = arg[0].replaceFirst(ruta[ind], "Sintactico.txt");
        String rutaErroresS = arg[0].replaceFirst(ruta[ind], "ErroresS.txt");
        //Semantico
        String rutaSemantico = arg[0].replaceFirst(ruta[ind], "Semantico.txt");
        //Codigo Intermedio
        String rutaCodigoIntermedio = arg[0].replaceFirst(ruta[ind], "codigoIntermedio.txt");
        try {
            s.parse();  
            imprimirSintactico(s.padre,0);
            analizadorSemantico(s.padre);
            imprimirTablaSimbolos();
            System.out.println("\n------------------ CÓDIGO INTERMEDIO -------------------\n");
            codigoIntermedio();
            System.out.println(CODIGOINTERMEDIO);
            System.out.println("Analisis sintactico realizado correctamente");
            Guardar(rutaSintactico, "Analisis sintactico realizado correctamente\n"+sintactico);
            if(errorSemantico)
                Guardar(rutaErroresS, "Error semántico");
            else
                Guardar(rutaErroresS, "");
            //Guardar(rutaSemantico, tablaDeSimbolos + semantico);
            Guardar(rutaSemantico, tablaDeSimbolos + arbolTipado);
            Guardar(rutaCodigoIntermedio, CODIGOINTERMEDIO);
            
            //imprimir(s.padre,0);
            //txtAnalizarSin.setForeground(new Color(25, 111, 61));
        } catch (Exception ex) {
            Symbol sym = s.getS();
            String resultadoError = "Error de sintaxis. Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1 - caracteresAnteriores(sym.right+1,arg[0])) + ", Texto: \"" + sym.value + "\"";
            System.out.println(resultadoError);
            Guardar(rutaErroresS, resultadoError);
            Guardar(rutaSintactico, "Error de sintaxis");
            Guardar(rutaSemantico, "Error de sintaxis");
            //txtAnalizarSin.setForeground(Color.red);
        }
        //imprimirGeneral(s.padre,0);
    }
    
    public static String leeBuffer(BufferedReader buffer) throws Exception
    {
        String retorno = null;

        String lineaSalida = "";
        StringBuffer contenido = new StringBuffer();
        String separador = "";

        while ((lineaSalida = buffer.readLine()) != null)
        {
            contenido.append(separador + lineaSalida);
            separador = "\n";
        }

        retorno = contenido.toString();

        return retorno;
    }
    
    public static void Guardar(String ruta, String texto) {
        try {
            if (new File(ruta) != null) {
                try (FileWriter guardado = new FileWriter(new File(ruta))) {
                    guardado.write(texto);
                    //JOptionPane.showMessageDialog(rootPane, "Archivo actualizado");
                }
            }
        } catch (IOException ex) {
            //JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private static int caracteresAnteriores(int right, String string) throws IOException {
        int nCaracteres = 0;
        if(right>1){
            BufferedReader buffer = new BufferedReader(new FileReader(string));
            String lineaSalida = "";
            StringBuffer contenido = new StringBuffer();
            String separador = "";
            int line = 1;
            while ((lineaSalida = buffer.readLine()) != null)
            {
                contenido.append(separador + lineaSalida);
                separador = "\n";
                line++;
                if(line>=right){
                    contenido.append("\n");
                    break;
                }
            }

            nCaracteres = contenido.toString().length();
            System.out.println(contenido.toString());
        }    
        System.out.print(nCaracteres);
        return nCaracteres;
    }

    private static void imprimirGeneral(Nodo padre,int espacios) {
        impimirEspacios(espacios,true);
        if(padre != null){
            System.out.println(padre.getNombre() + "->" + padre.getValor());
            espacios+=3;
            for(int i=0;i<padre.getHijos().size();i++){
                imprimirGeneral(padre.getHijos().get(i),espacios);
            }
        }
        
    }
    
    private static void imprimirSintactico(Nodo padre,int espacios) {
        Boolean sinEstilos = false;
        if(padre != null){
            if(padre.getNombre().equals("int")||padre.getNombre().equals("float")||padre.getNombre().equals("bool"))
                tipo = padre.getNombre();
            if(padre.getDeclararIdentificador()){
                padre.setTipo(tipo);
                impimirEspacios(espacios,sinEstilos);
                if(tablaSimbolos.containsKey(padre.getNombre())){
                    //System.out.println(padre.getNombre()+ " ---> El identificador ya ha sido declarado anteriormente");
                    sintactico += padre.getNombre()+"("+padre.getTipo()+") ---> El identificador ya ha sido declarado anteriormente\n";
                    errorSemantico = true;
                }else{
                    //System.out.println(padre.getNombre()+ "("+padre.getTipo()+")");
                    sintactico += padre.getNombre()+"("+padre.getTipo()+")\n";
                    tablaSimbolos.put(padre.getNombre(), padre.getTipo());
                }
                espacios+=3;
            }
            else{
                if(imprimible(padre.getNombre())){
                    impimirEspacios(espacios,sinEstilos);
                    if(padre.getSoyIdentificador()){
                        if(tablaSimbolos.containsKey(padre.getNombre())){
                            padre.setTipo(tablaSimbolos.get(padre.getNombre()).toString());
                            //System.out.println(padre.getNombre()+ "("+padre.getTipo()+")");
                            sintactico += padre.getNombre()+"("+padre.getTipo()+")\n";
                        }else{
                            //System.out.println(padre.getNombre()+ " ---> El identificador \""+padre.getNombre()+"\" no ha sido declarado...");
                            sintactico += padre.getNombre()+" ---> El identificador \""+padre.getNombre()+"\" no ha sido declarado...\n";
                            errorSemantico = true;
                            return;
                        }
                    }else    
                        if(padre.getValor()!=null){
                            //System.out.println(padre.getValor());
                            if(padre.getValor().contains("."))
                                padre.setTipo("float");
                            else
                                if(padre.getValor().equals("true") || padre.getValor().equals("false"))
                                    padre.setTipo("bool");
                                else
                                    padre.setTipo("int");
                            sintactico += padre.getValor()+"("+padre.getTipo()+")\n";
                        }
                        else{
                            //System.out.println(padre.getNombre());
                            sintactico += padre.getNombre()+"\n";
                        }
                    espacios+=3;
                }
            }
            for(int i=0;i<padre.getHijos().size();i++){
                imprimirSintactico(padre.getHijos().get(i),espacios);
            }
        }
    }

    private static boolean imprimible(String nombre) {
        switch (nombre) {
            case "int":
            case "float":
            case "bool":
            case "Lista_declaracion":
            case "Lista_sentencias":
            case "Lista_id":
            case "Lista_id_2":
            case "B_expresion":
            case "B_term":
            case "Not_factor":
            case "Relacion":
            case "Expresion":
            case "Termino":
            case "SignoFactor":
            case "B_factor":
            case "Sentencias":
            case ";":
            case "{":
            case "(":
            case "}":
            case ")":
            case "Factor":
            case "Relacion_2":
            case "Expresion_2":
            case "Termino_2":
            case "Seleccion":
            case "Bloque":
                return false;
            default:
                return true;
        }
    }

    private static void impimirEspacios(int espacios, boolean completo) {
        if(espacios!=0)
            for(int i=0;i<espacios; i++){
                if(completo){
                    //System.out.print(" ");
                    sintactico += " ";
                    continue;
                }
                if(espacios-3==i){
                    //System.out.print("|__");
                    sintactico += "|__";
                    return;
                }else
                    if(i==0){
                        //System.out.print("|"); 
                        sintactico += "|";
                    }
                    else{
                        //System.out.print(" ");
                        sintactico += " ";
                    } 
            }  
    }

    private static void imprimirTablaSimbolos() {
        //System.out.println("-------------------------------------");
        tablaDeSimbolos += "-------------------------------------\n";
        //System.out.println("Tabla de Simbolos");
        tablaDeSimbolos += "Tabla de Simbolos\n";
        //System.out.println("-------------------------------------");
        tablaDeSimbolos += "-------------------------------------\n";
        //System.out.println("Variable\t|Tipo");
        tablaDeSimbolos += "Var.\tTipo\n"; 
        tablaDeSimbolos += "-------------------------------------\n";
        for (Object key: tablaSimbolos.keySet()){  
            //System.out.println(key+ "\t|" + tablaSimbolos.get(key));
            tablaDeSimbolos += key+ "\t" + tablaSimbolos.get(key)+"\n";
        }
        tablaDeSimbolos += "-------------------------------------\n";
        System.out.println(tablaDeSimbolos);
    }

    private static void analizadorSemantico(Nodo padre) {
        semantico = sintactico;
        sintactico = sintactico.replace("(int)", "");
        sintactico = sintactico.replace("(float)", "");
        sintactico = sintactico.replace("(bool)", "");
        //System.out.println(semantico);
        String[] codigo = semantico.split("\n");
        try{
        validarTipos(codigo);
        }catch(Exception e){
            System.err.println(e.toString());
        }
    }

    private static void validarTipos(String[] codigo) {
        for(int i=0;i<codigo.length;i++){
            //System.out.print(codigo[i]);
            if(codigo[i].contains("=")&&!codigo[i].contains("==")&&!codigo[i].contains(">=")){
                int ind = i;
                do{
                    if(ind+1>=codigo.length)
                        break;
                }while(proximaSentencia(codigo[++ind]));
                int lim = i;
                i = ind-1;
                lim= i-lim;
                switch (lim) {
                    case 2:
                        String tipoResultante = compararTipos(codigo[i-1],codigo[i]);
                        //System.out.println(" --> "+tipoResultante);
                        codigo[i-2] += " --> "+tipoResultante;
                        //System.out.println(codigo[i-1]);
                        //System.out.print(codigo[i]);
                        break;
                    case 4:
                        String nuevoTipo = combinarTipos(codigo[i-1],codigo[i]);
                        if(!nuevoTipo.contains("ERROR")){
                            tablaSimbolos.put("T"+contadorTemporal, nuevoTipo);
                            codigo[i-2]+=" --> "+nuevoTipo+ " T"+contadorTemporal++;
                        }else{
                            codigo[i-2]+=" --> "+nuevoTipo;
                            errorSemantico = true;
                        }
                        tipoResultante = compararTipos(codigo[i-3],codigo[i-2]);
                        //System.out.println(" --> "+tipoResultante);
                        codigo[i-4] += " --> "+tipoResultante;
                        //System.out.println(codigo[i-3]);
                        //System.out.println(codigo[i-2]);
                        //System.out.println(codigo[i-1]);
                        //System.out.print(codigo[i]);
                        break;
                    case 6:
                        String expresion ="";
                        for(int j = lim-1;j>=0;j-- )
                            expresion+=codigo[i-j]+"\n";
                        if(!((expresion.contains("*") || expresion.contains("*")) && (expresion.contains("+") || expresion.contains("-")))){
                            String tipo = validarTipoAsignacion(expresion);
                            //System.out.println(" --> "+tipoResultante);
                            if(!tipo.contains("ERROR")){
                                tablaSimbolos.put("T"+contadorTemporal, tipo);
                                codigo[i-lim]+=" --> "+tipo+ " T"+contadorTemporal++;
                            }else{
                                codigo[i-lim]+=" --> "+tipo;
                                errorSemantico = true;
                            }
                            break;
                        }
                        if(codigo[i-2].contains("*") || codigo[i-2].contains("/")){
                            System.out.println("Entra");
                            
                            
                            nuevoTipo = combinarTipos(codigo[i-1],codigo[i]);
                            if(!nuevoTipo.contains("ERROR")){
                                tablaSimbolos.put("T"+contadorTemporal, nuevoTipo);
                                codigo[i-2]+=" --> "+nuevoTipo+ " T"+contadorTemporal++;
                            }else{
                                codigo[i-2]+=" --> "+nuevoTipo;
                                errorSemantico = true;
                            }
                            nuevoTipo = combinarTipos(codigo[i-3],codigo[i-2]);
                            if(!nuevoTipo.contains("ERROR")){
                                tablaSimbolos.put("T"+contadorTemporal, nuevoTipo);
                                codigo[i-4]+=" --> "+nuevoTipo+ " T"+contadorTemporal++;
                            }else{
                                codigo[i-4]+=" --> "+nuevoTipo;
                                errorSemantico = true;
                            }

                            tipoResultante = compararTipos(codigo[i-5],codigo[i-4]);
                            //System.out.println(" --> "+tipoResultante);
                            codigo[i-6] += " --> "+tipoResultante;
                            //System.out.println(codigo[i-5]);
                            //System.out.println(codigo[i-4]);
                            //System.out.println(codigo[i-3]);
                            //System.out.println(codigo[i-2]);
                            //System.out.println(codigo[i-1]);
                            //System.out.print(codigo[i]);
                            
                        }else{
                        nuevoTipo = combinarTipos(codigo[i-2],codigo[i-1]);
                        if(!nuevoTipo.contains("ERROR")){
                            tablaSimbolos.put("T"+contadorTemporal, nuevoTipo);
                            codigo[i-3]+=" --> "+nuevoTipo+ " T"+contadorTemporal++;
                        }else{
                            codigo[i-3]+=" --> "+nuevoTipo;
                            errorSemantico = true;
                        }
                        nuevoTipo = combinarTipos(codigo[i-3],codigo[i]);
                        if(!nuevoTipo.contains("ERROR")){
                            tablaSimbolos.put("T"+contadorTemporal, nuevoTipo);
                            codigo[i-4]+=" --> "+nuevoTipo+ " T"+contadorTemporal++;
                        }else{
                            codigo[i-4]+=" --> "+nuevoTipo;
                            errorSemantico = true;
                        }
                        
                        tipoResultante = compararTipos(codigo[i-5],codigo[i-4]);
                        //System.out.println(" --> "+tipoResultante);
                        codigo[i-6] += " --> "+tipoResultante;
                        //System.out.println(codigo[i-5]);
                        //System.out.println(codigo[i-4]);
                        //System.out.println(codigo[i-3]);
                        //System.out.println(codigo[i-2]);
                        //System.out.println(codigo[i-1]);
                        //System.out.print(codigo[i]);
                        }
                        
                        break;
                    default:
                        expresion ="";
                        for(int j = lim-1;j>=0;j-- )
                            expresion+=codigo[i-j]+"\n";
                        tipo = validarTipoAsignacion(expresion);
                        //System.out.println(" --> "+tipoResultante);
                        if(!tipo.contains("ERROR")){
                            tablaSimbolos.put("T"+contadorTemporal, tipo);
                            codigo[i-lim]+=" --> "+tipo+ " T"+contadorTemporal++;
                        }else{
                            codigo[i-lim]+=" --> "+tipo;
                            errorSemantico = true;
                        }       
                }
                //System.out.println();
            }else{
                if(!codigo[i].contains("identificador")&&(codigo[i].contains("==")||codigo[i].contains(">=")
                        ||codigo[i].contains("<=")||codigo[i].contains("<")||codigo[i].contains(">"))){
                    tablaSimbolos.put("T"+contadorTemporal, "bool");
                    codigo[i]+=" --> bool T"+contadorTemporal++;                
                }else
                    if(codigo[i].contains("write")){
                        int ind = i;
                        do{
                            if(ind+1>=codigo.length)
                                break;
                        }while(proximaSentencia(codigo[++ind]));
                        int lim = i;
                        i = ind-1;
                        lim= i-lim;
                        if(lim!=1){
                            String expresion ="";
                            for(int j = lim-1;j>=0;j-- )
                                expresion+=codigo[i-j]+"\n";
                            tipo = combinarTipoWrite(expresion);
                            //System.out.println(" --> "+tipoResultante);
                            if(!tipo.contains("ERROR")){
                                tablaSimbolos.put("T"+contadorTemporal, tipo);
                                codigo[i-lim+1]+=" --> "+tipo+ " T"+contadorTemporal++;
                            }else{
                                codigo[i-lim+1]+=" --> "+tipo;
                                errorSemantico = true;
                            }
                        }
                    }
            }
        }
        for(int i=0;i<codigo.length;i++){
            if(i==codigo.length-1 && codigo[i].contains("-->"))
                codigo[i] = codigo[i].split("-->")[0];
            arbolTipado+=codigo[i]+"\n";
        }
        System.out.println(arbolTipado);
    }

    private static boolean proximaSentencia(String codigo) {
        if(codigo.contains("int")
         ||codigo.contains("float")
         ||codigo.contains("bool")
         ||codigo.contains("+")
         ||codigo.contains("-")
         ||codigo.contains("*")
         ||codigo.contains("/")
        ) return true;
        else
            return false;
    }

    private static String compararTipos(String ident1, String ident2) {
        String iguales = "";
        if(ident1.contains("int")){
            if(ident2.contains("int"))
                iguales = "int";
            else
                if(ident2.contains("float"))
                    iguales = "ERROR SEMANTICO";
                else
                    if(ident2.contains("bool"))
                        iguales = "ERROR SEMANTICO";
        }else{
            if(ident1.contains("float")){
                if(ident2.contains("int"))
                    iguales = "float";
                else
                    if(ident2.contains("float"))
                        iguales = "float";
                    else
                        if(ident2.contains("bool"))
                            iguales = "ERROR SEMANTICO";
            }
            else{
                if(ident1.contains("bool")){
                    if(ident2.contains("int"))
                        iguales = "ERROR SEMANTICO";
                    else
                        if(ident2.contains("float"))
                            iguales = "ERROR SEMANTICO";
                        else
                            if(ident2.contains("bool"))
                                iguales = "bool";
                }
            }
        }
        if(iguales.contains("ERROR"))
            errorSemantico = true;
        return iguales;
    }

    private static String combinarTipos(String ident1, String ident2) {
        String iguales = "";
        if(ident1.contains("int")){
            if(ident2.contains("int"))
                iguales = "int";
            else
                if(ident2.contains("float"))
                    iguales = "float";
                else
                    if(ident2.contains("bool"))
                        iguales = "ERROR SEMANTICO";
        }else{
            if(ident1.contains("float")){
                if(ident2.contains("int"))
                    iguales = "float";
                else
                    if(ident2.contains("float"))
                        iguales = "float";
                    else
                        if(ident2.contains("bool"))
                            iguales = "ERROR SEMANTICO";
            }else{
                if(ident1.contains("bool")){
                    if(ident2.contains("int"))
                        iguales = "ERROR SEMANTICO";
                    else
                        if(ident2.contains("float"))
                            iguales = "ERROR SEMANTICO";
                        else
                            if(ident2.contains("bool"))
                                iguales = "bool";
                }
            }
        }
        if(iguales.contains("ERROR"))
            errorSemantico = true;
        return iguales;
    }

    private static String validarTipoAsignacion(String expresion) {
        String tipo = "";
        String[] codigo = expresion.split("\n");
        for(int i=1; i<codigo.length;i++){
            tipo = compararTipos(codigo[0], codigo[i]);
            if(tipo.contains("ERROR"))
                break;
        }
        if(tipo.contains("ERROR"))
            errorSemantico = true;
        return tipo;
    }

    private static String combinarTipoWrite(String expresion) {
        String tipo = "";
        if((expresion.contains("int") || expresion.contains("float"))){
            if(!expresion.contains("bool")){
                if(expresion.contains("float"))
                    tipo = "float";
                else
                    tipo = "int";
            }else
                tipo = "ERROR SEMANTICO";
        }else{
            if(expresion.contains("bool")){
                tipo = "bool";
            }else{
                tipo = "";
            }
        }
        
        if(tipo.contains("ERROR"))
            errorSemantico = true;
        
        return tipo;
    }

    private static String getTipo(String codigo) {
        if(codigo.contains("int"))
            return "int";
        else 
            if(codigo.contains("float"))
                return "float";
            else
                if(codigo.contains("bool"))
                    return "bool";
                else
                    return "";
                
    }
    
    static int temporalCI = 1;
    static String CODIGOINTERMEDIO = "";
    private static void codigoIntermedio() {
        String codigoI = semantico;
        Flujo flujo = new Flujo();
        codigoI = codigoI.replace("|", "");
        codigoI = codigoI.replace("_", "");
        codigoI = codigoI.replace("--> int", "");
        codigoI = codigoI.replace("--> float", "");
        codigoI = codigoI.replace("--> bool", "");
        codigoI = codigoI.replace(" ", "");
        //System.out.println(codigoI+"\n------------------\n");
        
        //Array por linea de codigo de arbol
        String[] codI = codigoI.split("\n");
        
        //Recorrer linea a linea de codigo de arbol
        for(int i = 0; i<codI.length; i++){
            //Declaracion
            if(codI[i].contains("Declaracion")){
                i++;
                while(codI[i].contains("int") || codI[i].contains("float") || codI[i].contains("bool")){
                    CODIGOINTERMEDIO+=((codI[i].split("\\(|\\)"))[1]+" "+(codI[i].split("\\(|\\)"))[0])+"\n";
                    i++;
                }
                i--;
            }else
            //Asignacion
            if(codI[i].contains("Asignacion")){
                i++;
                int tipoAsignacion = 0;
                while(codI[i].contains("=") || codI[i].contains("int") || codI[i].contains("float") || codI[i].contains("bool")
                        || codI[i].contains("-")|| codI[i].contains("+")|| codI[i].contains("*")|| codI[i].contains("/")){
                    tipoAsignacion++;
                    i++;
                }
                switch (tipoAsignacion) {
                    case 3:
                        i -= tipoAsignacion;    
                        String asignacion = (codI[i+1] + codI[i]+ codI[i+2].replace("true", "1").replace("false", "0"));
                        asignacion=asignacion.replace("(int)", "");
                        asignacion=asignacion.replace("(float)", "");
                        asignacion=asignacion.replace("(bool)", "");
                        CODIGOINTERMEDIO+=(asignacion)+"\n";
                        i+=tipoAsignacion;
                        break;
                    case 5:
                        i -= tipoAsignacion;
                        if(codI[i+1].equals(codI[i+3])){
                           String temporal = "T"+(temporalCI++);
                            CODIGOINTERMEDIO+=((temporal+" = "+codI[i+3]+codI[i+2]+codI[i+4]).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                            CODIGOINTERMEDIO+=((codI[i+1]+ " = " +temporal).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n"; 
                        }else{
                            CODIGOINTERMEDIO+=((codI[i+1]+ " = "+codI[i+3]+codI[i+2]+codI[i+4]).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n"; 
                        }
                        i+=tipoAsignacion;
                        break;
                    case 7:
                        i -= tipoAsignacion;
                        //if(codI[i+1].equals(codI[i+3])){
                            String temporal = "T"+(temporalCI++);
                            CODIGOINTERMEDIO+=((temporal+" = "+codI[i+4]+codI[i+3]+codI[i+5]).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                            String temporal2 = "T"+(temporalCI++);
                            CODIGOINTERMEDIO+=((temporal2+ " = " +temporal+codI[i+2]+codI[i+6]).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n"; 
                            CODIGOINTERMEDIO+=((codI[i+1]+ " = " +temporal2).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n"; 
                        //}else{
                        //    System.out.println((codI[i+1]+ " = "+codI[i+3]+codI[i+2]+codI[i+4]).replace("(int)","").replace("(float)","").replace("(bool)","")); 
                        //}
                        i+=tipoAsignacion;
                        break;
                    default:
                        //System.out.println(tipoAsignacion);
                }
                i--;
            }else
            //do
            if(codI[i].contains("do")){
                CODIGOINTERMEDIO+=(flujo.newEtiqueta("do", false)+" ");
            }else
            //write
            if(codI[i].contains("write")){
                CODIGOINTERMEDIO+=((codI[i] + " "+codI[++i]).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n"; 
            }else
            //if
            if(codI[i].contains("if")){
                if(ifContain("and",codI,i)){
                    CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + flujo.newEtiqueta("if", false).replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                    Codigo codigo = (Codigo) flujo.etiqueta.get("if");
                    CODIGOINTERMEDIO+=("goto "+codigo.falso.replace(":", ""))+"\n";
                    CODIGOINTERMEDIO+=(codigo.verdad+" ");
                    i+=4;
                    CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + flujo.newEtiqueta("and", false).replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                    CODIGOINTERMEDIO+=("goto "+codigo.falso.replace(":", ""))+"\n";
                    codigo = (Codigo) flujo.etiqueta.get("and");
                    CODIGOINTERMEDIO+=(codigo.verdad+" ");
                    i+=3;
                }else{
                    CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + flujo.newEtiqueta("if", false).replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                    Codigo codigo = (Codigo) flujo.etiqueta.get("if");
                    CODIGOINTERMEDIO+=("goto "+codigo.falso.replace(":", ""))+"\n";
                    CODIGOINTERMEDIO+=(codigo.verdad+" ");
                    i+=4;
                }
            }else
            //else
            if(codI[i].contains("else")){
                CODIGOINTERMEDIO+=(flujo.newEtiqueta("else", false).replace(":", ""))+"\n";
                Codigo codigo = (Codigo) flujo.etiqueta.get("if");
                CODIGOINTERMEDIO+=(codigo.falso+" ");
            }else
            //fi
            if(codI[i].contains("fi")){
                if(flujo.existEtiqueta("else")){
                    Codigo codigo = (Codigo) flujo.etiqueta.get("else");
                    CODIGOINTERMEDIO+=(codigo.falso)+"\n";
                }else{
                    Codigo codigo = (Codigo) flujo.etiqueta.get("if");
                    CODIGOINTERMEDIO+=(codigo.falso)+"\n";
                }
            }else
            //while
            if(codI[i].contains("while")){
                if(ifContain("or",codI,i)){
                    CODIGOINTERMEDIO+=(flujo.newEtiqueta("while", false));
                    CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + flujo.newEtiqueta("if", false).replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                    Codigo codigo = (Codigo) flujo.etiqueta.get("if");
                    CODIGOINTERMEDIO+=("goto "+codigo.falso.replace(":", ""))+"\n";
                    CODIGOINTERMEDIO+=(codigo.falso+" ");
                    i+=4;
                    CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + flujo.newEtiqueta("or", false).replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                    codigo = (Codigo) flujo.etiqueta.get("or");
                    CODIGOINTERMEDIO+=("goto "+codigo.falso.replace(":", ""))+"\n";
                    Codigo codigoWhile = (Codigo) flujo.etiqueta.get("while");
                    codigoWhile.falso = codigo.falso;
                    flujo.setEtiqueta("while",codigoWhile);
                    codigo = (Codigo) flujo.etiqueta.get("if");
                    CODIGOINTERMEDIO+=(codigo.verdad+" ");
                    i+=3;
                }else{
                    CODIGOINTERMEDIO+=(flujo.newEtiqueta("while", false));
                    CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + flujo.newEtiqueta("if", false).replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)",""))+"\n";
                    Codigo codigoWhile = (Codigo) flujo.etiqueta.get("while");
                    Codigo codigoIf = (Codigo) flujo.etiqueta.get("if");
                    codigoWhile.falso = codigoIf.falso;
                    codigoWhile.verdad = codigoIf.verdad;
                    flujo.setEtiqueta("while",codigoWhile);
                    CODIGOINTERMEDIO+=("goto "+codigoWhile.falso.replace(":", ""))+"\n";
                    CODIGOINTERMEDIO+=(codigoWhile.verdad+" ");
                    i+=4;
                }
            }else
            //while
            if(codI[i].contains("endWhile")){
                Codigo codigo = (Codigo) flujo.etiqueta.get("while");
                CODIGOINTERMEDIO+=("goto "+codigo.declaracion.replace(":", ""))+"\n";
                CODIGOINTERMEDIO+=(codigo.falso)+"\n";
            }else
            //until
            if(codI[i].contains("until")){
                Codigo codigoDo = (Codigo) flujo.etiqueta.get("do");
                flujo.newEtiqueta("until",false);
                Codigo codigoUntil = (Codigo) flujo.etiqueta.get("until");
                codigoUntil.verdad = codigoDo.declaracion;
                flujo.setEtiqueta("until", codigoUntil);
                CODIGOINTERMEDIO+=(("if "+codI[i+2] + codI[i+1] + codI[i+3] + " goto " + codigoUntil.verdad.replace(":","")).replace("(int)","").replace("(float)","").replace("(bool)","").replace("true", "1").replace("false", "0"))+"\n";
                CODIGOINTERMEDIO+=("goto "+codigoUntil.falso.replace(":", ""))+"\n";
                CODIGOINTERMEDIO+=(codigoUntil.falso)+"\n";

            }
        }
    }

    private static boolean ifContain(String andOr,String [] cod, int i) {
        boolean existe = false;
        while(!cod[i++].contains("then") && i<cod.length){
            if(cod[i].contains(andOr)){
                return true;
            }
        }
        
        return existe;
    }
}
