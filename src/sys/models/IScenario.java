package sys.models;

import sys.State;

public interface IScenario {

    void oneTimeAction(State currentState);
    void dailyAction(State currentState);
    void frameAction(State currentState);
}
