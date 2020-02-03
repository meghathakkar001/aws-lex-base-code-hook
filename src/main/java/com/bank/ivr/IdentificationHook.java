package com.bank.ivr;

import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

import java.util.ArrayList;
import java.util.List;

public class IdentificationHook extends BaseHook{
    @Override
    protected LexResponse finalRFCMessage() {
    	DialogAction dialogAction= new DialogAction();
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        Message message= new Message();

        message.setContent("<speak>Let me put you through to someone who can help</speak>");
        message.setContentType("SSML");
        dialogAction.setMessage(message);
        //dialogAction.setSlots(intent.get);
        LexResponse lexResponse= new LexResponse();
        lexResponse.setDialogAction(dialogAction);

        return lexResponse;
    }

    @Override
    protected LexResponse fulfillIntent() {
        return null;
    }

    @Override
    protected void initializeIntentHook() {

    }



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
        machineID.setPrimaryPrompt("<break time=\"1s\" />For identification please tell me your merchant I D");
        slots.add(machineID);
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("identification");
        
        List<Slot> optionalSlots = new ArrayList<>();
        intent.setOptionalSlots(optionalSlots);
        this.setIntent(intent);

    }


}
