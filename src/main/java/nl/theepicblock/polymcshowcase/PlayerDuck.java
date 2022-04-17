package nl.theepicblock.polymcshowcase;

public interface PlayerDuck {
    boolean getIsUsingPolyMc();
    void setIsUsingPolyMc(boolean v);

    long getPolyMcLastSwapTick();
    void setPolyMcLastSwapTick(long v);
}
