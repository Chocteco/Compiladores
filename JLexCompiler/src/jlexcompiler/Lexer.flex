package jlexcompiler;
import static jlexcompiler.Tokens.*;
%%
%class Lexer
%type Tokens
%line
%column
letra=[a-zA-Z]
digito=[0-9]
terminacionLinea = \r|\n|\r\n
caracterEntrada = [^\r\n]
espacio    = {terminacionLinea} | [ \t\f]
comentarioTradicional   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
comentarioLineal     = "//" {caracterEntrada}* {terminacionLinea}?
comentario = {comentarioTradicional} | {comentarioLineal}
%{
    public String lexeme;
    public int line;
    public int column;
%}
%%
program |
if |
else |
fi |
do |
until |
while |
read |
write |
float |
int |
bool |
not |
and |
or {lexeme=yytext(); line=yyline; column=yycolumn; return Reservada;}
{espacio} {/*Ignore*/}
"+" {lexeme=yytext(); line=yyline; column=yycolumn; return Suma;}
"-" {lexeme=yytext(); line=yyline; column=yycolumn; return Resta;}
"*" {lexeme=yytext(); line=yyline; column=yycolumn; return Multiplicacion;}
"/" {lexeme=yytext(); line=yyline; column=yycolumn; return Division;}
"^" {lexeme=yytext(); line=yyline; column=yycolumn; return Potencia;}
"<" {lexeme=yytext(); line=yyline; column=yycolumn; return Menor_que;}
"<=" {lexeme=yytext(); line=yyline; column=yycolumn; return Menor_o_igual;}
">" {lexeme=yytext(); line=yyline; column=yycolumn; return Mayor_que;}
">=" {lexeme=yytext(); line=yyline; column=yycolumn; return Mayor_o_igual;}
"==" {lexeme=yytext(); line=yyline; column=yycolumn; return Igual_que;}
"!=" {lexeme=yytext(); line=yyline; column=yycolumn; return Diferente_que;}
"=" {lexeme=yytext(); line=yyline; column=yycolumn; return Igual;}
";" {lexeme=yytext(); line=yyline; column=yycolumn; return Punto_y_coma;}
"," {lexeme=yytext(); line=yyline; column=yycolumn; return Coma;}
"(" {lexeme=yytext(); line=yyline; column=yycolumn; return Abre_parentesis;}
")" {lexeme=yytext(); line=yyline; column=yycolumn; return Cierra_parentesis;}
"{" {lexeme=yytext(); line=yyline; column=yycolumn; return Abre_llave;}
"}" {lexeme=yytext(); line=yyline; column=yycolumn; return Cierra_llave;}
{letra}({letra}|{digito})* {lexeme=yytext(); line=yyline; column=yycolumn; return Identificador;}
{digito}{digito}*(.{digito}{digito}*)? {lexeme=yytext(); line=yyline; column=yycolumn; return Numero;}
{comentario} {lexeme=yytext(); line=yyline; column=yycolumn; return Comentario;}
 . {lexeme=yytext(); line=yyline; column=yycolumn; return ERROR;}