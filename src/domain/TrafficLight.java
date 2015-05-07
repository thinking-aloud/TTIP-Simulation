/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import helper.XapHelper;

/**
 *
 * @author jo
 */
public class TrafficLight implements Runnable {
    
    private final Roxel rox;
    private final XapHelper xapHelper;
    
    public TrafficLight(Roxel rox) {
        this.rox = rox;
        this.xapHelper = new XapHelper();
    }

    private boolean carOnPreviousRoxel() {
        if (rox.getOpenDirection() == Car.DrivingDirection.EAST && xapHelper.getRoxelById(rox.getWest()) == null) {
            return true;
        }
        return rox.getOpenDirection() == Car.DrivingDirection.SOUTH && xapHelper.getRoxelById(rox.getNorth()) == null;
    }

    public void handleRoxel() {
        if (!carOnPreviousRoxel()) {
            if (rox.getOpenDirection() == Car.DrivingDirection.EAST) {
                rox.setOpenDirection(Car.DrivingDirection.SOUTH);
            } else {
                rox.setOpenDirection(Car.DrivingDirection.EAST);
            }
        }
        xapHelper.releaseRoxel(rox);
    }

    @Override
    public void run() {
        handleRoxel();
    }
}
