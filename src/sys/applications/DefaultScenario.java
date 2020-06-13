package sys.applications;

import sys.State;
import sys.Core.*;
import sys.models.IScenario;


public class DefaultScenario implements IScenario {

    //Non fa nulla
    @Ready
    @Override
    public void oneTimeAction(State currentState) { }

    //Non fa nulla
    @Ready
    @Override
    public void dailyAction(State currentState) { }

    //Non fa nulla
    @Ready
    @Override
    public void frameAction(State currentState) { }
}
