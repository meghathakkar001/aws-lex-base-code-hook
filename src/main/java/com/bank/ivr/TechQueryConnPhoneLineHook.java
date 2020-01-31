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

public class TechQueryConnPhoneLineHook extends BaseHook {
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
    protected void initializeIntentHook() {
    	/*
	    Intent intent= new Intent();
        intent.setIntentName("tech_query_connection_phoneline");
        intent.setAcknowledgeIntent(true);
        intent.setAcknowledgementPrompt("Okay.");
        intent.setIntentType(IntentType.DEFAULT);
        List<Slot> slots= new ArrayList<>();
        intent.setMandatorySlots(slots);
        intent.setPreRequisites(new ArrayList<>());
        intent.setIntentFunction("tech_query_connection_phoneline");
        List<Prerequisite> preRequisites= new ArrayList<>();
        Prerequisite prerequisite= new Prerequisite();
        prerequisite.setIntentName("identification");
        prerequisite.setLambdaCodeHookAlias("IdentificationCodeHook");
        preRequisites.add(prerequisite);
        intent.setPreRequisites(preRequisites);


        this.setIntent(intent);
        */
    }

}
