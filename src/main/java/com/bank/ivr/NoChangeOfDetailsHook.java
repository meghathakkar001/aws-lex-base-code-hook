package com.bank.ivr;

import java.util.ArrayList;
import java.util.List;

import com.bank.ivr.model.DialogAction;
import com.bank.ivr.model.Intent;
import com.bank.ivr.model.LexResponse;
import com.bank.ivr.model.Message;
import com.bank.ivr.model.Prerequisite;
import com.bank.ivr.model.Slot;
import com.bank.ivr.model.Intent.IntentType;

public class NoChangeOfDetailsHook extends BaseHook {
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
        intent.setIntentName("no_change_of_details");
        intent.setAcknowledgeIntent(true);
        intent.setAcknolwegementPrompt("Okay. ");
        intent.setIntentType(IntentType.DEFAULT);
        List<Slot> slots= new ArrayList<>();
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentAlias("no_change_of_details");
        List<Prerequisite> preRequisites= new ArrayList<>();
        Prerequisite prerequisite= new Prerequisite();
        prerequisite.setIntentName("identification");
        prerequisite.setLambdaCodeHookAlias("IdentificationCodeHook");
        preRequisites.add(prerequisite);
        intent.setPreRequisites(preRequisites);
        this.setIntent(intent);
    }

}
