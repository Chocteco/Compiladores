package compilador;

import java.util.logging.Level;
import java.util.logging.Logger;
import jflex.exceptions.SilentExit;

/**
 *
 * @author kevin
 */
public class ExecuteJFlex {

    public static void main(String omega[]) {
        String lexerFile = System.getProperty("user.dir") + "/src/compilador/Lexer.flex",
                lexerColor = System.getProperty("user.dir") + "/src/compilador/LexerColor.flex";
        try {
            jflex.Main.generate(new String[]{lexerFile, lexerColor});
        } catch (SilentExit ex) {
            Logger.getLogger(ExecuteJFlex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
