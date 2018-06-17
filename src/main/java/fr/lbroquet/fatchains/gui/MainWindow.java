package fr.lbroquet.fatchains.gui;

import com.googlecode.lanterna.gui2.AbstractWindow;
import com.googlecode.lanterna.gui2.Border;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;

/**
 * A simple window with a 'Quit' button, wrapping a given panel.
 */
class MainWindow extends AbstractWindow {

    MainWindow(Panel panel) {
        super("Fat Chains");
        Border border = Borders.singleLine();
        border.setComponent(panel);

        Panel mainPanel = new Panel();
        mainPanel.addComponent(border);
        mainPanel.addComponent(new Button("Quit", this::close), LinearLayout.createLayoutData(LinearLayout.Alignment.End));
        setComponent(mainPanel);
    }

}
