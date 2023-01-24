package Graphics.Header.RestartComponent;
//CreateTime: 2023-01-22 2:44 p.m.

import Graphics.Adapter.ListenerAdapter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RestartListener extends ListenerAdapter {
    private Restart parent;
    public RestartListener(Restart parent){
        this.parent = parent;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        parent.restartTheGame();
    }
}
