/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phantomlauncher;

/**
 *
 * @author Alexander
 */
public interface ScreenInterface {

    // This method will be used to assign the handler to each "screen".
    // The different "screens" (menu and program) will use the handler 
    // when they want to change the "screen". // ett interface som ska köras för alla så att alla berättar vad dom är.
    void ScreenHandler(ScreenController screen);
}
