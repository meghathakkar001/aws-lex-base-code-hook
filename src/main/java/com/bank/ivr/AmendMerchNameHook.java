package com.bank.ivr;

import java.util.ArrayList;
import java.util.List;

import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Prerequisite;
import com.bank.ivr.model.Slot;

public class AmendMerchNameHook extends BaseHook {
	@Override
    protected LexResponse finalRFCMessage() {
        DialogAction dialogAction= new DialogAction();
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        Message message= new Message();

        message.setContent("<speak>Thanks. Transferring you to an agent who can help with this</speak>");
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
    protected void initializeIntent() {
    	Intent intent= new Intent();
        intent.setIntentName("amend_merchant_name");
        intent.setAcknowledgeIntent(false);
        List<Slot> slots= new ArrayList<>();

        Slot machineID= new Slot();
        machineID.setSlotName("disamb_yn_changeofdetails");
        machineID.setPrimaryPrompt("<speak>Have any of your business details changed with companies house recently"
        		+ "</speak>");
        slots.add(machineID);
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("amend_merchant_name");

        this.setIntent(intent);
    }

}
