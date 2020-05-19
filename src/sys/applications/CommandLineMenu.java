package sys.applications;
import sys.Config;
import sys.Core.*;
import sys.Simulation;
import sys.State;
import sys.models.IMenu;

public class CommandLineMenu implements IMenu {

    @Override
    @NotImplemented
    public int show() { return -1; }

    @Override
    @NotImplemented
    public void firstInput(Config config) { }

    @Override
    @NotImplemented
    public int settings(Config config) { return 0; }

    @Override
    @NotImplemented
    public void feedback(State state){ }

    @Override
    @NotImplemented
    public void finalFeedback(State state) { }


}
