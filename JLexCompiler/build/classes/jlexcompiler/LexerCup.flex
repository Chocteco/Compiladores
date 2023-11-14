
package jlexcompiler;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char
letra=[a-zA-Z]
digito=[0-9]
terminacionLinea = \r|\n|\r\n
caracterEntrada = [^\r\n]
espacio    = {terminacionLinea} | [ \t\f]
comentarioTradicional   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
comentarioLineal     = "//" {caracterEntrada}* {terminacionLinea}?
comentario = {comentarioTradicional} | {comentarioLineal}
%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%
( program ) {return new Symbol(sym.Program, yychar, yyline, yytext());}
( if ) {return new Symbol(sym.If, yychar, yyline, yytext());}
( then ) {return new Symbol(sym.Then, yychar, yyline, yytext());}
( else ) {return new Symbol(sym.Else, yychar, yyline, yytext());}
( fi ) {return new Symbol(sym.Fi, yychar, yyline, yytext());}
( do ) {return new Symbol(sym.Do, yychar, yyline, yytext());}
( until ) {return new Symbol(sym.Until, yychar, yyline, yytext());}
( while ) {return new Symbol(sym.While, yychar, yyline, yytext());}
( read ) {return new Symbol(sym.Read, yychar, yyline, yytext());}
( write ) {return new Symbol(sym.Write, yychar, yyline, yytext());}
( int ) {return new Symbol(sym.Int, yychar, yyline, yytext());}
( float ) {return new Symbol(sym.Float, yychar, yyline, yytext());}
( bool ) {return new Symbol(sym.Bool, yychar, yyline, yytext());}
( true ) {return new Symbol(sym.True, yychar, yyline, yytext());}
( false ) {return new Symbol(sym.False, yychar, yyline, yytext());}
( not ) {return new Symbol(sym.Not, yychar, yyline, yytext());}
( and ) {return new Symbol(sym.And, yychar, yyline, yytext());}
( or ) {return new Symbol(sym.Or, yychar, yyline, yytext());}
{espacio} {/*Ignore*/}
( "+" ) {return new Symbol(sym.Suma, yychar, yyline, yytext());}
( "-" ) {return new Symbol(sym.Resta, yychar, yyline, yytext());}
( "*" ) {return new Symbol(sym.Multiplicacion, yychar, yyline, yytext());}
( "/" ) {return new Symbol(sym.Division, yychar, yyline, yytext());}
( "^" ) {return new Symbol(sym.Potencia, yychar, yyline, yytext());}
( "<" ) {return new Symbol(sym.Menor_que, yychar, yyline, yytext());}
( "<=" ) {return new Symbol(sym.Menor_o_igual, yychar, yyline, yytext());}
( ">" ) {return new Symbol(sym.Mayor_que, yychar, yyline, yytext());}
( ">=" ) {return new Symbol(sym.Mayor_o_igual, yychar, yyline, yytext());}
( "==" ) {return new Symbol(sym.Igual_que, yychar, yyline, yytext());}
( "!=" ) {return new Symbol(sym.Diferente_que, yychar, yyline, yytext());}
( "=" ) {return new Symbol(sym.Igual, yychar, yyline, yytext());}
( ";" ) {return new Symbol(sym.Punto_y_coma, yychar, yyline, yytext());}
( "," ) {return new Symbol(sym.Coma, yychar, yyline, yytext());}
( "(" ) {return new Symbol(sym.Abre_parentesis, yychar, yyline, yytext());}
( ")" ) {return new Symbol(sym.Cierra_parentesis, yychar, yyline, yytext());}
( "{" ) {return new Symbol(sym.Abre_llave, yychar, yyline, yytext());}
( "}" ) {return new Symbol(sym.Cierra_llave, yychar, yyline, yytext());}
{letra}({letra}|{digito})* {return new Symbol(sym.Identificador, yychar, yyline, yytext());}
{digito}{digito}*(.{digito}{digito}*)? {return new Symbol(sym.Numero, yychar, yyline, yytext());}
{comentario} {return new Symbol(sym.Comentario, yychar, yyline, yytext());}
. {return new Symbol(sym.ERROR, yychar, yyline, yytext());}