package org.ei.telemedicine.view.dialog;

import org.ei.telemedicine.R;
import org.ei.telemedicine.provider.SmartRegisterClientsProvider;

import static org.ei.telemedicine.Context.getInstance;

public class FPPrioritizationTwoPlusChildrenServiceMode extends FPPrioritizationAllECServiceMode {

    public FPPrioritizationTwoPlusChildrenServiceMode(SmartRegisterClientsProvider provider) {
        super(provider);
    }

    @Override
    public String name() {
        return getInstance().getStringResource(R.string.fp_prioritization_two_plus_children_service_mode);
    }
}
