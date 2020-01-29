package com.bank.ivr;

import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

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
        intent.setIntentType(IntentType.DEFAULT);
        List<Slot> slots= new ArrayList<>();

        Slot machineID= new Slot();
        String[] noMatchPrompts = new String[2];
        noMatchPrompts[0] = "Sorry I didn't catch that, Please say or enter your Merchant or Terminal ID";
        noMatchPrompts[1] = "Sorry I didn't catch that, Please say or tap in your Merchant or Terminal ID";
        machineID.setNoMatchPrompts(noMatchPrompts);
        machineID.setSlotName("midtid");
        machineID.setPrimaryPrompt("For identification, please tell me your merchant I.D.");
        slots.add(machineID);
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("identification");

        this.setIntent(intent);

    }
}
