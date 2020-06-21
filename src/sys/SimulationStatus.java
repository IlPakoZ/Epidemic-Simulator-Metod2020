package sys;

enum SimulationStatus {
    NOT_YET_STARTED, PLAYING, ERADICATED_DISEASE, NONE_SURVIVED, NO_MORE_RESOURCES;

    @Override
    public String toString(){
        return this.name().replace('_', ' ');
    }
}
