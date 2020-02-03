package com.bank.ivr;

import java.util.ArrayList;
import java.util.List;

import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

public class AmendMerchNameHook extends BaseHook {
	@Override
    protected LexResponse finalRFCMessage() {
        DialogAction dialogAction= new DialogAction();
        dialogAction.setFulfillmentState("Fulfilled");
        dialogAction.setType("Close");
        Message message= new Message();

        message.setContent("Thanks. Transferring you to an agent who can help with this");
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
        intent.setIntentName("amend_merchant_name");
        intent.setAcknowledgeIntent(true);
        intent.setAcknolwegementPrompt("Okay. amend the merchant name. ");
        intent.setIntentType(IntentType.DISAMBIGUATION);
        List<Slot> slots= new ArrayList<>();
        intent.setMandatorySlots(slots);
        Slot machineID= new Slot();
        machineID.setSlotName("disamb_yn_changeofdetails");
        machineID.setPrimaryPrompt("Have any of your business details changed with companies house recently");
        String[] noMatchPrompts = new String[2];
        noMatchPrompts[0] = "Have any of your business details changed with companies house recently";
        noMatchPrompts[1] = "Have any of your business details changed with companies house recently";
        machineID.setNoMatchPrompts(noMatchPrompts);
        slots.add(machineID);
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("amend_merchant_name");
        intent.setDefaultTag("not_sure_change_of_details");
        List<Slot> optionalSlots = new ArrayList<>();
        intent.setOptionalSlots(optionalSlots);
        this.setIntent(intent);
    }

}
