/*
 * Created on 04.04.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author clecon
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/* WindowClosingAdapter.java */

import java.awt.*;
import java.awt.event.*;

public class WindowClosingAdapter
extends WindowAdapter
{
  private boolean exitSystem;

  /**
   * Erzeugt einen WindowClosingAdapter zum Schliessen
   * des Fensters. Ist exitSystem true, wird das komplette
   * Programm beendet.
   */
  public WindowClosingAdapter(boolean exitSystem)
  {
    this.exitSystem = exitSystem;
  }

  /**
   * Erzeugt einen WindowClosingAdapter zum Schliessen
   * des Fensters. Das Programm wird nicht beendet.
   */
  public WindowClosingAdapter()
  {
    this(false);
  }

  public void windowClosing(WindowEvent event)
  {
    event.getWindow().setVisible(false);
    event.getWindow().dispose();
    if (exitSystem) {
      System.exit(0);
    }
  }
}