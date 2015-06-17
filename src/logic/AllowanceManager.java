/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import domain.CarAllowance;
import domain.Roxel;
import domain.RoxelRegistration;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;

/**
 *
 * @author jo
 */
@EventDriven
@Polling
@NotifyType(write = true, update = true)
public class AllowanceManager {

    @GigaSpaceContext
    GigaSpace gs;

    public AllowanceManager(GigaSpace gs) {
        this.gs = gs;
    }

    @EventTemplate
    public RoxelRegistration registration() {
        return new RoxelRegistration();
    }

    @SpaceDataEvent
    public void handleRegistration(RoxelRegistration reg) {
        if (reg.getTimer() > 0) {
            reg.setTimer(reg.getTimer() - 1);
            gs.write(reg);
        } else /*if (reg.getRoxelId() != null)*/ {
            // Roxel desired = gs.readById(Roxel.class, reg.getRoxelId());
            CarAllowance allowance = new CarAllowance(reg.getCarId(), reg.getRoxelId());
            gs.write(allowance);
        }
    }
}
