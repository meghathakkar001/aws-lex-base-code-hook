package com.bank.ivr;

import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Slot;

import java.util.ArrayList;
import java.util.List;

public class IdentificationHook extends BaseHook{
    @Override
    protected LexResponse finalRFCMessage() {
        return null;
        //identification will never get a final message request
    }

    @Override
    protected LexResponse fulfillIntent() {
        return null;
    }

    @Override
    protected void initializeIntent() {
        Intent intent= new Intent();
        intent.setIntentName("identification");
        intent.setAcknowledgeIntent(false);
        List<Slot> slots= new ArrayList<>();

        Slot machineID= new Slot();
        machineID.setSlotName("midtid");
        machineID.setPrimaryPrompt("<speak>For identification, please tell me your machine I.D.</speak>");
        slots.add(machineID);
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("identification");

        this.setIntent(intent);

    }
}
