package logic;

import domain.CarAllowance;
import domain.RoxelRegistration;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;
import org.openspaces.events.polling.ReceiveHandler;
import org.openspaces.events.polling.receive.ReceiveOperationHandler;
import org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler;

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
        } else {
            CarAllowance allowance = new CarAllowance(reg.getCarId(), reg.getRoxelId());
            gs.write(allowance);
        }
    }

    @ReceiveHandler
    public ReceiveOperationHandler receiveHandler() {
        SingleTakeReceiveOperationHandler receiveHandler = new SingleTakeReceiveOperationHandler();
        receiveHandler.setNonBlocking(true);
        return receiveHandler;
    }
}
