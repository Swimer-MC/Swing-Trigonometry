package org.restudios;

public class FPSMgr {

    public int fps = 0;
    private long lastTime = System.currentTimeMillis();
    private int frameCount = 0;
    public void updateFps() {
        long currentTime = System.currentTimeMillis();
        frameCount++;

        if (currentTime - lastTime >= 50) {
            fps = (int) (frameCount / ((currentTime - lastTime) / 1000.0));
            frameCount = 0;
            lastTime = currentTime;
        }
    }
}
